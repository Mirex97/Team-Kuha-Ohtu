package vinkkikirjasto.dao;

import java.sql.SQLException;
import java.util.List;
import vinkkikirjasto.kategoriat.Video;

public class VideoDao implements Dao<Video, Integer> {

    private Database database;

    public VideoDao(Database database) {
        this.database = database;
    }

    @Override
    public Video findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Video> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Video saveOrUpdate(Video object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
