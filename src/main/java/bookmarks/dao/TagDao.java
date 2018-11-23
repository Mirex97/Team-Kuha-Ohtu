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

	@Override
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
	protected PreparedStatement getSearchQuery(Connection conn, String query) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT id FROM tag WHERE `name` LIKE %?%");
		stmt.setString(1, query);
		return stmt;
	}


	@Override
	protected PreparedStatement getInsertQuery(Connection conn, Tag object) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO tag (`type`, `name`) VALUES (?, ?)");
		stmt.setString(1, object.getType());
		stmt.setString(2, object.getName());
		return stmt;
	}

	@Override
	protected PreparedStatement getUpdateQuery(Connection conn, Tag object) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("UPDATE tag SET `type`=?, `name`=? WHERE id=?");
		stmt.setString(1, object.getType());
		stmt.setString(2, object.getName());
		stmt.setInt(3, object.getID());
		return stmt;
	}

	@Override
	protected PreparedStatement getExistenceCheckQuery(Connection conn, Tag object) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tag WHERE `name` = ?");
		stmt.setString(1, object.getName());
		return stmt;
	}

	@Override
	protected PreparedStatement getDeleteQuery(Connection conn, Integer id) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("DELETE FROM tag WHERE id = ?");
		stmt.setInt(1, id);
		return stmt;
	}


	@Override
	public Tag save(Tag object) throws SQLException {
		Connection conn = database.getConnection();
		PreparedStatement checkExistence = getExistenceCheckQuery(conn, object);
		ResultSet rs = checkExistence.executeQuery();
		boolean hasOne = rs.next();

		if (hasOne) {
			object.setID(rs.getInt("id"));
			checkExistence.close();
			rs.close();
			return update(object);
		} else {
			checkExistence.close();
			rs.close();
			return insert(object);
		}
	}
}
