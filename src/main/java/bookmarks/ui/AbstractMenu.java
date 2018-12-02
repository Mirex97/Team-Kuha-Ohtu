
package bookmarks.ui;

import bookmarks.dao.EntryDao;
import bookmarks.domain.Entry;
import bookmarks.io.IO;
import java.util.List;

public abstract class AbstractMenu {
	protected IO io;
	protected EntryDao entryDao;
	
	
	public void search() {
		String query = io.readLine("Term to search: ");
		try {
			List<Entry> entries = entryDao.search(query);
			if (entries.isEmpty()) {
				io.print("No matches :(");
			} else {
				io.printf("%d match%s", entries.size(), entries.size() > 1 ? "es" : "");
				for (Entry entry : entries) {
					io.print(entry.toShortString());
				}
			}
		} catch (Exception e) {
			io.print("Failed to search :(");
			e.printStackTrace();
		}
	}
}
