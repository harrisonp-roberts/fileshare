package dev.hroberts.fileshare.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthenticationFilter extends OncePerRequestFilter {
    final String API_HEADER = "X-API-KEY";
    @Value("${api-key}")
    String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestApiKey = request.getHeader(API_HEADER);
        if (apiKey.equals(requestApiKey) || apiKey.equals("dev")) {
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(request, response);

            //   response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API key");
        }
    }
}
