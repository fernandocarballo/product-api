package ar.fcarballo.product_api.configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SwaggerBasicAuthFilter extends OncePerRequestFilter {
    
    @Value("${swagger.username}")
	private String swaggerUsername;
    @Value("${swagger.password}")
	private String swaggerPassword;

    Logger logger= LoggerFactory.getLogger(this.getClass());
    
    /* 
     * This method is called by spring framework when the filter is applied
     * It's useful to check if the request is authorized or not
    */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        if (isRestrictedUrl(requestUri)) {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Basic ")) {
                String[] credentials = extractAndDecodeHeader(authHeader);

                if (isAuthorized(credentials[0], credentials[1])) {
                    chain.doFilter(request, response);
                    return;
                }
            }

            response.setHeader("WWW-Authenticate", "Basic realm=\"Swagger\"");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(request, response);
    }

    /* 
     * This method is called by spring framework to check if the filter is applied
     * It will allow to access the swagger ui and the swagger documentation
    */
    private boolean isRestrictedUrl(String requestUri) {
        return !(requestUri.startsWith("/swagger") || requestUri.startsWith("/api-docs") || requestUri.startsWith("/swagger-ui.html"));
    }

    /* 
     * Check if the user is authorized or not against the credentials configured on config file
    */
    private boolean isAuthorized(String username, String password) {
        logger.info(swaggerUsername+":"+swaggerPassword);
        return username.equals(swaggerUsername) && password.equals(swaggerPassword);
    }

    /* 
     * Extract the username and password from the header
    */
    private String[] extractAndDecodeHeader(String header) throws IOException {
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, StandardCharsets.UTF_8);

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new RuntimeException("Invalid basic authentication token");
        }

        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }
}