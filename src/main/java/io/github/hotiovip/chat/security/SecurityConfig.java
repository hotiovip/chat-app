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
                // Allow iframe frames for SockJS transport options
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )

                .authorizeHttpRequests(auth -> auth
                        // Allow access to static assets (CSS, JS) if you move them to external files
                        .requestMatchers("/css/**", "/js/**", "/ws-chat/**").permitAll()
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