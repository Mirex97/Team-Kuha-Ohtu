package vinkkikirjasto.dao;

import java.sql.SQLException;
import java.util.List;

public class PodcastDao implements Dao<Podcast, Integer> {

    private Database database;

    public PodcastDao(Database database) {
        this.database = database;
    }

    @Override
    public Podcast findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Podcast> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Podcast saveOrUpdate(Podcast object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
