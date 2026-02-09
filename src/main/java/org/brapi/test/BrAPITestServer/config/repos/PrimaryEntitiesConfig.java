package org.brapi.test.BrAPITestServer.config.repos;

import org.brapi.test.BrAPITestServer.repository.primaryEntities.BrAPIRepositoryImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configures the repos of entities derived directly from BrAPIPrimaryEntity so
 * the custom repository implementation can be used to save entities, additional info, and external references
 * the right way.
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "org.brapi.test.BrAPITestServer.repository.primaryEntities",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "org\\.brapi\\.test\\.BrAPITestServer\\.repository\\.baseEntities\\..*"
        ),
        repositoryBaseClass = BrAPIRepositoryImpl.class)
public class PrimaryEntitiesConfig {
}
