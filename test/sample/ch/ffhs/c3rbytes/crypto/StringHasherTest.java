package sample.ch.ffhs.c3rbytes.crypto;

import org.junit.Test;
import sample.ch.ffhs.c3rbytes.crypto.StringHasher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringHasherTest {

    @Test
    public void samePWHasherTest(){
        StringHasher shaONE = new StringHasher();
        String shaONEstr = shaONE.encryptSHA3("SHA3-512", "123");

        StringHasher shaTWO = new StringHasher();
        String shaTWOstr = shaTWO.encryptSHA3("SHA3-512", "123");

        assertTrue(shaONEstr.equals(shaTWOstr));
    }

    @Test
    public void differentPWHasherTest(){
        StringHasher shaONE = new StringHasher();
        String shaONEstr = shaONE.encryptSHA3("SHA3-512", "test");

        StringHasher shaTWO = new StringHasher();
        String shaTWOstr = shaTWO.encryptSHA3("SHA3-512", "no test");

        assertFalse(shaONEstr.equals(shaTWOstr));
    }
}
