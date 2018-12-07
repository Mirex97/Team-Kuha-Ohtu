package bookmarks.domain;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntryTest {
	private Entry entry;

	@Before
	public void setUp() {
		entry = new Entry(1);
	}

	@Test
	public void isReadReturnsCorrectString() {
		assertFalse(entry.isRead());
		entry.setRead(true);
		assertTrue(entry.isRead());
	}

	@Test
	public void longStringTest() {
		entry.putMetadata("type", "book");
		entry.putMetadata("Title", "");
		entry.putMetadata("Author", null);
		entry.putMetadata("ISBN", null);
		entry.putMetadata("Description", "");
		entry.putMetadata("Comment", "ei ole");
		assertTrue(!entry.toLongString().contains("ISBN"));
		assertTrue(!entry.toLongString().contains("Description"));
	}

	@Test
	public void equalsTest() {
		assertEquals(entry, entry);
		Entry compare = new Entry(2);
		Object wat = new Object();
		assertTrue(!entry.equals(wat));
		wat = null;
		assertNotEquals(entry, wat);
		assertNotEquals(entry, compare);
		compare.setID(1);
		assertEquals(entry, compare);
		compare.setID(1);
		Tag tag = new Tag(1, "tag", "wat");
		Set<Tag> meh = compare.getTags();
		meh.add(tag);
		compare.setTags(meh);
		assertNotEquals(entry, compare);
	}

	@Test
	public void toStringTest() {
		assertEquals("Entry{id=1, read=false, tags=[], metadata={}}", entry.toString());
	}

	@Test
	public void hashCodeTest() {
		assertEquals(1, entry.hashCode());
	}
}
