package bookmarks.domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntryTest {

	private Entry entry;

	public EntryTest() {
		entry = new Entry(1);
	}


	@Before
	public void setUp() {

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
	public void equalsTest() {
		Entry compare = new Entry(2);
		assertTrue(!entry.equals(compare));
		compare.setID(1);
		assertTrue(entry.equals(compare));
	}
	
	@Test
	public void toStringTest() {
		System.out.println(entry);
		assertEquals("Wasn't the same!", "Entry{id=1, read=0, tags=[], metadata={}}", entry.toString());
	}
	
	@Test
	public void hashCodeTest() {
		assertTrue(entry.hashCode() == 1);
	}
	
}
