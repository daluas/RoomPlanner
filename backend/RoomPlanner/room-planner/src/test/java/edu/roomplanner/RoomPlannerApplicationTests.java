package edu.roomplanner;

import edu.roomplanner.service.impl.EncryptionServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoomPlannerApplicationTests {

	@Test
	public void contextLoads() {
	}
	@Test
	public void TestEncryption()
	{
		String actual="password";
		EncryptionServiceImpl EncryptionServiceImplementation=new EncryptionServiceImpl();
		String encrypted=EncryptionServiceImplementation.encrypt(actual);
		String decrypted=EncryptionServiceImplementation.decrypt(encrypted);
		Assertions.assertEquals(decrypted,actual);

	}

}
