package bookmarks.dao;

import bookmarks.domain.Entry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		e.setRead(rs.getBoolean("read"));
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

	public List<Entry> findAllUnread() throws SQLException {
		PreparedStatement stmt = getFindAllUnreadQuery();
		ResultSet rs = stmt.executeQuery();
		List<Entry> items = read(rs);
		stmt.close();
		return items;
	}

	protected PreparedStatement getFindAllUnreadQuery() throws SQLException {
		return db.conn.prepareStatement("SELECT id, read FROM entry WHERE read=0");
	}

	@Override
	protected PreparedStatement getFindAllQuery() throws SQLException {
		return db.conn.prepareStatement("SELECT * FROM entry");
	}

	@Override
	protected PreparedStatement getSearchQuery(String query) throws SQLException {
		PreparedStatement stmt = db.conn.prepareStatement("SELECT DISTINCT entry_id AS id, read FROM entry, entry_metadata WHERE value LIKE ? AND entry_id=entry.id");
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
	protected PreparedStatement getUpdateQuery(Entry object) throws SQLException {
		PreparedStatement stmt = db.conn.prepareStatement("UPDATE entry SET read=? WHERE id IS ?");
		stmt.setBoolean(1, object.isRead());
		stmt.setInt(2, object.getID());
		return stmt;
	}

	@Override
	protected Entry update(Entry object) throws SQLException {
		super.update(object);
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
			"SELECT DISTINCT entry.id, read FROM entry "
				+ "LEFT JOIN entry_tag ON entry_tag.entry_id = entry.id "
				+ "LEFT JOIN tag ON tag.id = entry_tag.tag_id "
				+ "WHERE tag.name LIKE ?");
		stmt.setString(1, "%" + query + "%");
		return stmt;
	}
}
