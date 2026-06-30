package io.github.hotiovip.chat.service;

import io.github.hotiovip.chat.entity.ChatRoom;
import io.github.hotiovip.chat.entity.UserEntity;
import io.github.hotiovip.chat.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserEntityService {

    private final UserRepository userRepository;

    public UserEntityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<ChatRoom> getUserChats(String username) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
        if (optionalUserEntity.isPresent()) {
            return optionalUserEntity.get().getChatRooms();
        }
        else return new ArrayList<>();
    }
}
