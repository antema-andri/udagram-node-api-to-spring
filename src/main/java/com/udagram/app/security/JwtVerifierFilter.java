package com.udagram.app.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtVerifierFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE, PATCH");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,authorization");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, authorization");
		
        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else if(request.getRequestURI().equals("/users/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        else {
            verifier(request, response, filterChain);
        }
	}
	
	public void verifier(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		String jwtToken = request.getHeader(Constant.JWT_HEADER_NAME);
        System.out.println("Token="+jwtToken);
        if (jwtToken == null || !jwtToken.startsWith(Constant.HEADER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims = Jwts.parser()
        		.setSigningKey(Constant.SECRET)
        		.parseClaimsJws(jwtToken.replace(Constant.HEADER_PREFIX, ""))
        		.getBody();
        
        String username = claims.getSubject();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Constant.ROLE_LIST.forEach(rn -> {
            authorities.add(new SimpleGrantedAuthority(rn));
        });
        
        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(user);
        filterChain.doFilter(request, response);
	}

}
