package ar.fcarballo.product_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Statistic {
    @Id
	private String categoryName;
    
    private int productsCount;

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
