//package com.smart.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.SecurityFilterChain;
//
//
//
//@Configuration
//@EnableMethodSecurity
//public class Config 
//{	
//
//
//    @Bean
//    UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder)
//	{
//		//UserDetails normalUser=User.withUsername("Salman1234").password(bCryptPasswordEncoder().encode("pass")).roles("USER").build();
//		//UserDetails adminUser=User.withUsername("Salman123").password(bCryptPasswordEncoder().encode("passs")).roles("ADMIN").build();
//		//return new InMemoryUserDetailsManager(normalUser,adminUser);
//    	return new UserDetailsServiceImpl();
//	}
//
//    @Bean
//    DefaultSecurityFilterChain chain(HttpSecurity httpSecurity) throws Exception
//	{
//    	
//    	httpSecurity
//		.authorizeHttpRequests((requests) -> requests
//			.requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/user/**").hasRole("User")
//			.anyRequest().authenticated()
//		)
//		.formLogin((form) -> form
//			.loginPage("/signin")
//			.permitAll()
//		)
//		.logout((logout) -> logout.permitAll());
//
//return httpSecurity.build();
//    	
////        return httpSecurity.csrf(csrf -> csrf.disable()).authorizeHttpRequests(requests -> requests
////                .requestMatchers("/admin/**").hasRole("ADMIN")
////                .requestMatchers("/user/**").hasRole("USER")
////                .requestMatchers("/**")
////                .permitAll()).formLogin().and().build();

////    	return httpSecurity.csrf(csrf -> csrf.disable())
////                .authorizeHttpRequests(requests -> requests.requestMatchers("/auth/welcome").permitAll())
////                .authorizeHttpRequests(requests -> requests.requestMatchers("/auth/user/**").authenticated())
////                .authorizeHttpRequests(requests -> requests.requestMatchers("/auth/admin/**").authenticated()).formLogin(login -> login.build());
////		
//        
//	}
//
//
//	
////Below are the code old spring security after spring 3 version we need to use above logic
//	@Bean
//	public UserDetailsService detailsService()
//	{
//		return new UserDetailsServiceImpl();
//	}	
//	@Bean
//public BCryptPasswordEncoder bCryptPasswordEncoder()
//	{
//		return new BCryptPasswordEncoder();
//	}
//	
//////	@Bean
//////	public DaoAuthenticationProvider authenticationProvider()
//////	{
//////		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//////		daoAuthenticationProvider.setUserDetailsService(this.userDetailsService());
//////		daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
//////		return daoAuthenticationProvider;
//////	}
////////
//////	protected void configure(AuthenticationManagerBuilder auth) throws Exception
//////	{
//////		auth.authenticationProvider(authenticationProvider());
//////	}
////////	protected void configure(HttpSecurity http) throws Exception
//////	{
//////		http.authorizeRequests().requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/user/**").hasRole("User").
//////		requestMatchers("/**").permitAll().and().formLogin().and().csrf().disable();
//	
//}
