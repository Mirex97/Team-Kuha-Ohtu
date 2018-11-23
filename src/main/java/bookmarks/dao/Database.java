package bookmarks.dao;

import java.sql.*;

public class Database {
	private String databaseAddress;
	protected Connection conn;

	public Database(String databaseAddress) throws SQLException {
		this.databaseAddress = databaseAddress;
		conn = DriverManager.getConnection(databaseAddress);
	}

	public void createNewTables() {
		String entry = "CREATE TABLE entry ("
			+ "id INTEGER PRIMARY KEY"
			+ ");";

		String entryMetadata = "CREATE TABLE entry_metadata ("
			+ "entry_id INTEGER,"
			+ "key VARCHAR(255),"
			+ "value TEXT,"
			+ "PRIMARY KEY (entry_id, key),"
			+ "FOREIGN KEY (entry_id) REFERENCES entry(id) ON DELETE CASCADE"
			+ ");";

		String tag = "CREATE TABLE tag ("
			+ "id INTEGER PRIMARY KEY,"
			+ "`type` VARCHAR(255),"
			+ "name VARCHAR(255)"
			+ ");";

		String entryTag = "CREATE TABLE entry_tag ("
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
