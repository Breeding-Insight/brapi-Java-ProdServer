package org.brapi.test.BrAPITestServer;

import org.brapi.test.BrAPITestServer.model.entity.BrAPIPrimaryEntity;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "org.brapi.test.BrAPITestServer.repository.nonBaseRepos",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BrAPIPrimaryEntity.class))
public class NonBaseRepoConfig {
}
