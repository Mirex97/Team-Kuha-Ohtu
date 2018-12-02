
package bookmarks.dao;

import bookmarks.domain.Tag;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagDaoTest {
	TagDao tagDao;
	
	
	@Before
	public void setUp() throws SQLException {
		Database db = new Database("jdbc:sqlite::memory:");
		db.createNewTables();
		tagDao = new TagDao(db);
		tagDao.insert(new Tag(0, "tag", "hermanni"));
		tagDao.insert(new Tag(0, "tag", "hermanni123"));
		tagDao.insert(new Tag(0, "tag", "hermanni123123"));
		tagDao.insert(new Tag(0, "tag", "123"));
		tagDao.insert(new Tag(0, "tag", "useless"));
		tagDao.insert(new Tag(0, "tag", "pointless"));
		tagDao.insert(new Tag(0, "tag", "important"));
		tagDao.insert(new Tag(0, "tag", "maybe"));
		
	}
	
	@After
	public void tearDown() {
	}
	
	private void printAll(List<Tag> tags) {
		for (Tag tag: tags) {
			System.out.println(tag.getID()+", "+ tag.getType() +", " + tag.getName());
		}
	}
	
	@Test
	public void searchTest() throws SQLException {
		List<Tag> tags = tagDao.search("hermanni");
		assertTrue(tags.get(0).getName().equals("hermanni") && tags.size() == 3);
		
	}
}
