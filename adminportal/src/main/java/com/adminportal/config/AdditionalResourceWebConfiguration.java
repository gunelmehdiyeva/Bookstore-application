package com.adminportal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public abstract class AdditionalResourceWebConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/").addResourceLocations("file:/src/main/resources/upload/");
    }
}
