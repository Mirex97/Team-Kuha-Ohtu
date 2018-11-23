package bookmarks.dao;

import bookmarks.domain.Entry;
import bookmarks.domain.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntryDao extends AbstractDao<Entry, Integer> {
	protected EntryTagDao entryTagDao;
	protected EntryMetadataDao entryMetadataDao;

	public EntryDao(Database database, EntryTagDao entryTagDao, EntryMetadataDao entryMetadataDao) {
		super(database);
		this.entryTagDao = entryTagDao;
		this.entryMetadataDao = entryMetadataDao;
	}

	protected Entry readOne(ResultSet rs) throws SQLException {
		Entry e = new Entry(rs.getInt("id"));
		e.setTags(entryTagDao.find(e));
		e.setMetadata(entryMetadataDao.find(e));
		return e;
	}

	@Override
	protected PreparedStatement getFindOneQuery(Connection conn, Integer id) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM entry WHERE id = ?");
		stmt.setInt(1, id);
		return stmt;
	}

	@Override
	protected PreparedStatement getFindAllQuery(Connection conn) throws SQLException {
		return conn.prepareStatement("SELECT * FROM entry");
	}

	@Override
	protected PreparedStatement getSearchQuery(Connection conn, String query) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT entry_id AS id FROM entry_metadata WHERE value LIKE ?");
		stmt.setString(1, "%" + query + "%");
		return stmt;
	}

	@Override
	protected PreparedStatement getInsertQuery(Connection conn, Entry object) throws SQLException {
		return conn.prepareStatement("INSERT INTO entry (id) VALUES (NULL)");
	}

	protected Entry insert(Entry object) throws SQLException {
		object = super.insert(object);
		entryTagDao.save(object);
		entryMetadataDao.save(object);
		return object;
	}

	@Override
	protected PreparedStatement getUpdateQuery(Connection conn, Entry object) {
		// update() is overridden so this is not used.
		return null;
	}

	@Override
	protected Entry update(Entry object) throws SQLException {
		entryTagDao.save(object);
		entryMetadataDao.save(object);
		return object;
	}

	@Override
	protected PreparedStatement getExistenceCheckQuery(Connection conn, Entry object) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT id FROM entry WHERE id = ?");
		stmt.setInt(1, object.getID());
		return stmt;
	}

	@Override
	protected PreparedStatement getDeleteQuery(Connection conn, Integer id) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("DELETE FROM entry WHERE id = ?");
		stmt.setInt(1, id);
		return stmt;
	}
}
