package bookmarks.dao;

import bookmarks.domain.Entry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EntryMetadataDao {
	protected Database db;

	public EntryMetadataDao(Database db) {
		this.db = db;
	}

	public Map<String, String> find(Entry e) throws SQLException {
		PreparedStatement stmt = db.conn.prepareStatement(
			"SELECT key, value FROM entry_metadata WHERE entry_id = ?");
		stmt.setInt(1, e.getID());
		ResultSet rs = stmt.executeQuery();
		Map<String, String> result = new HashMap<>();
		while (rs.next()) {
			result.put(rs.getString("key"), rs.getString("value"));
		}
		stmt.close();
		return result;
	}

	public void delete(Entry e) throws SQLException {
		PreparedStatement stmt = db.conn.prepareStatement("DELETE FROM entry_metadata WHERE entry_id=?");
		stmt.setInt(1, e.getID());
		stmt.execute();
		stmt.close();
	}

	protected void insert(Entry e) throws SQLException {
		PreparedStatement stmt = db.conn.prepareStatement("INSERT INTO entry_metadata (entry_id, key, value) VALUES (?, ?, ?)");
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
		delete(e);
		insert(e);
	}
}
