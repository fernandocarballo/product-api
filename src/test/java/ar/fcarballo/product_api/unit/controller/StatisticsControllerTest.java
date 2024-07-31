package ar.fcarballo.product_api.unit.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import ar.fcarballo.product_api.model.Product;
import ar.fcarballo.product_api.model.Statistic;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StatisticsControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static final String BASE_URL = "http://localhost:";
    private static final String STATISTICS_URL = "/statistics";
    private static final String PRODUCTS_URL = "/products";

    @Value("${swagger.username}")
    private String user;
    @Value("${swagger.password}")
    private String password;

    private HttpHeaders headers = getHeaders();
    private JSONObject productJson = createProductJson();

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
    
    @BeforeEach
    public void setup() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    
    @Test
    void listStatistics() throws Exception {
        String categoryName = createProductWithCategory();
        
        ParameterizedTypeReference<ListResponse> responseType = new ParameterizedTypeReference<ListResponse>() {
        };

        // Realiza la solicitud GET utilizando exchange
        ResponseEntity<ListResponse> response = testRestTemplate
                .withBasicAuth(user, password)
                .exchange(BASE_URL + port + STATISTICS_URL, HttpMethod.GET, null, responseType);

        assertTrue(response.getStatusCode() == HttpStatus.OK);
        ListResponse listResponse = response.getBody();

        assertNotNull(listResponse);
        List<Statistic> list = listResponse._embedded.Estadisticas;
        assertTrue(list.size() >= 1);
        list.stream()   .filter(p -> p.getCategoryName() == categoryName)
                        .findFirst()
                        .ifPresent(p -> assertTrue(p.getProductsCount() == 1));
    }

    
    @Test
    void getStatistic() throws Exception {
        String categoryName = createProductWithCategory();
        
        // Realiza la solicitud GET utilizando exchange
        ResponseEntity<Statistic> response = testRestTemplate
                .withBasicAuth(user, password)
                .getForEntity(BASE_URL + port + STATISTICS_URL + "/" + categoryName, Statistic.class);

        assertTrue(response.getStatusCode() == HttpStatus.OK);
        Statistic statistic = response.getBody();

        assertNotNull(statistic);
        assertTrue(statistic.getProductsCount() == 1);
    }

    String createProductWithCategory() throws Exception {
        HttpEntity<String> request = new HttpEntity<String>(productJson.toString(), headers);

        ResponseEntity<Product> response = testRestTemplate
                .withBasicAuth(user, password)
                .postForEntity(BASE_URL + port + PRODUCTS_URL, request, Product.class);

        return response.getBody().getCategory();
    }

    
    private JSONObject createProductJson() {
        JSONObject product = new JSONObject();
        product.put("name", "Martillo");
        product.put("category", "Martillos");
        product.put("sku", "AKS2");
        product.put("price", 1234);

        return product;
    }

    @NoArgsConstructor
    static class Embedded {
        public List<Statistic> Estadisticas;
    }

    @NoArgsConstructor
    static class ListResponse {
        public Embedded _embedded;
    }

}
