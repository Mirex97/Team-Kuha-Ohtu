package bookmarks.dao;

import bookmarks.domain.IDObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T extends IDObject, K> implements Dao<T, K> {
	protected Database db;

	public AbstractDao(Database db) {
		this.db = db;
	}

	abstract protected T readOne(ResultSet rs) throws SQLException;

	abstract protected PreparedStatement getFindOneQuery(K id) throws SQLException;

	abstract protected PreparedStatement getFindAllQuery() throws SQLException;

	abstract protected PreparedStatement getSearchQuery(String query) throws SQLException;

	abstract protected PreparedStatement getInsertQuery(T object) throws SQLException;

	abstract protected PreparedStatement getUpdateQuery(T object) throws SQLException;

	abstract protected PreparedStatement getExistenceCheckQuery(T object) throws SQLException;

	abstract protected PreparedStatement getDeleteQuery(K id) throws SQLException;
	

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
		PreparedStatement stmt = getFindOneQuery(id);
		ResultSet rs = stmt.executeQuery();
		boolean hasOne = rs.next();
		if (!hasOne) {
			return null;
		}
		T item = readOne(rs);
		rs.close();
		stmt.close();
		return item;
	}

	@Override
	public List<T> findAll() throws SQLException {
		PreparedStatement stmt = getFindAllQuery();
		ResultSet rs = stmt.executeQuery();
		List<T> items = read(rs);
		stmt.close();
		return items;
	}

	public List<T> search(String query) throws SQLException {
		PreparedStatement stmt = getSearchQuery(query);
		ResultSet rs = stmt.executeQuery();
		List<T> items = read(rs);
		stmt.close();
		return items;
	}
	

	protected T insert(T object) throws SQLException {
		PreparedStatement insert = getInsertQuery(object);
		insert.executeUpdate();
		insert.close();

		PreparedStatement getInsertedID = db.conn.prepareStatement("SELECT last_insert_rowid() as id");
		ResultSet rs = getInsertedID.executeQuery();
		rs.next();
		int insertedID = rs.getInt("id");
		object.setID(insertedID);
		rs.close();
		getInsertedID.close();
		return object;
	}

	protected T update(T object) throws SQLException {
		PreparedStatement update = getUpdateQuery(object);
		update.executeUpdate();
		update.close();
		return object;
	}

	@Override
	public T save(T object) throws SQLException {
		PreparedStatement checkExistence = getExistenceCheckQuery(object);
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
		PreparedStatement delete = getDeleteQuery(id);
		delete.execute();
		delete.close();
	}
}
