package bookmarks.dao;

import bookmarks.domain.Entry;
import bookmarks.domain.Tag;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class EntryDaoTest {
	EntryDao entryDao;
	Random r;

	@Before
	public void setUp() throws Exception {
		r = new Random(1337);
		Database db = new Database("jdbc:sqlite::memory:");
		db.createNewTables();
		entryDao = new EntryDao(db, new EntryTagDao(db, new TagDao(db)), new EntryMetadataDao(db));
	}

	@Test
	public void findOneNull() throws SQLException {
		assertNull(entryDao.findOne(123));
	}

	private Entry create() throws SQLException {
		Set<Tag> tags = new HashSet<>();
		if (r.nextBoolean()) {
			tags.add(new Tag("tag", Integer.toString(r.nextInt(100))));
		}
		Map<String, String> metadata = new HashMap<>();
		metadata.put("type", Entry.getTypes().stream()
			.skip(r.nextInt(Entry.getTypes().size() - 1))
			.findFirst().orElse("book"));
		Entry e = new Entry(r.nextInt(), tags, metadata);
		entryDao.save(e);
		return e;
	}

	private Entry createWithTag(String... tags) throws SQLException {
		Entry e = create();
		for (String tag : tags) {
			e.getTags().add(new Tag("tag", tag));
		}
		entryDao.save(e);
		return e;
	}

	private Entry createWithMeta(String title, String author) throws SQLException {
		Entry e = create();
		e.getMetadata().put("Title", title);
		e.getMetadata().put("Author", author);
		entryDao.save(e);
		return e;
	}

	@Test
	public void findOne() throws SQLException {
		Entry e1 = create();
		Entry e2 = create();
		create();
		create();

		Entry e1Find = entryDao.findOne(e1.getID());

		assertNotSame(e1, e1Find);
		assertEquals(e1, e1Find);
		assertNotEquals(e1, entryDao.findOne(e2.getID()));
	}

	@Test
	public void findAllEmpty() throws SQLException {
		assertTrue(entryDao.findAll().isEmpty());
	}

	@Test
	public void findAll() throws SQLException {
		Entry e1 = create();
		Entry e2 = create();
		Entry e3 = create();

		List<Entry> all = entryDao.findAll();

		assertEquals(3, all.size());
		assertTrue(all.contains(e1));
		assertTrue(all.contains(e2));
		assertTrue(all.contains(e3));
	}

	@Test
	public void search() throws SQLException {
		createWithMeta("The Subtle Knife", "Philip Pullman");
		Entry e1 = createWithMeta("Stargate Atlantis: Allegiance", "Melissa Scott & Amy Griswold");
		Entry e2 = createWithMeta("Stargate Atlantis: The Lost", "Jo Graham & Amy Griswold");
		createWithMeta("Stargate SG-1: Sacrifice Moon", "Julie Fortune");

		List<Entry> results = entryDao.search("atlantis");

		assertEquals(2, results.size());
		assertTrue(results.contains(e1));
		assertTrue(results.contains(e2));
	}

	@Test
	public void delete() throws SQLException {
		Entry e1 = create();
		Entry e2 = create();
		create();
		create();
		assertEquals(e1, entryDao.findOne(e1.getID()));
		assertEquals(e2, entryDao.findOne(e2.getID()));

		entryDao.delete(e1.getID());

		assertNull(entryDao.findOne(e1.getID()));
		assertEquals(e2, entryDao.findOne(e2.getID()));
	}

	@Test
	public void update() throws SQLException {
		Entry e1 = create();
		Entry e2 = create();
		Entry e1Copy = entryDao.findOne(e1.getID());
		assertNotSame(e1, e1Copy);
		assertEquals(e1, e1Copy);
		assertEquals(e2, entryDao.findOne(e2.getID()));

		e1Copy.getMetadata().put("foo", "bar");
		entryDao.save(e1Copy);

		Entry e1CopyCopy = entryDao.findOne(e1.getID());
		assertNotEquals(e1, e1CopyCopy);
		assertNotSame(e1Copy, e1CopyCopy);
		assertEquals(e1Copy, e1CopyCopy);
	}

	@Test
	public void findWithTag() throws SQLException {
		Entry e1 = createWithTag("foo");
		createWithTag("bar");
		Entry e2 = createWithTag("bar", "foobar");
		createWithTag("fobar", "bar");

		List<Entry> results = entryDao.findWithTag("foo");

		assertEquals(2, results.size());
		assertTrue(results.contains(e1));
		assertTrue(results.contains(e2));
	}
}
