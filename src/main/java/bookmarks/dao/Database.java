package bookmarks.dao;

import java.sql.*;

public class Database {
	protected Connection conn;

	public Database(String databaseAddress) throws SQLException {
		conn = DriverManager.getConnection(databaseAddress);
		createNewTables();
	}


	public void createNewTables() {
		String entry = "CREATE TABLE IF NOT EXISTS entry ("
			+ "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT"
			+ ");";

		String entryMetadata = "CREATE TABLE IF NOT EXISTS entry_metadata ("
			+ "entry_id INTEGER NOT NULL,"
			+ "key VARCHAR(255) NOT NULL,"
			+ "value TEXT,"
			+ "PRIMARY KEY (entry_id, key),"
			+ "FOREIGN KEY (entry_id) REFERENCES entry(id) ON DELETE CASCADE"
			+ ");";

		String tag = "CREATE TABLE IF NOT EXISTS tag ("
			+ "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
			+ "`type` VARCHAR(255),"
			+ "name VARCHAR(255)"
			+ ");";

		String entryTag = "CREATE TABLE IF NOT EXISTS entry_tag ("
			+ "entry_id INTEGER,"
			+ "tag_id INTEGER,"
			+ "FOREIGN KEY (entry_id) REFERENCES entry(id) ON DELETE CASCADE,"
			+ "FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE CASCADE"
			+ ");";

		try {
			Statement prof = conn.createStatement();
			prof.execute("PRAGMA foreign_keys = ON");
			prof.execute(entry);
			prof.execute(entryMetadata);
			prof.execute(tag);
			prof.execute(entryTag);
		} catch (SQLException e) {}
	}
}
