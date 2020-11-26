package sample.ch.ffhs.c3rbytes.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao {
    static List getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    Dao save(DatabaseEntry databaseEntry) throws SQLException, ClassNotFoundException;
    Dao update(DatabaseEntry databaseEntry) throws SQLException, ClassNotFoundException;
    Dao delete(DatabaseEntry databaseEntry) throws SQLException, ClassNotFoundException;
}
