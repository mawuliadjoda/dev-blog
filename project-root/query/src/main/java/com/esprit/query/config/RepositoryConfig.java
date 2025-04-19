package com.esprit.query.config;

import com.esprit.query.persistence.projection.ProductDetail;
import com.esprit.query.persistence.projection.ProductSummary;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

// https://docs.spring.io/spring-data/rest/reference/projections-excerpts.html
@Configuration
public class RepositoryConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config, CorsRegistry cors) {
        config.getProjectionConfiguration().addProjection(ProductDetail.class);
        config.getProjectionConfiguration().addProjection(ProductSummary.class);
    }

}
