package bookmarks;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import bookmarks.dao.*;
import bookmarks.domain.Entry;
import bookmarks.domain.Tag;
import bookmarks.io.ConsoleIO;
import bookmarks.io.IO;

public class Main {
	public EntryDao entryDao;
	public TagDao tagDao;
	private IO io;

	public Main(IO io, String db) {
		this.io = io;
		Database database;
		try {
			database = new Database("jdbc:sqlite:" + db);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}

		tagDao = new TagDao(database);
		EntryTagDao entryTagDao = new EntryTagDao(database, tagDao);
		EntryMetadataDao entryMetadataDao = new EntryMetadataDao(database);
		entryDao = new EntryDao(database, entryTagDao, entryMetadataDao);
	}

	public void addCommand() {
		Map<String, String> metadata = new HashMap<>();

		String type = null;
		String[] fields = null;

		while (fields == null) {
			type = io.readLine("Type: ");
			if (type == null) {
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

		if (!readFields(metadata, fields)) {
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

	public void editCommand() {
		Entry entry = getEntryTo("edit");
		if (entry == null) {
			return;
		}

		if (!readFields(entry.getMetadata(), entry.getFields())) {
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
			String label = field.substring(0, 1).toUpperCase() + field.substring(1);
			String currentVal = metadata.get(field);
			String val = io.readLine(String.format(currentVal == null ? "%s: " : "%s [%s]: ", label, currentVal));
			if (val == null) {
				return false;
			}
			if (currentVal == null || !val.isEmpty()) {
				metadata.put(field, val);
			}
		}
		return true;
	}

	private Set<Tag> readTags(Set<Tag> existingTags) {
		String existingTagsStr = existingTags != null ? existingTags.stream()
			.map(Tag::getName)
			.collect(Collectors.joining(", ")) : null;
		String newTags = io.readLine(String.format(existingTags != null ? "Tags [%s]: " : "Tags: ", existingTagsStr));
		if (newTags == null) {
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

	public void deleteCommand() {
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

	public void viewCommand() {
		Entry entry = getEntryTo("view");
		if (entry == null) {
			return;
		}
		io.print(entry.toLongString());
	}
	
	public void searchCommand() {
		String query = io.readLine("Term to search: ");
		try {
			List<Entry> entries = entryDao.search(query);
			if (entries.isEmpty()) {
				io.print("No matches :(");
			} else {
				io.printf("%d match%s", entries.size(), entries.size() > 1 ? "es" : "");
				for (Entry entry : entries) {
					io.print(entry.toShortString());
				}
			}
		} catch (Exception e) {
			io.print("Failed to search :(");
			e.printStackTrace();
		}
	}

	public void listCommand() {
		try {
			List<Entry> entries = entryDao.findAll();
			if (entries.isEmpty()) {
				io.print("No entries :(");
			}
			for (Entry entry : entries) {
				io.print(entry.toShortString());
			}
		} catch (Exception e) {
			io.print("Failed to get list :(");
			e.printStackTrace();
		}
	}

	public void helpHomeCommand() {
		io.print("(a) add    - add a new entry");
		io.print("(d) delete - delete an existing entry");
		io.print("(e) edit   - edit an existing entry");
		io.print("(h) help   - print this screen");
		io.print("(l) list   - list all entries");
		io.print("(q) quit   - exits the program");
		io.print("(s) search - search for an entry");
		io.print("(t) tags   - takes you to tag section");
		io.print("(v) view   - view an existing entry");
	}

	public void helpTagCommand() {
		io.print("(b) back   - return back to home");
		io.print("(d) delete - delete an existing tag");
		io.print("(h) help   - print this screen");
		io.print("(l) list   - list all tags");
		io.print("(s) search - search for entries with tag");
	}

	public void run() {
		io.print("bookmarks v0.1.0");
		helpHomeCommand();
		while (true) {
			String comm = io.readLine("> ");
			if (comm == null) {
				io.print("Bye!");
				return;
			}
			switch (comm.toLowerCase()) {
				case "":
					continue;
				case "add":
				case "a":
					addCommand();
					break;
				case "edit":
				case "e":
					editCommand();
					break;
				case "search":
				case "find":
				case "s":
					searchCommand();
					break;
				case "delete":
				case "d":
					deleteCommand();
					break;
				case "view":
				case "show":
				case "v":
					viewCommand();
					break;
				case "list":
				case "ls":
				case "l":
					listCommand();
					break;
				case "tags":
				case "t":
					tagsSubSection();
					continue;
				case "quit":
				case "exit":
				case "q":
					io.print("Bye!");
					return;
				case "help":
				case "h":
					helpHomeCommand();
					break;
				default:
					io.print("Unrecognized command");
			}
			io.print("");
		}
	}
	
	public void findTags() {
		String query = io.readLine("Entries with tag (use %query% for substring of tag): ");
		try {
			List<Entry> entries = entryDao.findWithTag(query);
			if (entries.isEmpty()) {
				io.print("No matches with tag :(");
			} else {
				io.printf("%d match%s", entries.size(), entries.size() > 1 ? "es" : "");
				for (Entry entry : entries) {
					io.print(entry.toShortString());
				}
			}
		} catch (Exception e) {
			io.print("Failed to find :(");
			e.printStackTrace();
		}
	}

	public void listTags() {
		try {
			List<Tag> tags = tagDao.findAll();
			if (tags.isEmpty()) {
				io.print("No tags");
			}
			for (Tag tag : tags) {
				io.print(tag.toString());
			}
		} catch (Exception e) {
			io.print("Failed to get list :(");
			e.printStackTrace();
		}
	}

	public void deleteTag() {
		int id = io.readInt("ID of tag to delete: ");
		Tag tag;
		try {
			tag = tagDao.findOne(id);
		} catch (SQLException e) {
			io.print("Failed to fetch tag :(");
			e.printStackTrace();
			return;
		}
		if (tag == null) {
			io.print("Tag not found");
			return;
		}

		io.print(tag.toString());
		String confirmation = io.readLine("Are you sure you want to delete the tag [y/N]? ").toLowerCase();
		if (!confirmation.startsWith("y")) {
			io.print("Deletion cancelled");
			return;
		}

		try {
			tagDao.delete(id);
			io.print("Tag deleted successfully");
		} catch (Exception e) {
			e.printStackTrace();
			io.print("Failed to delete tag :(");
		}
	}

	public void tagsSubSection() {
		io.print("");
		helpTagCommand();
		while (true) {
			String comm = io.readLine("tags> ");
			if (comm == null) {
				return;
			}
			switch (comm.toLowerCase()) {
				case "":
					continue;
				case "search":
				case "find":
				case "s":
					findTags();
					break;
				case "list":
				case "ls":
				case "l":
					listTags();
					break;
				case "delete":
				case "d":
					deleteTag();
					break;
				case "back":
				case "return":
				case "exit":
				case "b":
					return;
				case "help":
				case "h":
					helpTagCommand();
					break;
				default:
					io.print("Unrecognized command");
			}
			io.print("");
		}
	}

	public static void main(String[] args) {
		IO io = new ConsoleIO();
		new Main(io, "bookmarks.db").run();
	}
}
