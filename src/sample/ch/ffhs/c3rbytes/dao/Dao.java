package sample.ch.ffhs.c3rbytes.dao;

import sample.ch.ffhs.c3rbytes.databaseEntry.DatabaseEntry;

import java.sql.SQLException;
import java.util.List;

public interface Dao {
    static List getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    boolean save(DatabaseEntry databaseEntry) throws SQLException, ClassNotFoundException;
    void update(DatabaseEntry databaseEntry) throws SQLException, ClassNotFoundException;
    void delete(DatabaseEntry databaseEntry) throws SQLException, ClassNotFoundException;
    void deleteAccount() throws SQLException, ClassNotFoundException, InterruptedException;
    void setup() throws SQLException, ClassNotFoundException, InterruptedException;
    void connect() throws SQLException;
    void disconnect() throws SQLException;
}
