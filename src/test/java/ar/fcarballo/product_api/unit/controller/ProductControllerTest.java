package ar.fcarballo.product_api.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import net.minidev.json.JSONObject;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static final String BASE_URL = "http://localhost:";
    private static final String PRODUCTS_URL = "/products";

    @Value("${swagger.username}")
    private String user;
    @Value("${swagger.password}")
    private String password;

    private HttpHeaders headers = getHeaders();
    private JSONObject productJson = createProductJson();

    
    @BeforeEach
    public void setup() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    void getProductNotFound() {
        ResponseEntity<?> response = testRestTemplate
                .withBasicAuth(user, password)
                .getForEntity(BASE_URL + port + PRODUCTS_URL + "/100", String.class);

        assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
    }

    @Test
    void getProductWrongCredential() {
        ResponseEntity<?> response = testRestTemplate
                .withBasicAuth(user, password + "1")
                .getForEntity(BASE_URL + port + PRODUCTS_URL + "/1", String.class);

        assertTrue(response.getStatusCode() == HttpStatus.UNAUTHORIZED);
    }

    @Test
    void getProductUnauthorized() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity(BASE_URL + port + PRODUCTS_URL + "/1",
                String.class);

        assertTrue(response.getStatusCode() == HttpStatus.UNAUTHORIZED);
    }

    @Test
    int createProduct() throws Exception {
        HttpEntity<String> request = new HttpEntity<String>(productJson.toString(), headers);

        ResponseEntity<Product> response = testRestTemplate
                .withBasicAuth(user, password)
                .postForEntity(BASE_URL + port + PRODUCTS_URL, request, Product.class);

        assertTrue(response.getStatusCode() == HttpStatus.CREATED);
        Product product = response.getBody();
        assertNotNull(product);
        assertNotNull(product.getId());
        assertTrue(product.getId() > 0);
        assertCompareProduct(product);
        return product.getId();
    }

    private void assertCompareProduct(Product product) {
        assertEquals(product.getName(), productJson.get("name"));
        assertEquals(product.getSku(), productJson.get("sku"));
        assertEquals(product.getPrice(), productJson.get("price"));
    }

    @Test
    void getProduct() throws Exception {
        int id = createProduct();

        ResponseEntity<Product> response = testRestTemplate
                .withBasicAuth(user, password)
                .getForEntity(BASE_URL + port + PRODUCTS_URL + "/" + id, Product.class);

        assertTrue(response.getStatusCode() == HttpStatus.OK);
        Product product = response.getBody();
        assertNotNull(product);
        assertCompareProduct(product);

    }

    
    @Test
    void getByName() throws Exception {
        createProduct();

        ParameterizedTypeReference<ListResponse> responseType = new ParameterizedTypeReference<ListResponse>() {
        };

        // Realiza la solicitud GET utilizando getForEntity
        ResponseEntity<ListResponse> response = testRestTemplate
                .withBasicAuth(user, password)
                .exchange(BASE_URL + port + PRODUCTS_URL + "/search/byName?name=" + productJson.get("name"), HttpMethod.GET, null, responseType);

        assertTrue(response.getStatusCode() == HttpStatus.OK);
        ListResponse listResponse = response.getBody();

        assertNotNull(listResponse);
        List<Product> list = listResponse._embedded.Productos;
        assertTrue(list.size() >= 1);
        assertCompareProduct(list.get(0));
    }

    @Test
    void updateProduct() throws Exception {
        int id = createProduct();
        JSONObject updated = (JSONObject) productJson.clone();
        updated.put("name", "masapan");
        HttpEntity<String> request = new HttpEntity<String>(updated.toString(), headers);

        ResponseEntity<Product> response = testRestTemplate
                .withBasicAuth(user, password)
                .exchange(BASE_URL + port + PRODUCTS_URL + "/" + id, HttpMethod.PATCH, request, Product.class);

        assertNotNull(response);
        assertTrue(response.getStatusCode() == HttpStatus.OK);

        Product responseProduct = response.getBody();
        assertTrue(responseProduct.getName().compareTo(updated.get("name").toString()) == 0);

    }

    
    @Test
    void deleteProduct() throws Exception {
        int id = createProduct();
        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth(user, password)
                .exchange(BASE_URL + port + PRODUCTS_URL + "/" + id, HttpMethod.DELETE, null, String.class);

        assertNotNull(response);
        assertTrue(response.getStatusCode() == HttpStatus.OK);
    }

    static class Embedded {
        public Embedded() {
        }

        public List<Product> Productos;
    }

    static class ListResponse {
        public Embedded _embedded;

        public ListResponse() {
        }
    }

    @Test
    void listProducts() throws Exception {
        createProduct();
        int id = createProduct();

        ParameterizedTypeReference<ListResponse> responseType = new ParameterizedTypeReference<ListResponse>() {
        };

        // Realiza la solicitud GET utilizando getForEntity
        ResponseEntity<ListResponse> response = testRestTemplate
                .withBasicAuth(user, password)
                .exchange(BASE_URL + port + PRODUCTS_URL, HttpMethod.GET, null, responseType);

        assertTrue(response.getStatusCode() == HttpStatus.OK);
        ListResponse listResponse = response.getBody();

        assertNotNull(listResponse);
        List<Product> list = listResponse._embedded.Productos;
        assertTrue(list.size() >= 2);
        list.stream().filter(p -> p.getId() == id).findFirst().ifPresent(p -> assertCompareProduct(p));

    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private JSONObject createProductJson() {
        JSONObject product = new JSONObject();
        product.put("name", "Mandarina");
        product.put("sku", "AKS332");
        product.put("price", 1234);

        return product;
    }
}