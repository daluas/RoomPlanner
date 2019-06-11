package edu.roomplanner;

import edu.roomplanner.service.impl.EncryptionServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class EncriptionServiceUnitTest {

    private EncryptionServiceImpl sut=new EncryptionServiceImpl();

    @Before
    public void SetUp(){
        sut.setPassword("key");
    }

    @Test
    public void testingDecryption() {
        String encryptedValue="+QzrtdhQ0T0QaPqM32s0Tzbt/lZPNNa9";
        String decrypted=sut.decrypt(encryptedValue);
        String originalValue="password";
        Assertions.assertEquals(originalValue,decrypted);
    }

    @Test
    public void testingEncryption(){
        String originalValue="password";
        String encryptedValue="+QzrtdhQ0T0QaPqM32s0Tzbt/lZPNNa9";
        String encrypted=sut.encrypt(originalValue);
        Assertions.assertNotEquals(encryptedValue,encrypted);
    }

    @Test
    public void testingEncryptionAndDecryption() {
        String actual = "password";
        String encrypted = sut.encrypt(actual);
        String decrypted = sut.decrypt(encrypted);
        Assertions.assertEquals(decrypted, actual);
    }

    @After
    public void CleanUp(){
        sut=null;
    }
}
