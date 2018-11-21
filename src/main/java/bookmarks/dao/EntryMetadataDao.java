package bookmarks.dao;

import bookmarks.domain.Entry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EntryMetadataDao {
	protected Database database;

	public EntryMetadataDao(Database database) {
		this.database = database;
	}

	public Map<String, String> find(Entry e) throws SQLException {
		Connection conn = database.getConnection();
		PreparedStatement stmt = conn.prepareStatement(
			"SELECT key, value FROM entry_metadata WHERE entry_id = ?");
		stmt.setInt(1, e.getID());
		ResultSet rs = stmt.executeQuery();
		Map<String, String> result = new HashMap<>();
		while (rs.next()) {
			result.put(rs.getString("key"), rs.getString("value"));
		}
		stmt.close();
		conn.close();
		return result;
	}

	public void delete(Entry e) throws SQLException {
		Connection conn = database.getConnection();
		delete(conn, e);
		conn.close();
	}

	protected void delete(Connection conn, Entry e) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("DELETE FROM entry_metadata WHERE entry_id=?");
		stmt.setInt(1, e.getID());
		stmt.execute();
		stmt.close();
	}

	protected void insert(Connection conn, Entry e) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO entry_metadata (entry_id, key, value) VALUES (?, ?, ?)");
		for (Map.Entry<String, String> meta : e.getMetadata().entrySet()) {
			stmt.setInt(1, e.getID());
			stmt.setString(2, meta.getKey());
			stmt.setString(3, meta.getValue());
			stmt.addBatch();
		}
		stmt.executeBatch();
		stmt.close();
	}

	public void save(Entry e) throws SQLException {
		Connection conn = database.getConnection();
		delete(conn, e);
		insert(conn, e);
		conn.close();
	}
}
