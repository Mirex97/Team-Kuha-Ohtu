package bookmarks.dao;

import java.sql.*;

public class Database {
	private String databaseAddress;

	public Database(String databaseAddress) {
		this.databaseAddress = databaseAddress;
	}

	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(databaseAddress);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return conn;
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

		try (Connection conn = DriverManager.getConnection(this.databaseAddress)) {
			Statement prof = conn.createStatement();
			prof.execute(entry);
			prof.execute(entryMetadata);
			prof.execute(tag);
			prof.execute(entryTag);
		} catch (SQLException e) {

		}
	}
}
