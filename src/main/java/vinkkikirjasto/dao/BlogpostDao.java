package vinkkikirjasto.dao;

import java.sql.SQLException;
import java.util.List;

public class BlogpostDao implements Dao<Blogpost, Integer> {

    private Database database;

    public BlogpostDao(Database database) {
        this.database = database;
    }

    @Override
    public Blogpost findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Blogpost> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Blogpost saveOrUpdate(Blogpost object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
