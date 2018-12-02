package bookmarks.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {
	Tag tag;

	@Before
	public void setUp() {
		tag = new Tag(1, "tag", "meh");
	}

	@Test
	public void setters() {
		tag.setID(1);
		assertEquals(1, tag.getID());
		tag.setID(2);
		assertEquals(2, tag.getID());
		tag.setName("lol");
		assertEquals("lol", tag.getName());
	}

	@Test
	public void equalsTest() {
		Tag compare = new Tag(1, "tag", "meh");
		assertEquals(tag, (Object) tag);
		assertFalse(tag.equals(null));
		assertNotEquals(tag, new Object());
		assertEquals(tag, compare);
		compare.setID(2);
		assertNotEquals(tag, compare);
		compare.setID(1);
		compare.setType("wat");
		assertNotEquals(tag, compare);
	}
}
