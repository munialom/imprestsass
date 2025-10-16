package com.ctecx.brs.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configure static resources handling
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600) // Cache for 1 hour (in seconds)
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        // Exclude API paths from static resource handling
                        if (resourcePath.startsWith("api/") ||
                                resourcePath.startsWith("auth/") ||
                                resourcePath.startsWith("actuator/") ||
                                resourcePath.startsWith("swagger/") ||
                                resourcePath.startsWith("v3/api-docs/")) {
                            return null;
                        }

                        // Try to resolve the requested resource
                        Resource requestedResource = location.createRelative(resourcePath);

                        // If the requested resource exists, return it
                        if (requestedResource.exists() && requestedResource.isReadable()) {
                            return requestedResource;
                        }

                        // For SPA fallback, check if index.html exists before returning it
                        Resource indexResource = new ClassPathResource("/static/index.html");
                        if (indexResource.exists() && indexResource.isReadable()) {
                            return indexResource;
                        }

                        // If index.html doesn't exist, return null to let Spring handle the 404
                        return null;
                    }
                });

        // Optional: Add handler for webjars if you're using them
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCachePeriod(3600);
    }
}