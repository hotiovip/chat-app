package io.github.hotiovip.chat.controller;

import io.github.hotiovip.chat.entity.ChatRoom;
import io.github.hotiovip.chat.entity.Message;
import io.github.hotiovip.chat.entity.UserEntity;
import io.github.hotiovip.chat.repository.ChatRoomRepository;
import io.github.hotiovip.chat.repository.MessageRepository;
import io.github.hotiovip.chat.repository.UserRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller
public class ChatWebSocketController {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ChatWebSocketController(MessageRepository messageRepository,
                                   ChatRoomRepository chatRoomRepository,
                                   UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Map<String, Object> handleMessage(@DestinationVariable Long roomId,
                                             Map<String, String> payload,
                                             Principal principal) {

        String content = payload.get("content");

        String username = principal.getName();

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));

        UserEntity sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Message message = new Message();
        message.setContent(content);
        message.setChatRoom(chatRoom);
        message.setSender(sender);
        messageRepository.save(message);

        return Map.of(
                "sender", sender.getUsername(),
                "content", content
        );
    }
}