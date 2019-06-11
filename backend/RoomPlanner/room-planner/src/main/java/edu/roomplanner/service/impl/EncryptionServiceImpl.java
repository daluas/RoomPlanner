package edu.roomplanner.service.impl;

import edu.roomplanner.service.EncryptionService;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionServiceImpl implements EncryptionService {

    @Value("${security.encryption.password}")
    private String password;

    public EncryptionServiceImpl() {
    }

    @Override
    public String encrypt(String text) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPasswordCharArray(password.toCharArray());

        return textEncryptor.encrypt(text);
    }

    @Override
    public String decrypt(String text) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPasswordCharArray(password.toCharArray());

        return textEncryptor.decrypt(text);
    }

}
