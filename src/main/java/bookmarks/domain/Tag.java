
package bookmarks.domain;

import java.util.Objects;

public class Tag {
	private int id;
	private String tag;

	public Tag(int id, String tag) {
		this.id = id;
		this.tag = tag;
	}

	public int getId() {
		return id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tag tag1 = (Tag) o;
		return id == tag1.id && tag.equals(tag1.tag);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tag);
	}
}
