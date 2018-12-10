package bookmarks.ui;

import java.io.EOFException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import bookmarks.Sorter;
import bookmarks.dao.*;
import bookmarks.domain.Entry;
import bookmarks.domain.Tag;
import bookmarks.io.AbstractIO;
import bookmarks.io.IO;

public class App {
	static final int ENTRIES_PER_PAGE = 10;

	public Database database;
	public EntryDao entryDao;
	public TagDao tagDao;
	public boolean isNewUser;
	private IO io;
	private Tags tags;
	private EntryTypes entryTypes;
	private Entry[] prevList;
	private int shownPages;
	private int currPage;

	public App(IO io, String db) {
		this.io = io;
		try {
			database = new Database("jdbc:sqlite:" + db);
		} catch (SQLException e) {
			System.out.println("Failed to connect to database");
			System.exit(1);
			return;

		}
		this.isNewUser = database.createNewTables();

		entryTypes = new EntryTypes(io);
		tagDao = new TagDao(database);
		EntryTagDao entryTagDao = new EntryTagDao(database, tagDao);
		EntryMetadataDao entryMetadataDao = new EntryMetadataDao(database);
		entryDao = new EntryDao(database, entryTagDao, entryMetadataDao);

		this.tags = new Tags(io, tagDao, entryDao);
	}


	public void clearPrevList() {
		prevList = null;
	}

	public void sort(List<String> order) {
		if (prevList == null || prevList.length == 0) {
			io.print("Nothing to sort. Try using `list`, `search` or `view` first");
			return;
		}

	}

	public void setOfflineMode(boolean offline) {
		this.entryTypes.offlineMode = offline;
	}

	public void showPage() {
		int totalPages = (prevList.length + ENTRIES_PER_PAGE - 1) / ENTRIES_PER_PAGE; // Round up
		int first = currPage * ENTRIES_PER_PAGE;

		if (first >= prevList.length) {
			io.print("Nothing to show");
		} else {
			io.print("Page " + (currPage + 1) + " out of " + (totalPages) + ":");
			for (int i = first; (i < prevList.length) && (i < first + ENTRIES_PER_PAGE); ++i) {
				io.print(prevList[i].toShortString());
			}
		}
	}

	public void showNextPage() {
		++currPage;
		showPage();
	}

	public void showGivenPage() {
		int i = io.readInt("Page to show: ") - 1;
		if (i < 0) {
			io.print("Please give a positive index.");
		} else {
			currPage = i;
			showPage();
		}
	}

	public void showList(Entry[] list, boolean shortStr, String noMatches, String oneMatch, String manyMatches) {
		prevList = list;
		currPage = -1;

		if (list.length > 10) {
			showNextPage();
		} else {

			if (list.length == 0) {
				io.print(noMatches);
			} else {
				++currPage;

				if (list.length == 1) io.print(oneMatch);
				else io.print(manyMatches);

				for (Entry entry : list) {
					if (shortStr) io.print(entry.toShortString());
					else io.print(entry.toLongString());
				}
			}
		}
	}

	public void sortPrevList(List<String> order) {
		int n = prevList.length;
		for (int j = order.size() - 1; j >= 0; --j) {
			String comp = order.get(j);

			int[] perm = null;
			if (comp.equals("Id") || comp.equals("read")) {
				// Sort by int
				int[] data = new int[n];
				for (int i = 0; i < n; ++i) {
					if (comp.equals("read")) data[i] = prevList[i].isRead() ? 1 : 0;
					else data[i] = prevList[i].getID();
				}

				perm = Sorter.sortInts(data);
			} else {
				// Sort by string
				String[] data = new String[n];
				for (int i = 0; i < n; ++i) {
					data[i] = prevList[i].getMetadataEntry(comp);
					if (data[i] == null) data[i] = "";
				}
				perm = Sorter.sortStrings(data);
			}

			Sorter.permute(prevList, perm);
		}
	}

	public void sort() {
		if (prevList == null || prevList.length == 0) {
			io.print("Nothing to sort. Try using `list`, `search` or `view` first");
			return;
		}

		// Get parameters to sort by
		List<String> order = new ArrayList<>();
		for (int i = 1; ; ++i) {
			String pat = "" + i;

			if (i == 1) pat += "st";
			else if (i == 2) pat += "nd";
			else if (i == 3) pat += "rd";
			else pat += "th";

			pat += " parameter to sort by: ";
			String str = io.readWord(pat);

			if (str == null || str.length() == 0) break;
			order.add(str);
		}

		// Sort
		sortPrevList(order);

		// Redisplay
		showList(prevList, true, "No entries :(", "Entry:", "Entries:");
	}

