package bookmarks.ui;

import bookmarks.dao.EntryDao;
import bookmarks.dao.TagDao;
import bookmarks.domain.Entry;
import bookmarks.domain.Tag;
import bookmarks.io.AbstractIO;
import bookmarks.io.IO;

import java.sql.SQLException;
import java.util.List;

public class Tags {
	IO io;
	TagDao tagDao;
	EntryDao entryDao;

	public Tags(IO io, TagDao tagDao, EntryDao entryDao) {
		this.io = io;
		this.tagDao = tagDao;
		this.entryDao = entryDao;
	}

	public void run() {
		io.print("");
		io.print("Type \"help\" for help.");
		while (true) {
			String comm = io.readWord("tags> ");
			if (comm.equals(AbstractIO.EndOfTransmission)) {
				return;
			}
			switch (comm.toLowerCase()) {
				case "":
					continue;
				case "search":
				case "find":
				case "s":
					search();
					break;
				case "list":
				case "ls":
				case "l":
					list();
					break;
				case "delete":
				case "d":
					delete();
					break;
				case "back":
				case "return":
				case "exit":
				case "b":
					return;
				case "help":
				case "h":
					printHelp();
					break;
				default:
					io.print("Unknown command. Type \"help\" for help.");
			}
			io.print("");
		}
	}

	public void printHelp() {
		io.print("(shortcut) command - description");
		io.print("(b) back   - return back to home");
		io.print("(d) delete - delete an existing tag");
		io.print("(h) help   - print this screen");
		io.print("(l) list   - list all tags");
		io.print("(s) search - search for entries with tag");
	}

	public void search() {
		String query = io.readLine("Tag to search: ");
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

	public void list() {
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

	public void delete() {
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
}
