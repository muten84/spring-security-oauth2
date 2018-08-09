package it.eng.areas.ems.sdodaeservices;

import org.junit.Test;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

public class TestPassword {

	@Test
	public void testPassword() {

		String[] passwd = new String[] { "22222222", "asdqwezxc", "qWeRtY" };

		for (String p : passwd) {
			Zxcvbn zxcvbn = new Zxcvbn();
			Strength strength = zxcvbn.measure(p);
			System.out.println("Pass : " + p + " score: " + strength.getScore());
		}
	}

}
