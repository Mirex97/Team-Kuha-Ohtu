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
		assertTrue("Didn't find added tag!", tagDao.findOne(tag.getID()) != null);
	}

	@Test
	public void updateTest() throws SQLException {
		Tag tag = tagDao.update(new Tag(1, "tag", "futuristic"));
		assertTrue("Wasn't modified", tagDao.findOne(1).getName().equals("futuristic"));
	}

	@Test
	public void deleteTest() throws SQLException {
		tagDao.delete(1);
		assertTrue("Found one although deleted?", tagDao.findOne(1) == null);
	}

	@Test
	public void searchTests() throws SQLException {
		List<Tag> tags = tagDao.search("a-");
		assertTrue("Didn't find tag although should exist!", tags.get(0).getName().equals("a-1") && tags.size() == 3);
		tags = tagDao.search("NOSUCHTAG");
		assertTrue("Found tags although shouldn't!", tags.isEmpty());
	}

	@Test
	public void findOneTest() throws SQLException {
		Tag tag = tagDao.findOne(1);
		assertTrue("Id was not the same!", tag.equals(new Tag(1, "tag", "scifi")));

	}

	@Test
	public void findDoesntFindOneTest() throws SQLException {
		Tag tag = tagDao.findOne(23);
		assertTrue("Tag existed although impossible!", tag == null);
	}

	@Test
	public void findAllTest() throws SQLException {
		List<Tag> tags = tagDao.findAll();
		int i = 1;
		for (Tag tag : tags) {
			assertTrue("Wasn't the same id!", tag.equals(new Tag(i, tag.getType(), tag.getName())));
			i++;
		}
	}

}
