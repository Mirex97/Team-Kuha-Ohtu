package bookmarks.domain;

import java.util.*;
import java.util.stream.Collectors;

public class Entry implements IDObject {
	private int id;
	private int read;
	private Set<Tag> tags;
	private Map<String, String> metadata;

	private static HashMap<String, String[]> typeFields = new HashMap<>();
	private static HashMap<String, String> typeShortcuts = new HashMap<>();

	static {
		typeFields.put("book", new String[]{"Title", "Author", "ISBN", "Description", "Comment"});
		typeFields.put("article", new String[]{"Title", "Author", "Paper", "Description", "Comment"});
		typeFields.put("blog", new String[]{"Title", "Author", "Link", "Description", "Comment"});
		typeFields.put("video", new String[]{"Title", "Author", "Link", "Description", "Comment"});
		typeFields.put("podcast", new String[]{"Title", "Author", "Podcast name", "Description", "Comment"});
		typeFields.put("meme", new String[]{"Title", "Author", "Image", "Up text", "Bottom text", "Comment"});

		typeShortcuts.put("b", "book");
		typeShortcuts.put("a", "article");
		typeShortcuts.put("l", "blog");
		typeShortcuts.put("v", "video");
		typeShortcuts.put("p", "podcast");
		typeShortcuts.put("m", "meme");

		typeShortcuts.put("book", "book");
		typeShortcuts.put("article", "article");
		typeShortcuts.put("blog", "blog");
		typeShortcuts.put("video", "video");
		typeShortcuts.put("podcast", "podcast");
		typeShortcuts.put("meme", "meme");
	}

	public static String[] getFieldsOfType(String type) {
		return typeFields.get(type);
	}

	public static String unshortenType(String type) {
		return typeShortcuts.get(type);
	}

	public static Set<String> getTypes() {
		return typeFields.keySet();
	}

	public Entry(int id) {
		this(id, new HashSet<>(), new HashMap<>());
	}

	public Entry(Set<Tag> tags, Map<String, String> metadata) {
		this(0, tags, metadata);
	}

	public Entry(int id, Set<Tag> tags, Map<String, String> metadata) {
		this.id = id;
		this.read = 0;
		this.tags = tags;
		this.metadata = metadata;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getRead() {
		return read;
	}
	
	public String isRead() {
		if (read == 1) {
			return "True";
		} else {
			return "False";
		}
	}
	

	public void setRead(int read) {
		this.read = read;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public String getType() {
		return this.metadata.get("type");
	}

	public String[] getFields() {
		return getFieldsOfType(getType());
	}

	public void setTags(List<Tag> tags) {
		this.tags = new HashSet<>(tags);
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public String toShortString() {
		return String.format("%d. %s: \"%s\" by %s", id, this.getType(), metadata.get("Title"), metadata.get("Author"));
	}

	public String toLongString() {
		StringBuilder str = new StringBuilder();
		str.append(String.format("\nEntry %d: \"%s\" by %s\n", id, metadata.get("Title"), metadata.get("Author")));
		for (String field : getFields()) {
			if (field.equals("Title") || field.equals("Author")) {
				continue;
			}
			String val = metadata.get(field);
			if (val != null && !val.isEmpty()) {
				str.append(String.format("\n%s: %s", field, val));
			}
		}		
		String tags = getTags().stream().filter(t -> t.getType().equals("tag")).map(Tag::getName).collect(Collectors.joining(", "));
		if (!tags.isEmpty()) {
			str.append(String.format("\nTags: %s", tags));
		}
		str.append(String.format("\n\n%s: %s", "Is read? ", isRead()));
		return str.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Entry entry = (Entry) o;
		return id == entry.id &&
			tags.equals(entry.tags) &&
			metadata.equals(entry.metadata);
	}

	@Override
	public String toString() {
		return String.format("Entry{id=%d, read=%d, tags=%s, metadata=%s}", id, read, tags, metadata);
	}

	@Override
	public int hashCode() {
		return id;
	}
}
