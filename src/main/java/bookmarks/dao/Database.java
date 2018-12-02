package bookmarks.dao;

import java.sql.*;

public class Database {
	protected Connection conn;

	public Database(String databaseAddress) throws SQLException {
		conn = DriverManager.getConnection(databaseAddress);
	}

	public boolean createNewTables() {
		String entry = "CREATE TABLE entry ("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "read BOOLEAN NOT NULL DEFAULT FALSE"
			+ ");";

		String entryMetadata = "CREATE TABLE entry_metadata ("
			+ "entry_id INTEGER,"
			+ "key VARCHAR(255),"
			+ "value TEXT NOT NULL,"
			+ "PRIMARY KEY (entry_id, key),"
			+ "FOREIGN KEY (entry_id) REFERENCES entry(id) ON DELETE CASCADE"
			+ ");";

		String tag = "CREATE TABLE tag ("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "`type` VARCHAR(255) NOT NULL,"
			+ "name VARCHAR(255) NOT NULL"
			+ ");";

		String entryTag = "CREATE TABLE entry_tag ("
			+ "entry_id INTEGER,"
			+ "tag_id INTEGER,"
			+ "PRIMARY KEY (entry_id, tag_id),"
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
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
}
