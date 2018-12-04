package bookmarks.dao;

import bookmarks.domain.Entry;
import bookmarks.domain.Tag;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntryTagDaoTest {
	EntryTagDao entryTagDao;
	TagDao tagDao;
	EntryDao entryDao;

	@Before
	public void setUp() throws Exception {
		Database db = new Database("jdbc:sqlite::memory:");
		db.createNewTables();
		tagDao = new TagDao(db);
		entryTagDao = new EntryTagDao(db, tagDao);
		entryDao = new EntryDao(db, entryTagDao, new EntryMetadataDao(db));

		addEntryToDatabase(1);
		addTagToDatabase("tag1");
		addTagToDatabase("tag2");
	}

	@Test
	public void findWithNonExistingId() throws SQLException {
		List<Tag> list = entryTagDao.find(new Entry(3));
		assertEquals(list.size(), 0);
	}

	@Test
	public void insertFindAndSaveTest() throws SQLException {
		Entry e = createEntryWithTags();
		entryTagDao.insert(e);

		List<Tag> tagList = entryTagDao.find(e);

		assertEquals(tagList.get(0), new Tag(1, "tag", "tag1"));
		assertEquals(tagList.get(1), new Tag(2, "tag", "tag2"));

		e.getTags().add(new Tag(3, "tag", "tag3"));
		entryTagDao.save(e);

		tagList = entryTagDao.find(e);

		assertEquals(tagList.get(0), new Tag(1, "tag", "tag1"));
		assertEquals(tagList.get(1), new Tag(2, "tag", "tag2"));
		assertEquals(tagList.get(2), new Tag(3, "tag", "tag3"));
	}

	@Test
	public void deleteTest() throws SQLException {
		Entry e = createEntryWithTags();
		entryTagDao.insert(e);

		List<Tag> tagList = entryTagDao.find(e);
		assertEquals(tagList.get(0), new Tag(1, "tag", "tag1"));
		assertEquals(tagList.get(1), new Tag(2, "tag", "tag2"));

		entryTagDao.delete(e);

		tagList = entryTagDao.find(e);
		assertEquals(tagList.size(), 0);
	}

	@Test
	public void deleteWithNonExistingId() throws SQLException {
		Entry e = createEntryWithTags();
		entryTagDao.insert(e);

		List<Tag> tagList = entryTagDao.find(e);
		assertEquals(tagList.get(0), new Tag(1, "tag", "tag1"));
		assertEquals(tagList.get(1), new Tag(2, "tag", "tag2"));

		entryTagDao.delete(new Entry(2));

		tagList = entryTagDao.find(e);
		assertEquals(tagList.get(0), new Tag(1, "tag", "tag1"));
		assertEquals(tagList.get(1), new Tag(2, "tag", "tag2"));
	}

	private Entry createEntryWithTags() {
		Entry e = new Entry(1);
		Set<Tag> tags = new HashSet<>();
		tags.add(new Tag(1, "tag", "tag1"));
		tags.add(new Tag(2, "tag", "tag2"));
		e.setTags(tags);

		return e;
	}

	private void addTagToDatabase(String tag) throws SQLException {
		tagDao.insert(new Tag("tag", tag));
	}

	private void addEntryToDatabase(int id) throws SQLException {
		entryDao.insert(new Entry(id));
	}
}
