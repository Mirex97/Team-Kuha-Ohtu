
package vinkkikirjasto;

import vinkkikirjasto.dao.Database;


public class Main {
    
    public static Database base;

    public static void main(String[] args) {
        base = new Database("jdbc:sqlite:vinkit.db");
        base.getConnection();
        base.createNewTables();
        System.out.println("Hello Kuha!");
    }

}
