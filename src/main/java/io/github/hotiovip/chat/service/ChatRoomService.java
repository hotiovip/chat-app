package io.github.hotiovip.chat.service;

import io.github.hotiovip.chat.entity.ChatRoom;
import io.github.hotiovip.chat.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public List<ChatRoom> getUserChats(String username) {
        return chatRoomRepository.findChatRoomsByUsername(username);
    }

    public Optional<ChatRoom> getChatRoom(Long roomId) {
        return chatRoomRepository.findById(roomId);
    }
}
