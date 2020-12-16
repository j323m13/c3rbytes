package sample.ch.ffhs.c3rbytes.dao;

import sample.ch.ffhs.c3rbytes.databaseEntry.DatabaseEntry;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface Dao {
    default List<DatabaseEntry> getAll() throws SQLException, ClassNotFoundException, InterruptedException, IOException {
        return null;
    }

    boolean save(DatabaseEntry databaseEntry) throws SQLException, ClassNotFoundException, InterruptedException;
    void update(DatabaseEntry databaseEntry) throws SQLException, ClassNotFoundException;
    void delete(DatabaseEntry databaseEntry) throws SQLException, ClassNotFoundException;
    void deleteAccount() throws SQLException, InterruptedException;
    void setup() throws SQLException, InterruptedException, ClassNotFoundException;
    void connect() throws SQLException;
    void disconnect() throws SQLException;
}
