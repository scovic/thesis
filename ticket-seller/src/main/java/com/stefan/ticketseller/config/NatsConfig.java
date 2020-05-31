package com.stefan.ticketseller.config;

import io.nats.client.Connection;
import io.nats.client.Nats;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ConfigurationProperties(prefix = "nats")
public class NatsConfig {
  private String host;

  @Bean
  @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
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
