package io.github.hotiovip.chat.controller;

import io.github.hotiovip.chat.entity.ChatRoom;
import io.github.hotiovip.chat.entity.Message;
import io.github.hotiovip.chat.entity.UserEntity;
import io.github.hotiovip.chat.repository.ChatRoomRepository;
import io.github.hotiovip.chat.repository.MessageRepository;
import io.github.hotiovip.chat.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public MessageController(MessageRepository messageRepository,
                             ChatRoomRepository chatRoomRepository,
                             UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/messages/send")
    public String sendMessage(@RequestParam String content,
                              @RequestParam Long roomId,
                              @AuthenticationPrincipal UserDetails currentUser) {

        // 1. Find the active room
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));

        // 2. Find the authenticated sender
        UserEntity sender = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 3. Build and save the message object
        Message message = new Message();
        message.setContent(content);
        message.setChatRoom(chatRoom);
        message.setSender(sender);

        messageRepository.save(message);

        // 4. Redirect back to the active chat room view to display the new message!
        return "redirect:/?roomId=" + roomId;
    }
}