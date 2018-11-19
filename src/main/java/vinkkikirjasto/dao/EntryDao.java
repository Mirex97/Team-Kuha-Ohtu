package vinkkikirjasto.dao;

import vinkkikirjasto.luokat.Entry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntryDao implements Dao<Entry, Integer> {
	private Database database;

	public EntryDao(Database database) {
		this.database = database;
	}

	@Override
	public Entry findOne(Integer key) throws SQLException {
		Connection conn = database.getConnection();
		PreparedStatement stmt = conn.prepareStatement(
				"SELECT * FROM entry WHERE id = ?");
		stmt.setInt(1, key);
		ResultSet rs = stmt.executeQuery();
		boolean hasOne = rs.next();
		if (!hasOne) {
			return null;
		}
		Entry entry = readOne(rs);
		rs.close();
		stmt.close();
		conn.close();
		return entry;
	}

	@Override
	public List<Entry> findAll() throws SQLException {
		Connection connection = database.getConnection();
		PreparedStatement statement = connection.prepareStatement(
				"SELECT * FROM entry");
		ResultSet rs = statement.executeQuery();
		List<Entry> entries = read(rs);
		statement.close();
		connection.close();

		return entries;
	}
        
	public List<Entry> findAllWithType(String key) throws SQLException {
		Connection conn = database.getConnection();
		PreparedStatement stmt = conn.prepareStatement(
				"SELECT * FROM entry WHERE type IS ?");
		stmt.setString(1, key);
		ResultSet rs = stmt.executeQuery();
		boolean hasOne = rs.next();
		if (!hasOne) {
			return null;
		}
		List<Entry> entries = read(rs);
		rs.close();
		stmt.close();
		conn.close();
		return entries;
	}

        

	private Entry readOne(ResultSet rs) throws SQLException {
		return new Entry(
				rs.getInt("id"),
				rs.getString("title"),
				rs.getString("type"),
				rs.getString("author"),
				rs.getString("description"),
				rs.getString("comment"),
				null,
				null);
	}

	private List<Entry> read(ResultSet rs) throws SQLException {
		List<Entry> entries = new ArrayList<>();
		while (rs.next()) {
			entries.add(readOne(rs));
		}
		rs.close();
		return entries;
	}

	private void setParams(PreparedStatement ps, Entry object) throws SQLException {
		ps.setString(1, object.getTitle());
		ps.setString(2, object.getType());
		ps.setString(3, object.getAuthor());
		ps.setString(4, object.getDescription());
		ps.setString(5, object.getComment());
	}

	private Entry insert(Entry object) throws SQLException {
		Connection conn = database.getConnection();
		PreparedStatement saveStatement = conn.prepareStatement(
				"INSERT INTO entry (title, `type`, author, description, comment) "
						+ "VALUES (?, ?, ?, ?, ?)");
		setParams(saveStatement, object);
		saveStatement.executeUpdate();
		saveStatement.close();

		PreparedStatement retrieveStatement = conn.prepareStatement(
				"SELECT last_insert_rowid() as id");
		ResultSet rs = retrieveStatement.executeQuery();
		rs.next();
		int insertedID = rs.getInt("id");
		object.setID(insertedID);
		rs.close();
		retrieveStatement.close();
		conn.close();
		return object;
	}

	private Entry update(Entry object) throws SQLException {
		Connection conn = database.getConnection();
		PreparedStatement updateStatement = conn.prepareStatement(
				"UPDATE entry SET "
						+ "title = ?,"
						+ "`type` = ?,"
						+ "author = ?,"
						+ "description = ?,"
						+ "comment = ? "
						+ "WHERE (id = ?)");
		setParams(updateStatement, object);
		updateStatement.setInt(6, object.getID());
		updateStatement.executeUpdate();
		updateStatement.close();
		conn.close();
		return object;
	}

	@Override
	public Entry saveOrUpdate(Entry object) throws SQLException {
		Connection conn = database.getConnection();
		PreparedStatement testStatement = conn.prepareStatement(
				"SELECT * FROM entry WHERE id = ?");
		testStatement.setInt(1, object.getID());
		ResultSet rs = testStatement.executeQuery();
		boolean hasOne = rs.next();
		testStatement.close();
		rs.close();
		if (hasOne) {
			return update(object);
		} else {
			return insert(object);
		}
	}

	@Override
	public void delete(Integer key) throws SQLException {
		Connection conn = database.getConnection();
		PreparedStatement removeStatement = conn.prepareStatement(
				"DELETE FROM entry WHERE id = ?");
		removeStatement.setInt(1, key);
		removeStatement.execute();
		removeStatement.close();
		conn.close();
	}
}
