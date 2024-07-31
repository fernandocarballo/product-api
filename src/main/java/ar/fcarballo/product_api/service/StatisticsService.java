package ar.fcarballo.product_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ar.fcarballo.product_api.model.Statistic;
import ar.fcarballo.product_api.repository.StatisticsRepository;
import lombok.extern.java.Log;

@Service
@Log
public class StatisticsService {
    @Autowired
    private StatisticsRepository repository;

    @Async
    public void insertProduct(final String category) {
        repository.findById(category)
            .ifPresentOrElse(this::addAndSave, 
                () -> this.addAndSave(new Statistic(category)));
    }

    @Async
    public void deleteProduct(final String category) {
        repository.findById(category)
            .ifPresent(this::subAndSave);
        
    }

    private void addAndSave(Statistic statistic) {
        statistic.add();
        repository.save(statistic);
        log.info("Product added to statistics");
    }
    private void subAndSave(Statistic statistic) {
        statistic.sub();
        repository.save(statistic);
        log.info("Product removed from statistics");
    }

    @Async
    public void updateProduct(final String categoryStored, final String categoryModified) {
        if (categoryStored.compareTo(categoryModified) != 0) {
            deleteProduct(categoryStored);
            insertProduct(categoryModified);
        }
    }
}
