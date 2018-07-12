package org.uatransport.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import org.uatransport.entity.Role;
import org.uatransport.exception.SecurityJwtException;
import org.uatransport.repository.UserRepository;
import org.uatransport.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

     String accessToken = jwtTokenProvider.resolveAccessToken(request);
     String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        String userName=jwtTokenProvider.getUsername(refreshToken);
        ///removeAlreadyFiltredAttributes
        Role role = userRepository.findByEmail(userName).getRole();
        Integer id = userRepository.findByEmail(userName).getId();
     try {
         if(accessToken!=null&& jwtTokenProvider.validateToken(accessToken)){
             Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
             SecurityContextHolder.getContext().setAuthentication(auth);
         }
         else if(refreshToken!=null&&jwtTokenProvider.validateToken(refreshToken)&&!jwtTokenProvider.validateToken(accessToken)){
             //checkExpiration(accessToken)
             response.addHeader("Access-token",jwtTokenProvider.createAccessToken(userName, role,id ));
             response.addHeader("Refresh-token", jwtTokenProvider.createRefreshToken(userName));
         }
     }catch (SecurityJwtException e) {
         throw new SecurityJwtException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
     }
        filterChain.doFilter(request, response);
    }
}
