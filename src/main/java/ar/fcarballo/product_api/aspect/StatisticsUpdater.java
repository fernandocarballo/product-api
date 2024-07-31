package ar.fcarballo.product_api.aspect;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import ar.fcarballo.product_api.model.Product;
import ar.fcarballo.product_api.repository.ProductRepository;
import ar.fcarballo.product_api.service.StatisticsService;

@EnableAspectJAutoProxy
@Aspect
@Component
public class StatisticsUpdater {

    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private ProductRepository productRepository;

    @Before("execution(* ar.fcarballo.product_api.repository.ProductRepository.deleteById(..)) && args(productId)")
    public void afterDeleteProduct(Integer productId) throws Exception {
        if(productId != null)
            productRepository.findById(productId)
                .ifPresent(product -> statisticsService.deleteProduct(product.getCategory()));
    }

    @Before("execution(* ar.fcarballo.product_api.repository.ProductRepository.save(..)) && args(product)")
    public void afterSaveProduct(Product product) throws Throwable {
        if(product.getId() == null)
            statisticsService.insertProduct(product.getCategory());
    }
}
