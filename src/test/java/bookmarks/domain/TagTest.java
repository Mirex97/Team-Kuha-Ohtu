
package bookmarks.domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
	Tag tag;
	
	
	
	@Before
	public void setUp() {
		tag = new Tag(1, "tag", "meh");
	}
	
	@After
	public void tearDown() {
	}
	
	@Test
	public void setters() {
		tag.setID(1);
		assertTrue(tag.getID() == 1);
		tag.setID(2);
		assertTrue(tag.getID() == 2);
		tag.setName("lol");
		assertTrue(tag.getName().equals("lol"));
	}
	
	@Test
	public void equalsTest() {
		Tag compare = new Tag(1, "tag", "meh");
		assertTrue(tag.equals((Object) tag));
		assertTrue(!tag.equals(null));
		assertTrue(!tag.equals(new Object()));
		assertTrue(tag.equals(compare));
		compare.setID(2);
		assertTrue(!tag.equals(compare));
		compare.setID(1);
		compare.setType("wat");
		assertTrue(!tag.equals(compare));
	}

}
