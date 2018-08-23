package it.eng.areas.ems.sdodaeservices.utils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.hash.Hashing;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;

@Component
public class PasswordUtils {

	@Value("${server.password.minLength}")
	protected int minLength = 8;
	// The higher the number of iterations the more
	// expensive computing the hash is for us and
	// also for an attacker.
	private static final int iterations = 20 * 1000;
	private static final int saltLen = 32;
	private static final int desiredKeyLen = 256;

	public void setNewPassword(UtenteDO utente, String password) {

	}

	/**
	 * Computes a salted PBKDF2 hash of given plaintext password suitable for
	 * storing in a database. Empty passwords are not supported.
	 */
	public static String getSaltedHash(String password) throws Exception {
		byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
		// store the salt with the password
		return Base64.getEncoder().encodeToString(salt) + "$" + hash(password, salt);
	}

	/**
	 * Checks whether given plaintext password corresponds to a stored salted hash
	 * of the password.
	 */
	public static boolean check(String password, String stored) throws Exception {
		String[] saltAndPass = stored.split("\\$");
		if (saltAndPass.length == 1) {
			// se il formato non è salt$hash vuol dire che la password è salvata nel vecchio
			// metodo, quindi utilizzo il vecchio metodo per calcolare la pass
			return stored.equals(oldHash(password));
		} else {
			String hashOfInput = hash(password, Base64.getDecoder().decode(saltAndPass[0]));
			return hashOfInput.equals(saltAndPass[1]);
		}
	}

	// using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
	// cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
	private static String hash(String password, byte[] salt) throws Exception {
		if (password == null || password.length() == 0)
			throw new IllegalArgumentException("Empty passwords are not supported.");
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		SecretKey key = f.generateSecret(new PBEKeySpec(password.toCharArray(), salt, iterations, desiredKeyLen));
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

	private static String oldHash(String password) throws Exception {
		return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
	}

	public boolean checkStrength(String pass) {
		if (pass.length() < minLength) {
			return false;
		}
		Zxcvbn zxcvbn = new Zxcvbn();
		Strength strength = zxcvbn.measure(pass);

		return strength.getScore() >= 2;
	}

	public boolean checkStrengthOld(String pass) {
		int score = 0;

		if (pass.length() < minLength) {
			return false;
		}
		// award every unique letter until 5 repetitions
		Map<Character, Integer> letters = new HashMap<>();

		for (Character ch : pass.toCharArray()) {
			Integer i = letters.get(ch);
			i = (i != null ? i : 0) + 1;
			letters.put(ch, i);

			score += 5.0 / i;
		}

		// bonus points for mixing it up
		boolean[] variations = new boolean[] { pass.matches("/d"), //
				pass.matches("[a-z]"), //
				pass.matches("[A-Z]"), //
				pass.matches("/W") };

		int variationCount = 0;
		for (boolean check : variations) {
			variationCount += check ? 1 : 0;
		}
		score += (variationCount - 1) * 10;

		return score >= 60;
	}
}
