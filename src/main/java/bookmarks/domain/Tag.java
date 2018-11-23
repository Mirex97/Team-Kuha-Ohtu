package bookmarks.domain;

public class Tag implements IDObject {
	private int id;
	private String type, name;

	public Tag(String type, String name) {
		this(0, type, name);
	}

	public Tag(int id, String type, String name) {
		this.id = id;
		this.type = type;
		this.name = name;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tag tag = (Tag) o;
		return id == tag.id &&
			type.equals(tag.type) &&
			name.equals(tag.name);
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return String.format("%s #%d:" +
			" %s", type, id, name);
	}
}
