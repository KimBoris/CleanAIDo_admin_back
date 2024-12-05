package org.zerock.cleanaido_admin_back.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class ImageFileConfig implements WebMvcConfigurer {

    @Value("${org.zerock.upload.path}")
    private String path;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map "/api/v1/images/**" to the directory specified by org.zerock.upload.path
        registry.addResourceHandler("/api/v1/images/**")
                .addResourceLocations("file:" + path+ "\\");
    }
}
