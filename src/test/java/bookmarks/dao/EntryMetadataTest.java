/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookmarks.dao;

import bookmarks.domain.Entry;
import bookmarks.domain.Tag;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntryMetadataTest {
	
	EntryDao entryDao;
	EntryMetadataDao metadataDao;
	
	@Before
	public void setUp() throws SQLException {
		Database db = new Database("jdbc:sqlite::memory:");
		db.createNewTables();
		metadataDao = new EntryMetadataDao(db);
		entryDao = new EntryDao(db, new EntryTagDao(db, new TagDao(db)), metadataDao);
		Set<Tag> tags = new HashSet<>();
		Map<String, String> metadata = new HashMap<>();
		metadata.put("Title", "A New Hope");
		Entry e = new Entry(0, tags, metadata);
		e = entryDao.save(e);
		metadataDao.save(e);
	}
	
	@Test
	public void saveTest() throws SQLException {
		Set<Tag> tags = new HashSet<>();
		Map<String, String> metadata = new HashMap<>();
		metadata.put("Title", "Empire Strikes Back");
		Entry e = new Entry(0, tags, metadata);
		e = entryDao.save(e);
		metadataDao.save(e);
		assertTrue("Did not save metadata!", metadataDao.find(new Entry(2)).containsValue("Empire Strikes Back"));	
	}
	
	@Test
	public void deleteTest() throws SQLException {
		metadataDao.delete(new Entry(1));
		assertTrue("Did not delete, although should've?", metadataDao.find(new Entry(1)).isEmpty());
	}
	
	@Test
	public void findTest() throws SQLException {
		assertTrue("Did not find although should exists?", !metadataDao.find(new Entry(1)).isEmpty());
	}
	

}
