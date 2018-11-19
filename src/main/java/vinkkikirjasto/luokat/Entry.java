package vinkkikirjasto.luokat;

import java.util.*;

public class Entry {
	private int id;
	private String title, type, author, description, comment;
	private List<Tag> tags;
	private Map<String, String> metadata;

	public Entry(int id, String title, String type, String author, String description, String comment, List<Tag> tags, Map<String, String> metadata) {
		this.id = id;
		this.title = title;
		this.type = type;
		this.author = author;
		this.description = description;
		this.comment = comment;
		if (tags == null) {
			tags = new ArrayList<>();
		}
		this.tags = tags;
		if (metadata == null) {
			metadata = new HashMap<>();
		}
		this.metadata = metadata;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
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
				Objects.equals(title, entry.title) &&
				Objects.equals(type, entry.type) &&
				Objects.equals(author, entry.author) &&
				Objects.equals(description, entry.description) &&
				Objects.equals(comment, entry.comment) &&
				tags.equals(entry.tags) &&
				metadata.equals(entry.metadata);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, type, author, description, comment, tags, metadata);
	}

	@Override
	public String toString() {
		return "Entry{" +
				"id=" + id +
				", title='" + title + '\'' +
				", type='" + type + '\'' +
				", author='" + author + '\'' +
				", description='" + description + '\'' +
				", comment='" + comment + '\'' +
				", tags=" + tags.toString() +
				", metadata=" + metadata.toString() +
				'}';
	}
}
