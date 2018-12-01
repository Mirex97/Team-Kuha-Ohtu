package bookmarks.dao;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class DatabaseTest {
	@Test
	public void createNewTablesReturnsTrueIfCreated() throws SQLException {
		Database db = new Database("jdbc:sqlite::memory:");
		boolean created = db.createNewTables();
		assertTrue(created);
		created = db.createNewTables();
		assertFalse(created);
	}
}
