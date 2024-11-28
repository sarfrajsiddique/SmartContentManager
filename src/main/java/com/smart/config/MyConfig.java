package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class MyConfig {
	
	

	@Bean 
	DefaultSecurityFilterChain chain(HttpSecurity httpSecurity) throws Exception 
	{

        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                		.requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER").requestMatchers("/**").permitAll().anyRequest().authenticated())
                		.formLogin((form) -> form
                		.loginPage("/login")
                		.loginProcessingUrl("/dologin")
                		.defaultSuccessUrl("/userindex")
                		.permitAll());
		return httpSecurity.build();
	}
	
	@Bean
	UserDetailsService detailsService()
	{
		return new UserDetailsServiceImpl();
	}	
	
	@Bean
    BCryptPasswordEncoder bCryptPasswordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

}
