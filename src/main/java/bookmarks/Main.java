package bookmarks;

import java.util.List;

import bookmarks.dao.EntryDao;
import bookmarks.dao.Database;
import bookmarks.domain.Entry;

public class Main {
	public static Database base;

	public static void main(String[] args) {
		base = new Database("jdbc:sqlite:vinkit.db");
		base.getConnection();
		base.createNewTables();

		tempTestDatabase();
	}

	public static void tempTestDatabase() {
		boolean errored = false;
		EntryDao entryDao = new EntryDao(base);
		try {
			entryDao.saveOrUpdate(new Entry(0, "Ei Otsikkoa", "book", "Minä", "12345", "Ei kommenttia", null, null));
			entryDao.delete(4);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DID NOT SAVE!!!");
		}

		List<Entry> entries = null;
		Entry entry = null;
		try {
			entries = entryDao.findAll();
			entry = entryDao.findOne(1);
		} catch (Exception e) {
			errored = true;
		}
		if (!errored) {
			for (Entry e : entries) {
				System.out.println(e);
			}
		}
	}
}
