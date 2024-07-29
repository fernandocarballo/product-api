package ar.fcarballo.product_api.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Integer id;

	@NotBlank(message = "El sku no puede estar vacio")
	private String sku;

	@NotBlank(message = "El nombre no puede estar vacio")
	private String name;

	@NotNull(message = "El nombre no puede ser null")
	private int price;
}
