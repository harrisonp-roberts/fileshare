package dev.hroberts.fileshare.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    final String API_HEADER = "X-API-KEY";
    @Value("${api-key}")
    String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestApiKey = request.getHeader(API_HEADER);
        //todo allow access to view in better way
        filterChain.doFilter(request, response);

//        if (apiKey.equals(requestApiKey) || request.getRequestURI().startsWith("/files")) {
//            filterChain.doFilter(request, response);
//        } else {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API key");
//        }
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return true;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return true;
    }
}
