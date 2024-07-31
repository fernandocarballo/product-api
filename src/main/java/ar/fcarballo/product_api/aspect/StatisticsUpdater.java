package ar.fcarballo.product_api.aspect;
import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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

    @Around("execution(* ar.fcarballo.product_api.repository.ProductRepository.deleteById(..)) && args(productId)")
    public Object aroundDeleteProduct(ProceedingJoinPoint joinPoint, Integer productId) throws Throwable {
        String category = getCategory(productId);

        Object result = joinPoint.proceed();

        if (category != null)
            statisticsService.deleteProduct(category);

        return result;
    }

    @Around("execution(* ar.fcarballo.product_api.repository.ProductRepository.save(..)) && args(product)")
    public Object aroundSaveProduct(ProceedingJoinPoint joinPoint, Product product) throws Throwable {
        String category = null;
        if (product.getId() == null)
           category = product.getCategory();

        Object result = joinPoint.proceed();

        if (category != null)
            statisticsService.insertProduct(category);

        return result;
    }

    private String getCategory(Integer productId) {
        if(productId == null)
            return null;

        Optional<Product> product = productRepository.findById(productId);
        if (!product.isPresent())
           return null;
        
        return product.get().getCategory();
    }
}
