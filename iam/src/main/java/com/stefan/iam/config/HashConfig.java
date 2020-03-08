package com.stefan.iam.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.hash")
public class HashConfig {
  private String algorithm;
  private int iterations;
  private int keyLength;

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public void setIterations(int iterations) {
    this.iterations = iterations;
  }

  public void setKeyLength(int keyLength) {
    this.keyLength = keyLength;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public int getIterations() {
    return iterations;
  }

  public int getKeyLength() {
    return keyLength;
  }
}
