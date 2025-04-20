package com.esprit.common.persistence.config;

import com.esprit.common.persistence.projection.ProductDetail;
import com.esprit.common.persistence.projection.ProductSummary;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * <a href="https://docs.spring.io/spring-data/rest/reference/projections-excerpts.html">...</a>
 *
 * Projection definitions are picked up and made available for clients if they are:
 *
 * Flagged with the @Projection annotation and located in the same package (or sub-package) of the domain type, OR
 *
 * Manually registered by using RepositoryRestConfiguration.getProjectionConfiguration().addProjection(…)
 */



@Configuration
public class RepositoryConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config, CorsRegistry cors) {
        config.getProjectionConfiguration().addProjection(ProductDetail.class);
        config.getProjectionConfiguration().addProjection(ProductSummary.class);
    }

}
