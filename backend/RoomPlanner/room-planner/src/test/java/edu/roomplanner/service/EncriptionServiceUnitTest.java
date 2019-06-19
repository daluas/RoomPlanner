package edu.roomplanner.service;

import edu.roomplanner.service.impl.EncryptionServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class EncriptionServiceUnitTest {

    private EncryptionServiceImpl sut = new EncryptionServiceImpl();

    @Before
    public void setUp() {
        sut.setPassword("key");
    }

    @Test
    public void shouldDecryptThePassword() {
        String encryptedValue = "+QzrtdhQ0T0QaPqM32s0Tzbt/lZPNNa9";
        String decrypted = sut.decrypt(encryptedValue);
        String originalValue = "password";
        assertEquals(originalValue, decrypted);
    }

    @Test
    public void shouldEncryptThePassword() {
        String originalValue = "password";
        String encryptedValue = "+QzrtdhQ0T0QaPqM32s0Tzbt/lZPNNa9";
        String encrypted = sut.encrypt(originalValue);
        assertNotEquals(encryptedValue, encrypted);
    }

    @Test
    public void shouldEncryptThePasswordAndThenDecryptTheEncryptedPassword() {
        String actual = "password";
        String encrypted = sut.encrypt(actual);
        String decrypted = sut.decrypt(encrypted);
        assertEquals(decrypted, actual);
    }

    @After
    public void cleanUp() {
        sut = null;
    }
}
