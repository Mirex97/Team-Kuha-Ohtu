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

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return String.format("%s #%d: %s", type, id, name);
	}
}
