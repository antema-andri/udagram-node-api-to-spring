package com.udagram.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsServiceImpl userDetailDtailServiceImpl;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailDtailServiceImpl).passwordEncoder(passwordEncoder()); //
	}

	@Override
    protected void configure(HttpSecurity http) throws Exception {
		JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(authenticationManager());
		
		http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/users/auth/**").permitAll();
        http.authorizeRequests().antMatchers("/users/auth/login/**").permitAll();
        http.authorizeRequests().antMatchers("/feed/**").permitAll();
        http.authorizeRequests().antMatchers("/upload/storage/**").permitAll();
        http.authorizeRequests().antMatchers("/image/**").permitAll();
        http.authorizeRequests().antMatchers("/user/**").hasAnyAuthority(Constant.ROLE_LIST.get(0));
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(jwtAuthFilter);
        http.addFilterBefore(new JwtVerifierFilter(), UsernamePasswordAuthenticationFilter.class);
    }
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
}
