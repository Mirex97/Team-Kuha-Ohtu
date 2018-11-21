package bookmarks.dao;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {
	T findOne(K key) throws SQLException;

	List<T> findAll() throws SQLException;

	T save(T object) throws SQLException;

	void delete(K key) throws SQLException;
}
