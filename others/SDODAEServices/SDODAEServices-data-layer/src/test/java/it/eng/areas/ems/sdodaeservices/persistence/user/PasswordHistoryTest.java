/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.persistence.user;

import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.hash.Hashing;

import it.eng.areas.ems.sdodaeservices.entity.PasswordHistoryDO;
import it.eng.areas.ems.sdodaeservices.service.UserTransactionalService;

/**
 * @author Bifulco Luigi
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UserServiceCtx.class)
public class PasswordHistoryTest {

	@Autowired
	private UserTransactionalService userService;

	@Test
	public final void checkPasswordHistoryTest() {
		String password = "prova";
		String hashPwd = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
		System.out.println(hashPwd);

		PasswordHistoryDO hist = userService.searchPasswordHistory("prova", hashPwd);
		Assert.assertNotNull("Password non trovata", hist);
		Assert.assertTrue(hist.getPasswordHash().equals(hashPwd));
	}

}