	public void export() {
		if (prevList == null || prevList.length == 0) {
			io.print("Nothing to export. Try using `list`, `search` or `view` first");
			return;
		}
		String fileName = io.readLine("File to export to: ");
		try {
			String str = Arrays.stream(prevList)
				.map(Entry::toLongString)
				.collect(Collectors.joining("\n"));
			io.writeFile(fileName, str);
			io.print("Export successful");
		} catch (IOException e) {
			io.print("Failed to write file");
		}
	}

	public void add() {
		String typeName = null;
		Entry.Type type = null;

		while (type == null) {
			typeName = io.readWord("Type: ");
			if (typeName.equals(AbstractIO.EndOfTransmission)) {
				io.print("Adding cancelled");
				return;
			}

			type = entryTypes.getType(typeName);

			if (type == null) {
				io.printf("Unrecognized type. Choose one of: %s", String.join(" ", entryTypes.getTypes()));
			}
		}
		Map<String, String> metadata = new HashMap<>();
		metadata.put("type", typeName);

		Entry entry = new Entry(new HashSet<>(), metadata);
		try {
			type.read(entry);
		} catch (EOFException e) {
			io.print("Adding cancelled");
			return;
		}
		Set<Tag> tags = readTags(entry.getTags());
		if (tags == null) {
			io.print("Adding cancelled");
			return;
		}
		entry.setTags(tags);

		saveEntry(entry, "created");
	}

	public void edit() {
		Entry entry = getEntryTo("edit");
		if (entry == null) {
			return;
		}

		try {
			entry.getType().read(entry);
		} catch (EOFException e) {
			io.print("Editing cancelled");
			return;
		}
		Set<Tag> tags = readTags(entry.getTags());
		if (tags == null) {
			io.print("Editing cancelled");
			return;
		}
		entry.setTags(tags);
		saveEntry(entry, "updated");
	}

	private void saveEntry(Entry e, String action) {
		try {
			entryDao.save(e);
			io.printf("Entry %s", action);
		} catch (Exception err) {
			io.print("Failed to save :(");
			err.printStackTrace();
		}
	}

	private Set<Tag> readTags(Set<Tag> existingTags) {
		String existingTagsStr = existingTags.stream()
			.map(Tag::getName)
			.collect(Collectors.joining(", "));
		String newTags = io.readLine(String.format(!existingTags.isEmpty() ? "Tags [%s]: " : "Tags: ", existingTagsStr));
		if (newTags.equals(AbstractIO.EndOfTransmission)) {
			return null;
		}
		if (!newTags.isEmpty()) {
			return Arrays.stream(
				newTags.split(","))
				.map(s -> new Tag("tag", s.trim()))
				.collect(Collectors.toSet());
		}
		return existingTags;
	}

	private Entry getEntryTo(String action) {
		int id = io.readInt("ID of entry to " + action + ": ");
		if (id <= 0) {
			io.print("Invalid entry ID");
			return null;
		}
		Entry entry;
		try {
			entry = entryDao.findOne(id);
		} catch (SQLException e) {
			io.print("Failed to fetch entry :(");
			e.printStackTrace();
			return null;
		}
		if (entry == null) {
			io.print("Entry not found");
			return null;
		}
		return entry;
	}

