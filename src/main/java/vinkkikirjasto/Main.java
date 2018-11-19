package vinkkikirjasto;

import java.util.List;
import vinkkikirjasto.dao.EntryDao;
import vinkkikirjasto.dao.Database;
import vinkkikirjasto.luokat.Entry;

public class Main {

    public static Database base;

    public static void main(String[] args) {
        base = new Database("jdbc:sqlite:vinkit.db");
        base.getConnection();
        base.createNewTables();
        System.out.println("Hello Kuha!");

        testi();
    }

    public static void testi() {
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
