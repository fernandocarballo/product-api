package ar.fcarballo.product_api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.MediaType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Type;

/*
 * Allows to expose the id of the entity in the REST API
 */
@Configuration
public class RepositoryRestConfig {

    @Bean
	public RepositoryRestConfigurer configureRepositoryRestConfiguration(EntityManager entityManager) {
    	return RepositoryRestConfigurer.withConfig(config -> {
            config.exposeIdsFor(entityManager.getMetamodel().getEntities()
                    .stream().map(Type::getJavaType).toArray(Class[]::new));
            config.setDefaultMediaType(MediaType.APPLICATION_JSON);
        });
    }

}
