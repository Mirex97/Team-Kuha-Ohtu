package bookmarks.domain;

import java.util.*;

public class Entry implements IDObject {
	private int id;
	private Set<Tag> tags;
	private Map<String, String> metadata;

	private static HashMap<String, String[]> typeFields = new HashMap<String, String[]>();
	static {
		typeFields.put("book", new String[]{"title", "author", "isbn", "description", "comment"});
		typeFields.put("article", new String[]{"title", "author", "paper", "description", "comment"});
		typeFields.put("blog", new String[]{"title", "author", "link", "description", "comment"});
		typeFields.put("video", new String[]{"title", "author", "link", "description", "comment"});
		typeFields.put("meme", new String[]{"title", "author", "image", "up-text", "bottom-text", "comment"});
	}
	public static String[] getFieldsOfType(String type) {
		return typeFields.get(type);
	}


	public Entry() {
		this(0, new HashSet<>(), new HashMap<>());
	}

	public Entry(int id) {
		this(id, new HashSet<>(), new HashMap<>());
	}

	public Entry(Set<Tag> tags, Map<String, String> metadata) {
		this(0, tags, metadata);
	}

	public Entry(int id, Set<Tag> tags, Map<String, String> metadata) {
		this.id = id;
		this.tags = tags;
		this.metadata = metadata;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
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
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return "Entry{" +
			"id=" + id +
			", tags=" + tags.toString() +
			", metadata=" + metadata.toString() +
			'}';
	}
}
