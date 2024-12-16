package com.home.simpleWebApp.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.home.simpleWebApp.config.filter.JWTFilter;
import com.home.simpleWebApp.model.UserRole;
import com.home.simpleWebApp.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity		// Ignore default security configuration and follow this configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	@Autowired
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	
	@Autowired
	private CustomAccessDeniedHandler customAccessDeniedHandler;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private JWTFilter jwtFilter;
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		return http	
			.cors(cors -> cors.configurationSource(request -> {
			        CorsConfiguration configuration = new CorsConfiguration();
			        configuration.setAllowedOrigins(Arrays.asList(
		        		"http://localhost:4200", 
		        		"http://192.168.43.164:4200", 
		        		"http://192.168.0.3:4200", 
		        		"http://192.168.43.1:4200")
	        		);
			        configuration.setAllowedMethods(Arrays.asList("*"));
			        configuration.setAllowedHeaders(Arrays.asList("*"));
			        return configuration;
		    }))
			.csrf(customizer -> customizer.disable())		// Disabling `csrf` (so we need to remove session!)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(request -> {
				request
					.requestMatchers(
						"/api/v1/customers/register", 
						"/api/v1/customers/refresh",
						"/api/v1/admin/refresh",
						"/api/v1/customers/login", 
						"/api/v1/admin/login"
					).permitAll()
					.requestMatchers("/api/v1/admin/**").hasRole(UserRole.ADMIN.name())
					.requestMatchers(HttpMethod.GET, "/api/v1/customers").hasRole(UserRole.ADMIN.name())
					.requestMatchers(HttpMethod.GET, "/api/v1/bookings").hasRole(UserRole.ADMIN.name())
					.requestMatchers(HttpMethod.POST, "/api/v1/bookings").hasRole(UserRole.CUSTOMER.name())
					.requestMatchers("/api/v1/customers/**").hasRole(UserRole.CUSTOMER.name())
					.anyRequest().authenticated();
			})
			.httpBasic(Customizer.withDefaults())	// for api-login
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)	// args -> (filterToBeApplies, BeforeThisFilter)
			.exceptionHandling((exceptionHandling) ->
				exceptionHandling
					.authenticationEntryPoint(customAuthenticationEntryPoint)
					.accessDeniedHandler(customAccessDeniedHandler)
			)
			.build();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
