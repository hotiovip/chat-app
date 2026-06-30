package io.github.hotiovip.chat.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // If you are using plain HTML instead of Thymeleaf, keep this disabled:
                //.csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // VERY IMPORTANT: Allow access to static assets (CSS, JS) if you move them to external files
                        .requestMatchers("/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // Tell Spring Security where your custom login page is
                        .loginPage("/login")
                        // Where to go after a successful login
                        .defaultSuccessUrl("/", true)
                        // Allow everyone to view the login page
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}