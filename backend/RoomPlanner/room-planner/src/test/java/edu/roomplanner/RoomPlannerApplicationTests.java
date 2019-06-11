package edu.roomplanner;

import edu.roomplanner.service.impl.EncryptionServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Component
public class RoomPlannerApplicationTests {

	@Autowired
	private EncryptionServiceImpl EncryptionServiceImplementation;

	@Test
	public void contextLoads() {
	}
	@Test
	public void TestEncryption()
	{
		String actual="password";
		String encrypted=EncryptionServiceImplementation.encrypt(actual);
		String decrypted=EncryptionServiceImplementation.decrypt(encrypted);
		Assertions.assertEquals(decrypted,actual);

	}
}
