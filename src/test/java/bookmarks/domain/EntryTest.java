package bookmarks.domain;

import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntryTest {

	private Entry entry;



	@Before
	public void setUp() {
		entry = new Entry(1);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void isReadReturnsCorrectString() {
		assertEquals("Wasn't false!", entry.isRead(), "False");
		entry.setRead(1);
		assertEquals("Wasn't true!", entry.isRead(), "True");
	}
	
	@Test
	public void longStringTest() {
		entry.getMetadata().put("type", "book");
		entry.getMetadata().put("Title", "");
		entry.getMetadata().put("Author", null);
		entry.getMetadata().put("ISBN", null);
		entry.getMetadata().put("Description", "");
		entry.getMetadata().put("Comment", "ei ole");
		try {
			System.out.println(entry.toLongString());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		assertTrue(!entry.toLongString().contains("ISBN"));
		assertTrue(!entry.toLongString().contains("Description"));
	}
	
	@Test
	public void equalsTest() {
		assertTrue(entry.equals((Object) entry));
		Entry compare = new Entry(2);
		Object wat = new Object();
		assertTrue(!entry.equals(wat));
		wat = null;
		assertTrue(!entry.equals(wat));
		assertTrue(!entry.equals(compare));
		compare.setID(1);
		assertTrue(entry.equals(compare));
		compare.setID(1);
		Tag tag = new Tag(1, "tag", "wat");
		Set<Tag> meh = compare.getTags();
		meh.add(tag);
		compare.setTags(meh);
		assertTrue(!entry.equals(compare));
	}
	
	@Test
	public void toStringTest() {
		assertEquals("Wasn't the same!", "Entry{id=1, read=0, tags=[], metadata={}}", entry.toString());
	}
	
	@Test
	public void hashCodeTest() {
		assertTrue(entry.hashCode() == 1);
	}
	
}
