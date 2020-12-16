package sample.ch.ffhs.c3rbytes.databaseEntry;

import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import sample.ch.ffhs.c3rbytes.databaseEntry.DatabaseEntry;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the DatabaseEntry Class.
 */
class DatabaseEntryTest {



    @Test
    void getDescriptionTest() throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String descriptionTest = "Social";
        final Field field = entry.getClass().getDeclaredField("description");
        field.setAccessible(true);
        field.set(entry,new SimpleStringProperty(descriptionTest));
        final String result = entry.getDescription();

        assertEquals(descriptionTest,result,"Field value was retrieved successfully");
    }

    @Test
    void setDescriptionTest() throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String descriptionTest = "Social";
        entry.setDescription(descriptionTest);
        final Field field = entry.getClass().getDeclaredField("description");
        field.setAccessible(true);
        SimpleStringProperty result = (SimpleStringProperty) field.get(entry);
        assertEquals(descriptionTest,result.get());
    }

    @Test
    void getUrlTest() throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String urlTest = "www.facebook.com";
        final Field field = entry.getClass().getDeclaredField("url");
        field.setAccessible(true);
        field.set(entry,new SimpleStringProperty(urlTest));
        final String result = entry.getUrl();

        assertEquals(urlTest,result);
    }

    @Test
    void setUrlTest() throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String urlTest = "www.facebook.com";
        final Field field = entry.getClass().getDeclaredField("url");
        field.setAccessible(true);
        field.set(entry, new SimpleStringProperty(urlTest));
        final String result = entry.getDescription();
        assertNotEquals(urlTest, field.get(entry));
    }

    @Test
    void getPasswordTest() throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String passwordTest = "123456789";
        final Field field = entry.getClass().getDeclaredField("password");
        field.setAccessible(true);
        field.set(entry,new SimpleStringProperty(passwordTest));
        final String result = entry.getPassword();

        assertEquals(passwordTest,result);
    }

    @Test
    void setPasswordTest() throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String passwordTest = "123456789";
        entry.setPassword(passwordTest);
        final Field field = entry.getClass().getDeclaredField("password");
        field.setAccessible(true);
        SimpleStringProperty result = (SimpleStringProperty) field.get(entry);
        assertEquals(passwordTest, result.get());
    }

    @Test
    void getCreationDateTest()  throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String creationDateTest = entry.getCreationDate();
        final Field field = entry.getClass().getDeclaredField("creationDate");
        field.setAccessible(true);
        field.set(entry,new SimpleStringProperty(creationDateTest));
        final String result = entry.getCreationDate();
        assertEquals(creationDateTest,result);
    }

    @Test
    void setCreationDateTest() throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String creationDateTest = DatabaseEntry.getDateTime();
        entry.setCreationDate(creationDateTest);
        final Field field = entry.getClass().getDeclaredField("creationDate");
        field.setAccessible(true);
        field.set(entry, new SimpleStringProperty(creationDateTest));
        SimpleStringProperty result = (SimpleStringProperty) field.get(entry);
        assertEquals(creationDateTest, result.get());
    }

    @Test
    void getLastUpdateTest() throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String lastUpdateTest = entry.getCreationDate();
        final Field field = entry.getClass().getDeclaredField("lastUpdate");
        field.setAccessible(true);
        field.set(entry,new SimpleStringProperty(lastUpdateTest));
        final String result = entry.getLastUpdate();
        assertEquals(lastUpdateTest,result);
    }

    @Test
    void setLastUpdateTest() throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String lastUpdateTest = entry.getCreationDate();
        final Field field = entry.getClass().getDeclaredField("lastUpdate");
        field.setAccessible(true);
        field.set(entry, new SimpleStringProperty(lastUpdateTest));
        SimpleStringProperty result = (SimpleStringProperty) field.get(entry);


        assertEquals(lastUpdateTest, result.get());
    }

    @Test
    void getUsernameTest() throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String usernameTest = "Jérémie";
        final Field field = entry.getClass().getDeclaredField("username");
        field.setAccessible(true);
        field.set(entry,new SimpleStringProperty(usernameTest));
        final String result = entry.getUsername();
        assertEquals(usernameTest,result);
    }

    @Test
    void setUsernameTest() throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String usernameTest = "Jérémie";
        entry.setUsername(usernameTest);
        final Field field = entry.getClass().getDeclaredField("username");
        field.setAccessible(true);
        SimpleStringProperty result = (SimpleStringProperty) field.get(entry);

        assertEquals(usernameTest, result.get());
    }

    @Test
    void getIdTest() throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String idTest = "1";
        final Field field = entry.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(entry,new SimpleStringProperty(idTest));
        final String result = entry.getId();
        assertEquals(idTest,result);
    }

    @Test
    void getDummyIdTest() throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String dummyIdTest = "007";
        final Field field = entry.getClass().getDeclaredField("dummyId");
        field.setAccessible(true);
        field.set(entry,dummyIdTest);
        final String result = entry.getDummyId();
        assertEquals(dummyIdTest,result);
    }

    @Test
    void setDummyIdTest() throws NoSuchFieldException, IllegalAccessException {
            DatabaseEntry entry = new DatabaseEntry();
            String dummyIdTest = "007";
            final Field field = entry.getClass().getDeclaredField("dummyId");
            field.setAccessible(true);
            field.set(entry, dummyIdTest);
            assertEquals(dummyIdTest, field.get(entry));
    }

    @Test
    void setIdTest() throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String idTest = "1";
        entry.setId(idTest);
        final Field field = entry.getClass().getDeclaredField("id");
        field.setAccessible(true);
        SimpleStringProperty result = (SimpleStringProperty) field.get(entry);
        assertEquals(idTest, result.get());
    }

    @Test
    void getNoteTest() throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String noteTest = "This is a note.";
        final Field field = entry.getClass().getDeclaredField("note");
        field.setAccessible(true);
        field.set(entry,new SimpleStringProperty(noteTest));
        final String result = entry.getNote();
        assertEquals(noteTest,result);
    }

    @Test
    void setNoteTest() throws NoSuchFieldException, IllegalAccessException {
        DatabaseEntry entry = new DatabaseEntry();
        String noteTest = "This a note.";
        entry.setNote(noteTest);
        final Field field = entry.getClass().getDeclaredField("note");
        field.setAccessible(true);
        SimpleStringProperty result = (SimpleStringProperty) field.get(entry);

        assertEquals(noteTest, result.get());
    }

}