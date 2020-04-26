package com.example.orchestration.config;

import io.nats.client.Connection;
import io.nats.client.Nats;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "nats")
public class NatsConfig {
  private String host;

  @Bean
  public Connection natsConnection() {
    try {
      return Nats.connect(this.host);
    } catch (Exception ex) {
      return null;
    }

  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }
}
