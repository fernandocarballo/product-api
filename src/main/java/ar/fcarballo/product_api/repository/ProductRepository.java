package ar.fcarballo.product_api.repository;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import ar.fcarballo.product_api.model.Product;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Productos - Controller") 
@RepositoryRestResource(path = "products", itemResourceRel = "Producto", collectionResourceRel = "Productos")
public interface ProductRepository extends ListCrudRepository<Product, Integer>
{
	@RestResource(path = "byName", rel = "customFindMethod")
	public List<Product> findByNameContainingIgnoreCase(String name);
}
