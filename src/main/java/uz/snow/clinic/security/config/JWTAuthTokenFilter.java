/*
package uz.snow.clinic.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.snow.clinic.security.service.UserDetailsServiceImpl;

import java.io.IOException;

@Slf4j

public class JWTAuthTokenFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProwider;
        private final UserDetailsServiceImpl userDetailsService;

    public JWTAuthTokenFilter(JwtProvider jwtProwider, UserDetailsServiceImpl userDetailsService) {
        this.jwtProwider = jwtProwider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    }
}
*/
