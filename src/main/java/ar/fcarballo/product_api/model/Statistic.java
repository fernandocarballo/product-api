package ar.fcarballo.product_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Statistic {
    @Id
	private String categoryName;
    
    private int productsCount;

    public Statistic() {}

    public Statistic(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public void add() {
        this.productsCount++;
    }

    public void sub() {
        this.productsCount--;
    }
}
