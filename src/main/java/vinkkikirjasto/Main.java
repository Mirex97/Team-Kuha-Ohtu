package vinkkikirjasto;

import vinkkikirjasto.luokat.Base;
import java.util.List;
import vinkkikirjasto.dao.BaseDao;
import vinkkikirjasto.dao.Database;

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
        BaseDao baseDao = new BaseDao(base);
        try {
//            baseDao.saveOrUpdate(new Base(0, "Ei Otsikkoa", "Kirja", "Ei Kommenttia", "12345", "Minä", null));
//            baseDao.delete(4);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DID NOT SAVE!!!");
        }
        

        List<Base> bases = null;
        Base basee = null;
        try {
            bases = baseDao.findAll();
            basee = baseDao.findOne(1);

        } catch (Exception e) {
            errored = true;
        }
        if (!errored) {
            for (Base base : bases) {
                System.out.println(base);
            }
        }
    }

}
