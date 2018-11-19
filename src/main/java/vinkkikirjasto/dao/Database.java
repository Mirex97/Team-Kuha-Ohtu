package vinkkikirjasto.dao;

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
			System.exit(-1);
		}
		return conn;
	}

	public void createNewTables() {
		String entry = "CREATE TABLE entry ("
				+ "id INTEGER PRIMARY KEY,"
				+ "title VARCHAR(255) NOT NULL,"
				+ "type VARCHAR(255) NOT NULL,"
				+ "author VARCHAR(255),"
				+ "description TEXT,"
				+ "comment TEXT"
				+ ");";

		String entryMetadata = "CREATE TABLE entry_metadata ("
				+ "entry_id INTEGER,"
				+ "key VARCHAR(255),"
				+ "value TEXT,"
				+ ");";

		String tag = "CREATE TABLE tag ("
				+ "entry_id INTEGER PRIMARY KEY,"
				+ "tag VARCHAR(255),"
				+ "PRIMARY KEY (entry_id, tag),"
				+ "FOREIGN KEY (entry_id) REFERENCES entry(id)"
				+ ");";

		String entryTag = "CREATE TABLE entry_tag ("
				+ "id INTEGER PRIMARY KEY,"
				+ "entry_id INTEGER,"
				+ "tag_id INTEGER,"
				+ "FOREIGN KEY (entry_id) REFERENCES entry(id),"
				+ "FOREIGN KEY (tag_id) REFERENCES tag(id)"
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