	public void delete() {
		Entry entry = getEntryTo("delete");
		if (entry == null) {
			return;
		}
		io.print(entry.toLongString());
		String confirmation = io.readLine("\nAre you sure you want to delete the entry [y/N]? ").toLowerCase();
		if (!confirmation.startsWith("y")) {
			io.print("Deletion cancelled");
			return;
		}

		try {
			entryDao.delete(entry.getID());
			io.print("Entry deleted successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			io.print("Failed to delete entry :(");
		}
	}

	public void setRead(boolean read) {
		String readStatus = (read ? "read" : "unread");
		Entry entry = getEntryTo("mark as " + readStatus);
		if (entry == null) {
			return;
		}
		if (entry.isRead() == read) {
			io.printf("Entry was already marked as %s", readStatus);
			return;
		}
		entry.setRead(read);
		try {
			entryDao.save(entry);
			if (read) {
				io.printf("Entry marked as %s", readStatus);
			} else {
				io.printf("Entry marked as %s", readStatus);
			}
		} catch (Exception e) {
			io.printf("Failed to mark as %s :(", readStatus);
			e.printStackTrace();
		}
	}

	public void view() {
		Entry entry = getEntryTo("view");
		if (entry == null) {
			return;
		}

		Entry[] list = new Entry[1];
		list[0] = entry;
		showList(list, false, "", "", "");
	}

	public void search() {
		String query = io.readLine("Term to search: ");
		try {
			List<Entry> data = entryDao.search(query);
			Entry[] entries = data.toArray(new Entry[0]);
			showList(entries, true, "No results :(", "Match:", "Matches:");
		} catch (Exception e) {
			io.print("Failed to search :(");
			e.printStackTrace();
		}
	}

	public void list() {
		String listUnread = io.readLine("\nList only unread entries [y/N]? ").toLowerCase();
		boolean unreadOnly = listUnread.startsWith("y") || listUnread.startsWith("u");

		try {
			List<Entry> data = unreadOnly
				? entryDao.findAllUnread()
				: entryDao.findAll();
			Entry[] entries = data.toArray(new Entry[0]);

			String showStr = (unreadOnly ? "\nUnread Entries:" : "\nEntries:");
			showList(entries, true, "No entries :(", showStr, showStr);
		} catch (Exception e) {
			io.print("Failed to get list :(");
			e.printStackTrace();
		}
	}

	public void printHelp() {
		io.print("(shortcut) command - description");
		io.print("(a) add    - add a new entry");
		io.print("(d) delete - delete an existing entry");
		io.print("(e) edit   - edit an existing entry");
		io.print("(f) find   - search for an entry");
		io.print("(h) help   - print this screen");
		io.print("(i) intro  - takes you to the introduction");
		io.print("(l) list   - list all entries");
		io.print("(n) next   - show next page of pagination");
		io.print("(p) page   - show given page of pagination");
		io.print("(q) quit   - exits the program");
		io.print("(r) read   - mark an entry as read");
		io.print("(t) tags   - takes you to tag section");
		io.print("(u) unread - mark an entry as unread");
		io.print("(v) view   - view the full details of an existing entry");
		io.print("(x) export - export the previously printed list");
	}

	public void help() {
		printHelp();

		io.print("Type command to view more detailed help, or press enter to cancel.");
		String comm = io.readWord("help> ");
		if (comm.equals(AbstractIO.EndOfTransmission)) {
			return;
		}
		switch (comm.toLowerCase()) {
			case "":
			case "back":
			case "return":
			case "exit":
			case "b":
				return;
		}
		io.print("");
	}

	public void run() {
		if (isNewUser) {
			new Introduction(this, io).run();
		}
		io.print("bookmarks v0.1.0");
		io.print("Type \"help\" for help.");
		while (true) {
			String comm = io.readWord("> ");
			if (comm.equals(AbstractIO.EndOfTransmission)) {
				io.print("Bye!");
				return;
			}
			switch (comm.toLowerCase()) {
				case "":
					continue;
				case "add":
				case "a":
					add();
					break;
				case "export":
				case "x":
					export();
					break;
				case "edit":
				case "e":
					edit();
					break;
				case "find":
				case "search":
				case "f":
					search();
					break;
				case "sort":
				case "s":
					sort();
					break;
				case "delete":
				case "d":
					delete();
					break;
				case "read":
				case "r":
					setRead(true);
					break;
				case "unread":
				case "u":
					setRead(false);
					break;
				case "view":
				case "show":
				case "v":
					view();
					break;
				case "list":
				case "ls":
				case "l":
					list();
					break;
				case "next":
				case "n":
					showNextPage();
					break;
				case "page":
				case "p":
					showGivenPage();
					break;
				case "help":
				case "h":
					help();
					break;
				case "tags":
				case "t":
					tags.run();
					continue;
				case "quit":
				case "exit":
				case "q":
					io.print("Bye!");
					return;
				case "demo":
				case "intro":
				case "introduction":
				case "i":
					new Introduction(this, io).run();
					break;
				default:
					io.print("Unknown command. Type \"help\" for help.");
			}
			io.print("");
		}
	}
}
