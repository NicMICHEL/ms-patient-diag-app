package com.nm.gateway.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
public class AppEnvironmentVariablesConfig {
}
