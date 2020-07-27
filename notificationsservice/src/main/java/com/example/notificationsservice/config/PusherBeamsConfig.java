package com.example.notificationsservice.config;

import com.pusher.pushnotifications.PushNotifications;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ConfigurationProperties
public class PusherBeamsConfig {
  private String instanceId;
  private String secretKey;

  @Bean
  @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
  public PushNotifications getPushNotifications() {
    return new PushNotifications(this.instanceId, this.secretKey);
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public String getSecretKey() {
    return secretKey;
  }
}
