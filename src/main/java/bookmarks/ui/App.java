package bookmarks.ui;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import bookmarks.dao.*;
import bookmarks.domain.Entry;
import bookmarks.domain.Tag;
import bookmarks.io.AbstractIO;
import bookmarks.io.IO;

import java.io.PrintWriter;

public class App {
	public EntryDao entryDao;
	public TagDao tagDao;
	public boolean isNewUser;
	private IO io;
	private Tags tags;
	private List<Entry> prevList;

	public App(IO io, String db) {
		this.io = io;
		Database database;
		try {
			database = new Database("jdbc:sqlite:" + db);
		} catch (SQLException e) {
			System.out.println("Failed to connect to database");
			System.exit(1);
			return;
		}
		this.isNewUser = database.createNewTables();

		tagDao = new TagDao(database);
		EntryTagDao entryTagDao = new EntryTagDao(database, tagDao);
		EntryMetadataDao entryMetadataDao = new EntryMetadataDao(database);
		entryDao = new EntryDao(database, entryTagDao, entryMetadataDao);

		this.tags = new Tags(io, tagDao, entryDao);
	}

	public void showList(List<Entry> list, boolean shortStr, String noMatches, String oneMatch, String manyMatches) {
		prevList = list;
		if (list.isEmpty()) {
			io.print(noMatches);
		} else {
			if (list.size() == 1) io.print(oneMatch);
			else io.print(manyMatches);
			
			for (Entry entry : list) {
				if (shortStr) io.print(entry.toShortString());
				else io.print(entry.toLongString());
			}
		}
	}

	public void export() {
		String fileName = io.readLine("File to export to: ");
		if (fileName != null) {
			try {
				io.writeFile(fileName, prevList.stream()
					.map(Entry::toLongString)
					.collect(Collectors.joining("\n")));
				io.print("Export successful");
			} catch (IOException e) {
				io.print("Failed to write file");
			}
		} else {
			io.print("Exporting cancelled");
		}
	}

	public void add() {
		Map<String, String> metadata = new HashMap<>();

		String type = null;
		String[] fields = null;

		while (fields == null) {
			type = io.readWord("Type: ");
			if (type.equals(AbstractIO.EndOfTransmission)) {
				io.print("Adding cancelled");
				return;
			}

			type = Entry.unshortenType(type.toLowerCase());
			if (type != null) {
				fields = Entry.getFieldsOfType(type.toLowerCase());
			}

			if (fields == null) {
				io.printf("Unrecognized type. Choose one of: %s", String.join(" ", Entry.getTypes()));
			}
		}
		metadata.put("type", type);

		if (readFields(metadata, fields)) {
			io.print("Adding cancelled");
			return;
		}
		Set<Tag> tags = readTags(null);
		if (tags == null) {
			io.print("Adding cancelled");
			return;
		}

		saveEntry(new Entry(tags, metadata), "created");
	}

	public void edit() {
		Entry entry = getEntryTo("edit");
		if (entry == null) {
			return;
		}

		if (readFields(entry.getMetadata(), entry.getFields())) {
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

	private boolean readFields(Map<String, String> metadata, String[] fields) {
		for (String field : fields) {
			while (true) {
				String label = field.substring(0, 1).toUpperCase() + field.substring(1);
				String currentVal = metadata.get(field);
				String val = io.readLine(String.format(currentVal == null ? "%s: " : "%s [%s]: ", label, currentVal));
				if (val.equals(AbstractIO.EndOfTransmission)) {
					return true;
				}

				if (currentVal == null || !val.isEmpty()) {
					if (validInput(field, val)) {
						metadata.put(field, val);
					} else {
						io.print("\"" + val + "\" is not a valid " + field);
						continue;
					}
				}
				break;
			}
		}
		return false;
	}

	private boolean validInput(String field, String input) {
		switch (field) {
			case "ISBN":
				return InputValidator.validateISBN(input);
			case "Link":
				return InputValidator.validateLink(input);
			default:
				return true;
		}
	}

	private Set<Tag> readTags(Set<Tag> existingTags) {
		String existingTagsStr = existingTags != null ? existingTags.stream()
			.map(Tag::getName)
			.collect(Collectors.joining(", ")) : null;
		String newTags = io.readLine(String.format(existingTags != null ? "Tags [%s]: " : "Tags: ", existingTagsStr));
		if (newTags.equals(AbstractIO.EndOfTransmission)) {
			return null;
		}
		if (existingTags == null || !newTags.isEmpty()) {
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

		List<Entry> list = new ArrayList<Entry>();
		list.add(entry);
		showList(list, false, "", "", "");
	}

	public void search() {
		String query = io.readLine("Term to search: ");
		try {
			List<Entry> entries = entryDao.search(query);
			showList(entries, true, "No results :(", "Match:", "Matches:");
		} catch (Exception e) {
			io.print("Failed to search :(");
			e.printStackTrace();
		}
	}

	public void list() {
		String listUnread = io.readLine("\nList only unread entries [y/N]? ").toLowerCase();
		boolean unreadOnly = (listUnread == null) || (listUnread.startsWith("y")) || (listUnread.startsWith("u"));

		try {
			List<Entry> entries = unreadOnly
				? entryDao.findAllUnread()
				: entryDao.findAll();

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
		io.print("(e) edit   - edit an existing entry");
		io.print("(x) export - export the previously printed list");
		io.print("(d) delete - delete an existing entry");
		io.print("(r) read   - mark an entry as read");
		io.print("(u) unread - mark an entry as unread");
		io.print("(v) view   - view the full details of an existing entry");
		io.print("(l) list   - list all entries");
		io.print("(s) search - search for an entry");
		io.print("(t) tags   - takes you to tag section");
		io.print("(h) help   - print this screen");
		io.print("(q) quit   - exits the program");
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
				case "search":
				case "find":
				case "s":
					search();
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
				default:
					io.print("Unknown command. Type \"help\" for help.");
			}
			io.print("");
		}
	}
}
