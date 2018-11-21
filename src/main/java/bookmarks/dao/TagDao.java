package bookmarks.dao;

import bookmarks.domain.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TagDao extends AbstractDao<Tag, Integer> {
	public TagDao(Database database) {
		super(database);
	}

	protected Tag readOne(ResultSet rs) throws SQLException {
		return new Tag(rs.getInt("id"), rs.getString("type"), rs.getString("name"));
	}

	@Override
	protected PreparedStatement getFindOneQuery(Connection conn, Integer id) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tag WHERE id = ?");
		stmt.setInt(1, id);
		return stmt;
	}

	@Override
	protected PreparedStatement getFindAllQuery(Connection conn) throws SQLException {
		return conn.prepareStatement("SELECT * FROM tag");
	}

	@Override
	protected PreparedStatement getInsertQuery(Connection conn, Tag object) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO tag (`type`, name) VALUES (?, ?, ?)");
		stmt.setString(2, object.getType());
		stmt.setString(3, object.getName());
		return stmt;
	}

	@Override
	protected PreparedStatement getUpdateQuery(Connection conn, Tag object) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("UPDATE tag SET `type`=?, name=? WHERE id=?");
		stmt.setString(1, object.getType());
		stmt.setString(2, object.getName());
		stmt.setInt(3, object.getID());
		return stmt;
	}

	@Override
	protected PreparedStatement getExistenceCheckQuery(Connection conn, Tag object) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT id FROM tag WHERE id = ?");
		stmt.setInt(1, object.getID());
		return stmt;
	}

	@Override
	protected PreparedStatement getDeleteQuery(Connection conn, Integer id) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("DELETE FROM tag WHERE id = ?");
		stmt.setInt(1, id);
		return stmt;
	}
}
