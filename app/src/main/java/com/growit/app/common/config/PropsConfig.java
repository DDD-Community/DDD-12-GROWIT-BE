package com.growit.app.common.config;

import com.growit.app.common.filter.ApiKeyProps;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApiKeyProps.class)
public class PropsConfig {}
