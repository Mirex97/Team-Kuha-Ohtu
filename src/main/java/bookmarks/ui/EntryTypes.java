package bookmarks.ui;

import bookmarks.domain.Entry;
import bookmarks.domain.InputValidators;
import bookmarks.domain.Tag;
import bookmarks.io.AbstractIO;
import bookmarks.io.IO;
import bookmarks.util.OpenLibrary;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EntryTypes {
	private IO io;
	private Map<String, Entry.Type> types = new HashMap<>();

	public EntryTypes(IO io) {
		this.io = io;
		this.addEntryType(new BookEntryType(), "book", "b");
		this.addEntryType(new VideoEntryType(), "video", "v");
		this.addEntryType(new SimpleEntryType("article",
				new String[]{"arXiv ID", "Title", "Author", "Description", "Comment"}),
			"article", "a");
		this.addEntryType(new SimpleEntryType("blog",
				new String[]{"Link", "Title", "Author", "Description", "Comment"}),
			"blog", "weblog", "l");
		this.addEntryType(new SimpleEntryType("podcast",
				new String[]{"Podcast name", "Title", "Author", "Description", "Comment"}),
			"podcast", "p");
		this.addEntryType(new SimpleEntryType("podcast",
				new String[]{"Title", "Author", "Image", "Up text", "Bottom text", "Comment"}),
			"podcast", "p");
	}

	public Entry.Type getType(String name) {
		return types.get(name);
	}

	public Set<String> getTypes() {
		return types.values().stream().map(Entry.Type::getName).collect(Collectors.toSet());
	}

	private void addEntryType(Entry.Type type, String... names) {
		Entry.registerType(type);
		for (String n : names) {
			types.put(n, type);
		}
	}

	public class SimpleEntryType implements Entry.Type {
		private String name;
		private String[] fields;
		private Map<String, InputValidators.Validator> validators;

		public SimpleEntryType(String name, String[] fields) {
			this(name, fields, new HashMap<>(InputValidators.ALL));
		}

		public SimpleEntryType(String name, String[] fields, Map<String, InputValidators.Validator> validators) {
			this.name = name;
			this.fields = fields;
			this.validators = validators;
		}

		public String getName() {
			return this.name;
		}

		public void addValidator(String key, InputValidators.Validator val) {
			validators.put(key, val);
		}

		public void read(Entry e) throws EOFException {
			for (String field : fields) {
				e.putMetadata(field, readSingleField(e, field));
			}
		}

		String readSingleField(Entry e, String field) throws EOFException {
			while (true) {
				String currentVal = e.getMetadata(field);
				String val = io.readLine(String.format(currentVal == null ? "%s: " : "%s [%s]: ", field, currentVal));
				if (val.equals(AbstractIO.EndOfTransmission)) {
					throw new EOFException();
				}

				if (!validInput(field, val)) {
					io.printf("\"%s\" is not a valid %s", val, field);
					continue;
				} else if (currentVal != null && val.isEmpty()) {
					return currentVal;
				}
				return val;
			}
		}

		public String formatShort(Entry e) {
			return String.format("%d. %s: \"%s\" by %s", e.getID(), e.getType(), e.getMetadata("Title"), e.getMetadata("Author"));
		}

		public String formatLong(Entry e) {
			StringBuilder str = new StringBuilder();
			str.append(String.format("\nEntry %d: \"%s\" by %s\n", e.getID(), e.getMetadata("Title"), e.getMetadata("Author")));
			for (String field : this.fields) {
				if (field.equals("Title") || field.equals("Author")) {
					continue;
				}
				String val = e.getMetadata(field);
				if (val != null && !val.isEmpty()) {
					str.append(String.format("\n%s: %s", field, val));
				}
			}
			String tags = e.getTags().stream()
				.filter(t -> t.getType().equals("tag"))
				.map(Tag::getName)
				.collect(Collectors.joining(", "));
			if (!tags.isEmpty()) {
				str.append(String.format("\nTags: %s", tags));
			}
			str.append(String.format("\nIs read: %s", e.isRead() ? "Yes" : "No"));
			return str.toString();
		}

		private boolean validInput(String field, String val) {
			return val.isEmpty() || validators.getOrDefault(field, InputValidators.NOOP).validate(val);
		}
	}

	public class BookEntryType extends SimpleEntryType {
		OpenLibrary library = new OpenLibrary();

		public BookEntryType() {
			super("book", new String[]{"Title", "Author", "Description", "Comment"}, new HashMap<>());
			addValidator("ISBN", InputValidators.ISBN);
		}

		private boolean fillFromOpenLibrary(Entry entry, String isbn) {
			JsonObject obj;
			try {
				obj = library.getByISBN(isbn);
			} catch (IOException e) {
				return false;
			}
			entry.putMetadata("Title", obj.get("title").getAsString());
			entry.putMetadata("Author", obj.getAsJsonArray("authors").get(0).getAsJsonObject().get("name").getAsString());
			entry.putMetadata("Publisher", obj.getAsJsonArray("publishers").get(0).getAsJsonObject().get("name").getAsString());
			for (JsonElement elem : obj.getAsJsonArray("subjects")) {
				String name = elem.getAsJsonObject().get("name").getAsString();
				entry.getTags().add(new Tag("tag", name));
			}
			return true;
		}

		@Override
		public void read(Entry e) throws EOFException {
			String isbn = readSingleField(e, "ISBN");
			if (e.getMetadata("ISBN") == null) {
				if (!isbn.isEmpty()) {
					io.print("Fetching book metadata from OpenLibrary...");
					if (!fillFromOpenLibrary(e, isbn)) {
						io.print("Failed to fetch metadata.");
					} else {
						io.print("Metadata fetched successfully.");
					}
				}
			}
			e.putMetadata("ISBN", isbn);
			super.read(e);
		}
	}

	public class VideoEntryType extends SimpleEntryType {
		public VideoEntryType() {
			super("video", new String[]{"Link", "Title", "Author", "Description", "Comment"});
		}

		// TODO fetch video info by youtube URL
	}
}
