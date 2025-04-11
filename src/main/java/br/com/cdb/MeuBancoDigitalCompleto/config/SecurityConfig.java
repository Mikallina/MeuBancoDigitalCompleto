//package br.com.cdb.MeuBancoDigitalCompleto.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//	
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .requestMatchers("/user/**").hasRole("USER")
//                //.antMatchers("/conta/**", "/cliente/**").permitAll()
//                .anyRequest().authenticated()
//            )
//            .formLogin(form -> form
//                .loginPage("/login")
//                .defaultSuccessUrl("http://localhost:8080/menuprincipal/menuprincipal", true)
//                .permitAll()
//            )
//            .logout(logout -> logout
//                .permitAll()
//            );
//        return http.build();
//    }
//    
//	@Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user1 = User.builder()
//            .username("usuario")
//            .password(passwordEncoder().encode("senha"))
//            .roles("USER")
//            .build();
//
//        UserDetails admin = User.builder()
//            .username("admin")
//            .password(passwordEncoder().encode("admin123"))
//            .roles("ADMIN")
//            .build();
//
//        return new InMemoryUserDetailsManager(user1, admin);
//    }
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}