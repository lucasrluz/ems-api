package com.emsapi.config;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.emsapi.models.CompanyModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.services.JwtService;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private CompanyRepository companyRepository;

    public SecurityFilter(JwtService jwtService, CompanyRepository companyRepository) {
        this.jwtService = jwtService;
        this.companyRepository = companyRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getToken(request);

        if (jwt != null) {
            String companyId = this.jwtService.validateJwt(jwt);

            Optional<CompanyModel> findCompanyModelResponse = this.companyRepository.findById(UUID.fromString(companyId));

            if (findCompanyModelResponse.isEmpty()) {
                response.setStatus(403);
                response.getWriter().write("JWT invalid");
                response.getWriter().flush();
                
                return;
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(companyId, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            return null;
        }

        return authHeader.replace("Bearer ", "");
    }
}
