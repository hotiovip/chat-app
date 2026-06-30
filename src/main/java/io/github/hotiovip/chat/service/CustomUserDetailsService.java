package io.github.hotiovip.chat.service;

import io.github.hotiovip.chat.entity.UserEntity;
import io.github.hotiovip.chat.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final Logger logger = Logger.getLogger(CustomUserDetailsService.class.getName());

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);

        if (userEntity.isPresent()) {
            return User.builder()
                    .username(userEntity.get().getUsername())
                    .password(userEntity.get().getPassword())
                    .roles("USER")
                    .build();
        } else {
            logger.log(Level.SEVERE, "Username not found: " + username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
