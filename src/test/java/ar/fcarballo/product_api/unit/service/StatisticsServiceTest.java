package ar.fcarballo.product_api.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.fcarballo.product_api.repository.StatisticsRepository;
import ar.fcarballo.product_api.service.StatisticsService;

@SpringBootTest
public class StatisticsServiceTest {

    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private StatisticsRepository repository;

    @Test
    void createAndCheckStatistic() {
        String categoryName = "Clavos";
        statisticsService.syncInsertProduct(categoryName);

        repository.findById(categoryName)
            .ifPresentOrElse(t -> assertEquals(t.getProductsCount(), 1)
                , () -> assertTrue(false));
    }

    @Test
    void createAndUpdateStatistic() {
        String categoryName = "Tuercas";
        statisticsService.syncInsertProduct(categoryName);

        String newCategoryName = "Tornillos";
        statisticsService.syncUpdateProduct(categoryName, newCategoryName);

        repository.findById(categoryName)
            .ifPresent(t -> assertEquals(t.getProductsCount(), 0));
        
        repository.findById(newCategoryName)
            .ifPresentOrElse(t -> assertEquals(t.getProductsCount(), 1)
                , () -> assertTrue(false));

    }

    
    @Test
    void createAndDeleteStatistic() {
        String categoryName = "Remaches";
        statisticsService.syncInsertProduct(categoryName);

        statisticsService.syncDeleteProduct(categoryName);

        repository.findById(categoryName)
            .ifPresent(t -> assertEquals(t.getProductsCount(), 0));
    }
}
