package de.rieck.todo.todobackend.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String token = jwtTokenProvider.resolveTokenFromRequest(httpServletRequest);
        try {
            if (token != null) {
                Authentication auth = jwtTokenProvider.getValidatedAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Throwable ex) {
            System.out.println(ex);
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
