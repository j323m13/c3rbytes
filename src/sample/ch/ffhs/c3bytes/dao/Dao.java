package sample.ch.ffhs.c3bytes.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao {
    static List getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    Dao getEntryById(int id) throws SQLException, ClassNotFoundException;
    Dao save();
    Dao update();
    Dao delete();
}
