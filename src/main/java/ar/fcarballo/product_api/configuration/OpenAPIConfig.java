package ar.fcarballo.product_api.configuration;

import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.Links;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {

	/*
	 * This bean is used to register the swaggerBasicAuthFilter bean.
	 */
	@Bean
	public FilterRegistrationBean<DelegatingFilterProxy> swaggerBasicAuthFilterRegistration() {
		FilterRegistrationBean<DelegatingFilterProxy> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new DelegatingFilterProxy("swaggerBasicAuthFilter"));
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}

	/*
	 * This bean is used to configure the CORS.
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("*")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
						.allowedHeaders("*")
						.maxAge(3600);
			}
		};
	}

	/*
	 * This bean is used to configure the OpenAPI info.
	 */
	@Bean
	public OpenAPI myOpenAPI() {

		SpringDocUtils.getConfig().addResponseTypeToIgnore(Links.class);
		Contact contact = new Contact();
		contact.setEmail("fcarballo@gmail.com");
		contact.setName("Fernando Carballo");
		contact.setUrl("https://github.com/fernandocarballo");

		License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

		Info info = new Info().title("Productos REST API").version("1.0").contact(contact)
				.description("Esta API permite crear, modificar y consultar productos.").license(mitLicense);

		return new OpenAPI().info(info);
	}

	@Bean
	public ForwardedHeaderFilter forwardedHeaderFilter() {
		return new ForwardedHeaderFilter();
	}
}