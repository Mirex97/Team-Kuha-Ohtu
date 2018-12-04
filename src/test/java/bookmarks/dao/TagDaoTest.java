package bookmarks.dao;

import bookmarks.domain.Tag;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagDaoTest {

	TagDao tagDao;

	@Before
	public void setUp() throws SQLException {
		Database db = new Database("jdbc:sqlite::memory:");
		db.createNewTables();
		tagDao = new TagDao(db);
		tagDao.insert(new Tag(0, "tag", "scifi"));
		tagDao.insert(new Tag(0, "tag", "fantasy"));
		tagDao.insert(new Tag(0, "tag", "important"));
		tagDao.insert(new Tag(0, "tag", "useless"));
		tagDao.insert(new Tag(0, "tag", "useless2"));
		tagDao.insert(new Tag(0, "tag", "a-1"));
		tagDao.insert(new Tag(0, "tag", "a-2"));
		tagDao.insert(new Tag(0, "tag", "a-3"));
	}

	@Test
	public void insertTest() throws SQLException {
		Tag tag = tagDao.insert(new Tag(0, "tag", "guides"));
		assertNotNull(tagDao.findOne(tag.getID()));
	}

	@Test
	public void updateTest() throws SQLException {
		Tag tag = tagDao.update(new Tag(1, "tag", "futuristic"));
		assertEquals("futuristic", tagDao.findOne(1).getName());
	}

	@Test
	public void deleteTest() throws SQLException {
		tagDao.delete(1);
		assertNull(tagDao.findOne(1));
	}

	@Test
	public void searchTests() throws SQLException {
		List<Tag> tags = tagDao.search("a-");
		assertEquals(3, tags.size());
		assertEquals("a-1", tags.get(0).getName());
		tags = tagDao.search("NOSUCHTAG");
		assertTrue(tags.isEmpty());
	}

	@Test
	public void findOneTest() throws SQLException {
		Tag tag = tagDao.findOne(1);
		assertEquals(tag, new Tag(1, "tag", "scifi"));

	}

	@Test
	public void findDoesntFindOneTest() throws SQLException {
		Tag tag = tagDao.findOne(23);
		assertNull(tag);
	}

	@Test
	public void findAllTest() throws SQLException {
		List<Tag> tags = tagDao.findAll();
		int i = 1;
		for (Tag tag : tags) {
			assertEquals(tag, new Tag(i, tag.getType(), tag.getName()));
			i++;
		}
	}
}
