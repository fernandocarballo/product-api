package ar.fcarballo.product_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import ar.fcarballo.product_api.model.Statistic;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Estadisticas - Controller") 
@RepositoryRestResource(path = "statistics", itemResourceRel = "Estadistica", collectionResourceRel = "Estadisticas")
public interface StatisticsRepository extends Repository<Statistic, String> {

    List<Statistic> findAll();

    Optional<Statistic> findById(String id);

    @RestResource(exported = false)
    <S extends Statistic> S save(S entity);
    
}
