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
	protected PreparedStatement getFindOneQuery(Integer id) throws SQLException {
		PreparedStatement stmt = db.conn.prepareStatement("SELECT * FROM entry WHERE id = ?");
		stmt.setInt(1, id);
		return stmt;
	}

	@Override
	protected PreparedStatement getFindAllQuery() throws SQLException {
		return db.conn.prepareStatement("SELECT * FROM entry");
	}

	@Override
	protected PreparedStatement getSearchQuery(String query) throws SQLException {
		PreparedStatement stmt = db.conn.prepareStatement("SELECT DISTINCT entry_id AS id FROM entry_metadata WHERE value LIKE ?");
		stmt.setString(1, "%" + query + "%");
		return stmt;
	}

	@Override
	protected PreparedStatement getInsertQuery(Entry object) throws SQLException {
		return db.conn.prepareStatement("INSERT INTO entry (id) VALUES (NULL)");
	}

	protected Entry insert(Entry object) throws SQLException {
		object = super.insert(object);
		entryTagDao.save(object);
		entryMetadataDao.save(object);
		return object;
	}

	@Override
	protected PreparedStatement getUpdateQuery(Entry object) {
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
	protected PreparedStatement getExistenceCheckQuery(Entry object) throws SQLException {
		PreparedStatement stmt = db.conn.prepareStatement("SELECT id FROM entry WHERE id = ?");
		stmt.setInt(1, object.getID());
		return stmt;
	}

	@Override
	protected PreparedStatement getDeleteQuery(Integer id) throws SQLException {
		PreparedStatement stmt = db.conn.prepareStatement("DELETE FROM entry WHERE id = ?");
		stmt.setInt(1, id);
		return stmt;
	}

	public List<Entry> findWithTag(String query) throws SQLException {
		PreparedStatement stmt = getFindWithTagQuery(query);
		ResultSet rs = stmt.executeQuery();
		List<Entry> items = read(rs);
		stmt.close();
		return items;
	}

	protected PreparedStatement getFindWithTagQuery(String query) throws SQLException {
		PreparedStatement stmt = db.conn.prepareStatement(
			"SELECT DISTINCT entry.id FROM entry " +
			"LEFT JOIN entry_tag ON entry_tag.entry_id = entry.id " +
			"LEFT JOIN tag ON tag.id = entry_tag.tag_id " +
			"WHERE tag.name LIKE ?");
		stmt.setString(1, "%" + query + "%");
		return stmt;
	}

}
