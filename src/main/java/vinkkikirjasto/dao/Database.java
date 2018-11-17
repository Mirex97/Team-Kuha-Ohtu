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

        } catch (Exception e) {
            return null;
        }
        return conn;
    }

    public void createNewTables() {
        String base = "CREATE TABLE Base (\n"
                + "id Integer PRIMARY KEY,\n"
                + "otsikko varchar(25) NOT NULL,\n"
                + "tyyppi varchar(25) NOT NULL,\n"
                + "kommentti varchar(25), \n"
                + "tagit Integer,\n"
                + "FOREIGN KEY (tagit) REFERENCES base_tag(id)\n"
                + ");";

        String tag = "CREATE TABLE Tag (\n"
                + "id Integer PRIMARY KEY,\n"
                + "tag varchar(25),\n"
                + "bases Integer,\n"
                + "FOREIGN KEY (bases) REFERENCES base_tag(id)\n"
                + ");";

        String base_tag = "CREATE TABLE base_tag (\n"
                + "id Integer PRIMARY KEY,\n"
                + "base_id Integer,\n"
                + "tag_id Integer,\n"
                + "FOREIGN KEY (base_id) REFERENCES Base(id),\n"
                + "FOREIGN KEY (tag_id) REFERENCES Tag(id)\n"
                + ");";

        String kirja = "CREATE TABLE Kirja (\n"
                + "id Integer,\n"
                + "otsikko varchar(25) NOT NULL,\n"
                + "tyyppi varchar(25) NOT NULL,\n"
                + "FOREIGN KEY (id) REFERENCES Base(id)\n"
                + ");";

        String video = "CREATE TABLE Video (\n"
                + "id Integer,\n"
                + "url varchar(25) NOT NULL,\n"
                + "FOREIGN KEY (id) REFERENCES Base(id)\n"
                + ");";

        String blogpost = "CREATE TABLE blogpost (\n"
                + "id Integer,\n"
                + "url varchar(25) NOT NULL,\n"
                + "FOREIGN KEY (id) REFERENCES Base(id)\n"
                + ");";

        String podcast = "CREATE TABLE podcast (\n"
                + "id Integer,\n"
                + "author varchar(25) NOT NULL,\n"
                + "nimi varchar(25) NOT NULL,\n"
                + "kuvaus varchar(100) NOT NULL, \n"
                + "FOREIGN KEY (id) REFERENCES Base(id)\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(this.databaseAddress)) {
            Statement prof = conn.createStatement();
            prof.execute(base);
            prof.execute(tag);
            prof.execute(base_tag);
            prof.execute(kirja);
            prof.execute(video);
            prof.execute(blogpost);
            prof.execute(podcast);
        } catch (SQLException e) {
            System.out.println("UH OH ERRORED!");
            e.printStackTrace();
            //Do not let this happen!
            System.exit(-1);
        }
    }
}
