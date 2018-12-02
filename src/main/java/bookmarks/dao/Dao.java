package bookmarks.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T, K> {
	T findOne(K key) throws SQLException;

	List<T> findAll() throws SQLException;

	T save(T object) throws SQLException;

	void delete(K key) throws SQLException;
}
