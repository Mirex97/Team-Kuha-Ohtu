package bookmarks.dao;

import bookmarks.domain.IDObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T extends IDObject, K> implements Dao<T, K> {
	protected Database database;

	public AbstractDao(Database database) {
		this.database = database;
	}

	abstract protected T readOne(ResultSet rs) throws SQLException;

	abstract protected PreparedStatement getFindOneQuery(Connection conn, K id) throws SQLException;

	abstract protected PreparedStatement getFindAllQuery(Connection conn) throws SQLException;

	abstract protected PreparedStatement getSearchQuery(Connection conn, String query) throws SQLException;

	abstract protected PreparedStatement getInsertQuery(Connection conn, T object) throws SQLException;

	abstract protected PreparedStatement getUpdateQuery(Connection conn, T object) throws SQLException;

	abstract protected PreparedStatement getExistenceCheckQuery(Connection conn, T object) throws SQLException;

	abstract protected PreparedStatement getDeleteQuery(Connection conn, K id) throws SQLException;

	protected List<T> read(ResultSet rs) throws SQLException {
		List<T> items = new ArrayList<>();
		while (rs.next()) {
			items.add(readOne(rs));
		}
		rs.close();
		return items;
	}

	@Override
	public T findOne(K id) throws SQLException {
		Connection conn = database.getConnection();
		PreparedStatement stmt = getFindOneQuery(conn, id);
		ResultSet rs = stmt.executeQuery();
		boolean hasOne = rs.next();
		if (!hasOne) {
			return null;
		}
		T item = readOne(rs);
		rs.close();
		stmt.close();
		conn.close();
		return item;
	}

	@Override
	public List<T> findAll() throws SQLException {
		Connection conn = database.getConnection();
		PreparedStatement stmt = getFindAllQuery(conn);
		ResultSet rs = stmt.executeQuery();
		List<T> items = read(rs);
		stmt.close();
		conn.close();
		return items;
	}

	public List<T> search(String query) throws SQLException {
		Connection conn = database.getConnection();
		PreparedStatement stmt = getSearchQuery(conn, query);
		ResultSet rs = stmt.executeQuery();
		List<T> items = read(rs);
		stmt.close();
		conn.close();
		return items;
	}

	protected T insert(T object) throws SQLException {
		Connection conn = database.getConnection();
		PreparedStatement insert = getInsertQuery(conn, object);
		insert.executeUpdate();
		insert.close();

		PreparedStatement getInsertedID = conn.prepareStatement("SELECT last_insert_rowid() as id");
		ResultSet rs = getInsertedID.executeQuery();
		rs.next();
		int insertedID = rs.getInt("id");
		object.setID(insertedID);
		rs.close();
		getInsertedID.close();
		conn.close();
		return object;
	}

	protected T update(T object) throws SQLException {
		Connection conn = database.getConnection();
		PreparedStatement update = getUpdateQuery(conn, object);
		update.executeUpdate();
		update.close();
		conn.close();
		return object;
	}

	@Override
	public T save(T object) throws SQLException {
		Connection conn = database.getConnection();
		PreparedStatement checkExistence = getExistenceCheckQuery(conn, object);
		ResultSet rs = checkExistence.executeQuery();
		boolean hasOne = rs.next();
		checkExistence.close();
		rs.close();
		if (hasOne) {
			return update(object);
		} else {
			return insert(object);
		}
	}

	@Override
	public void delete(K id) throws SQLException {
		Connection conn = database.getConnection();
		PreparedStatement delete = getDeleteQuery(conn, id);
		delete.execute();
		delete.close();
		conn.close();
	}
}
