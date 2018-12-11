package bookmarks.domain;

import java.io.EOFException;
import java.util.*;
import java.util.stream.Collectors;

public class Entry implements IDObject {
	private int id;
	private boolean read;
	private Set<Tag> tags;
	private Map<String, String> metadata;
	private static Map<String, Entry.Type> types = new HashMap<>();

	public static void registerType(Type type) {
		types.put(type.getName(), type);
	}

	public interface Type {
		public String getName();

		public void read(Entry e) throws EOFException;

		public String formatShort(Entry e);

		public String formatLong(Entry e);
	}

	public Entry(int id) {
		this(id, new HashSet<>(), new HashMap<>());
	}

	public Entry(Set<Tag> tags, Map<String, String> metadata) {
		this(0, tags, metadata);
	}

	public Entry(int id, Set<Tag> tags, Map<String, String> metadata) {
		this.id = id;
		this.read = false;
		this.tags = tags;
		this.metadata = metadata;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public String getTypeName() {
		return this.metadata.get("type");
	}

	public Type getType() {
		return types.get(this.getTypeName());
	}

	public void setTags(List<Tag> tags) {
		this.tags = new HashSet<>(tags);
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public String getMetadata(String key) {
		return this.metadata.get(key);
	}

	public void putMetadata(String key, String value) {
		this.metadata.put(key, value);
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public String getMetadataEntry(String key) {
		return metadata.get(key);
	}

	public String toShortString() {
		return this.getType().formatShort(this);
	}

	public String toLongString() {
		return this.getType().formatLong(this);
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
		return String.format("Entry{id=%d, read=%b, tags=%s, metadata=%s}", id, read, tags, metadata);
	}

	@Override
	public int hashCode() {
		return id;
	}
}
