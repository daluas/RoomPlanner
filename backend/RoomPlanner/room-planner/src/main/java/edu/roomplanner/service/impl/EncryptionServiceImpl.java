package edu.roomplanner.service.impl;

import edu.roomplanner.service.EncryptionService;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EncryptionServiceImpl implements EncryptionService {

    private static char[] password;

    static {
        try (InputStream in = EncryptionServiceImpl.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties props = new Properties();
            props.load(in);
            password = props.getProperty("password").toCharArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String encrypt(String text) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPasswordCharArray(password);
        String encryptedText = textEncryptor.encrypt(text);

        return encryptedText;
    }

    @Override
    public String decrypt(String text) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPasswordCharArray(password);
        String decryptedText = textEncryptor.decrypt(text);

        return decryptedText;
    }

    public static char[] getPassword() {
        return password;
    }

}
