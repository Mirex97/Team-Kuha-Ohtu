package vinkkikirjasto.dao;

import java.sql.*;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(databaseAddress);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return conn;
    }

    public void createNewTables() {
        String base = "CREATE TABLE Base (\n"
                + "id Integer PRIMARY KEY,\n"
                + "otsikko varchar(25) NOT NULL,\n"
                + "tyyppi varchar(25) NOT NULL,\n"
                + "kommentti varchar(25), \n"
                + "isbn varchar(25), \n"
                + "kirjoittaja varchar(25), \n"
                + "url varchar(25),\n"
                + "author varchar(25),\n"
                + "nimi varchar(25),\n"
                + "kuvaus varchar(100)\n"
                + ");";

        String tag = "CREATE TABLE Tag (\n"
                + "id Integer PRIMARY KEY,\n"
                + "tag varchar(25)\n"
                + ");";

        String base_tag = "CREATE TABLE base_tag (\n"
                + "id Integer PRIMARY KEY,\n"
                + "base_id Integer,\n"
                + "tag_id Integer,\n"
                + "FOREIGN KEY (base_id) REFERENCES Base(id),\n"
                + "FOREIGN KEY (tag_id) REFERENCES Tag(id)\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(this.databaseAddress)) {
            Statement prof = conn.createStatement();
            prof.execute(base);
            prof.execute(tag);
            prof.execute(base_tag);
        } catch (SQLException e) {

        }
    }
}
