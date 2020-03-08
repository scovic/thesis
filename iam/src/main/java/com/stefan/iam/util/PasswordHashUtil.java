package com.stefan.iam.util;

import com.stefan.iam.config.HashConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@Component
public class PasswordHashUtil {

  @Autowired
  private HashConfig config;

  public Optional<String> hashPassword(String password, String salt) {
    final int iterations = config.getIterations();
    final int keyLength = config.getKeyLength();
    final String algorithm = config.getAlgorithm();

    char[] chars = password.toCharArray();
    byte[] bytes = salt.getBytes();

    PBEKeySpec spec = new PBEKeySpec(chars, bytes, iterations, keyLength);

    Arrays.fill(chars, Character.MIN_VALUE);

    try {
      SecretKeyFactory fac = SecretKeyFactory.getInstance(algorithm);
      byte[] securePassword = fac.generateSecret(spec).getEncoded();

      return Optional.of(Base64.getEncoder().encodeToString(securePassword));
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      System.err.println("Exception encountered in hashPassword");
      return Optional.empty();
    } finally {
      spec.clearPassword();
    }
  }

  public boolean verifyPassword(String password, String hashedPassword, String salt) {
    Optional<String> optEncrypted = hashPassword(password, salt);

    if (!optEncrypted.isPresent()) {
      return false;
    }

    return optEncrypted.get().equals(hashedPassword);
  }

  public Optional<String> generateSalt (final int length) {
    SecureRandom RAND = new SecureRandom();

    if (length < 1) {
      System.err.println("Error while generating salt");
      return Optional.empty();
    }

    byte[] salt = new byte[length];
    RAND.nextBytes(salt);

    return Optional.of(Base64.getEncoder().encodeToString(salt));
  }
}
