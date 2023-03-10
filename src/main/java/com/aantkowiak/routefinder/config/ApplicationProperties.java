package com.aantkowiak.routefinder.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "datasource.service")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ApplicationProperties {
  private String countriesDataUrl;
}
