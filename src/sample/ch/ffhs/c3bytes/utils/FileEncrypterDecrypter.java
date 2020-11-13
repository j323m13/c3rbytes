package sample.ch.ffhs.c3bytes.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class FileEncrypterDecrypter {
    SecretKeySpec secretKeySpec;
    Cipher cipher;

    public FileEncrypterDecrypter(SecretKeySpec secretKeySpec, String transformation){
        this.secretKeySpec = secretKeySpec;
        try {
            this.cipher = Cipher.getInstance(transformation);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        };
    }

    public void encrypt(String content, String fileName) {
        try {
            //cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(content.getBytes());

            //byte[] iv = cipher.getIV();
            FileOutputStream fileOut = new FileOutputStream(fileName);
            CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher);
            //fileOut.write(iv);
            System.out.println("encrypted: " + encrypted);
            cipherOut.write(encrypted);
            cipherOut.close();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String decrypt(String fileName) {
        String content = null;
        String erg = null;

        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            //byte[]fileIv = new byte[16];
            //fileIn.read(fileIv);

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);



            //cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(fileIv));
            CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = cipherIn.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            byte[] cipherByteArray = baos.toByteArray();

            byte[] cipherData2 = cipher.doFinal(cipherByteArray);
            erg = new String(cipherData2);
            System.out.println(erg);



        /*
        InputStreamReader inputReader = new InputStreamReader(cipherIn);



        BufferedReader reader = new BufferedReader(inputReader);
                    StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        byte[] cipherData2 = cipher.doFinal(sb.);
        String erg = new String(cipherData2);
        System.out.println(erg);

        content = sb.toString();    */

        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            System.out.println("wrong pw");
        }			catch (IOException e1) {
            // TODO Auto-generated catch block
            System.out.println("wrong pw");
        }

        System.out.println("content: " + erg);
        return erg;
    }

}


