package vinkkikirjasto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import vinkkikirjasto.luokat.Base;

public class BaseDao implements Dao<Base, Integer> {

    private Database database;

    public BaseDao(Database database) {
        this.database = database;
    }

    @Override
    public Base findOne(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Base "
                + "WHERE (id IS ?)");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        Base base = new Base(
                rs.getInt("id"),
                rs.getString("otsikko"),
                rs.getString("tyyppi"),
                rs.getString("kommentti"),
                rs.getString("isbn"),
                rs.getString("kirjoittaja"),
                null
        );
        rs.close();
        stmt.close();
        conn.close();
        return base;
    }

    @Override
    public List<Base> findAll() throws SQLException {
        List<Base> bases = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Base");
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {

            bases.add(new Base(
                    rs.getInt("id"),
                    rs.getString("otsikko"),
                    rs.getString("tyyppi"),
                    rs.getString("kommentti"),
                    rs.getString("isbn"),
                    rs.getString("kirjoittaja"),
                    null
            ));
        }

        rs.close();
        statement.close();
        connection.close();

        return bases;

    }

    @Override
    public Base saveOrUpdate(Base object) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement testStatement = conn.prepareStatement("SELECT * FROM Base WHERE (id IS ?)");
        testStatement.setInt(1, object.getId());
        ResultSet rs = testStatement.executeQuery();
        boolean hasOne = rs.next();
        if (hasOne) {
            testStatement.close();
            PreparedStatement updateStatement = conn.prepareStatement("UPDATE Base SET "
                    + "otsikko = ?,"
                    + "tyyppi = ?,"
                    + "kommentti = ?,"
                    + "isbn = ?,"
                    + "kirjoittaja = ?,"
                    + "url = ?,"
                    + "author = ?,"
                    + "nimi = ?,"
                    + "kuvaus = ?"
                    + "WHERE (id = ?)");
            updateStatement.setString(1, object.getOtsikko());
            updateStatement.setString(2, object.getTyyppi());
            updateStatement.setString(3, object.getKommentti());
            updateStatement.setString(4, object.getIsbn());
            updateStatement.setString(5, object.getKirjoittaja());
            updateStatement.setString(6, object.getUrl());
            updateStatement.setString(7, object.getAuthor());
            updateStatement.setString(8, object.getNimi());
            updateStatement.setString(9, object.getKuvaus());
            updateStatement.setInt(10, object.getId());
            updateStatement.executeUpdate();
            updateStatement.close();

            rs.close();
            conn.close();
            return null;
        }
        testStatement.close();
        rs.close();
        PreparedStatement saveStatement = conn.prepareStatement("INSERT INTO Base (otsikko, tyyppi, kommentti, isbn, kirjoittaja, url, author, nimi, kuvaus) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        saveStatement.setString(1, object.getOtsikko());
        saveStatement.setString(2, object.getTyyppi());
        saveStatement.setString(3, object.getKommentti());
        saveStatement.setString(4, object.getIsbn());
        saveStatement.setString(5, object.getKirjoittaja());
        saveStatement.setString(6, object.getUrl());
        saveStatement.setString(7, object.getAuthor());
        saveStatement.setString(8, object.getNimi());
        saveStatement.setString(9, object.getKuvaus());
        saveStatement.executeUpdate();
        saveStatement.close();

        PreparedStatement retrieveStatement = conn.prepareStatement("SELECT last_insert_rowid() as id");

        rs = retrieveStatement.executeQuery();
        rs.next();
        int latestId = rs.getInt("id");
        object.setId(latestId);
        rs.close();
        retrieveStatement.close();
        conn.close();
        return object;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement removeStatement = conn.prepareStatement("DELETE FROM Base WHERE (id = ?)");
        removeStatement.setInt(1, key);
        removeStatement.execute();
        removeStatement.close();
        conn.close();
    }

}
