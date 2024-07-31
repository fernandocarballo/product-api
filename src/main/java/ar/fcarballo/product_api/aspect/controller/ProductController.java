package ar.fcarballo.product_api.aspect.controller;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.fcarballo.product_api.model.Product;
import ar.fcarballo.product_api.repository.ProductRepository;
import ar.fcarballo.product_api.service.StatisticsService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RepositoryRestController
@Tag(name = "Productos - Controller")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StatisticsService statisticsService;

    @PatchMapping("/products/{id}")
    @ResponseBody
    public Product patchProduct(@PathVariable("id") Integer id, @RequestBody Product updatedProduct) {
        return updateProduct(id, updatedProduct);
    }

    @PutMapping("/products/{id}")
    @ResponseBody
    public Product putProduct(@PathVariable("id") Integer id, @RequestBody Product updatedProduct) {
        return updateProduct(id, updatedProduct);
    }

    protected Product updateProduct(@PathVariable("id") Integer id, @RequestBody Product updatedProduct) {

        String storedCategory = null;
        if (id == null)
            return null;
        Optional<Product> optionalStored = productRepository.findById(id);
        if (optionalStored.isEmpty())
            return null;
        
        Product storedProduct = optionalStored.get();
        storedCategory = storedProduct.getCategory();
        BeanUtils.copyProperties(updatedProduct, storedProduct, "id");

        Product savedProduct = productRepository.save(storedProduct);

        statisticsService.updateProduct(storedCategory, savedProduct.getCategory());

        return savedProduct;
    }
}
