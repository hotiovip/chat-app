package io.github.hotiovip.chat.controller;

import io.github.hotiovip.chat.entity.ChatRoom;
import io.github.hotiovip.chat.entity.Message;
import io.github.hotiovip.chat.service.ChatRoomService;
import io.github.hotiovip.chat.service.MessageService;
import io.github.hotiovip.chat.service.UserEntityService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class MainController {

    private final Logger logger = Logger.getLogger(MainController.class.getName());

    private final UserEntityService userEntityService;
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    public MainController(ChatRoomService chatRoomService, UserEntityService userEntityService, MessageService messageService) {
        this.chatRoomService = chatRoomService;
        this.userEntityService = userEntityService;
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String index(Model model,
                        @AuthenticationPrincipal UserDetails currentUser,
                        @RequestParam(required = false) Long roomId) {

        // Load chat rooms
        List<ChatRoom> userRooms = userEntityService.getUserChats(currentUser.getUsername());
        model.addAttribute("rooms", userRooms);

        ChatRoom activeRoom = null;
        List<Message> roomMessages = List.of();

        if (roomId != null) {
            // Find the room the user clicked on
            activeRoom = chatRoomService.getChatRoom(roomId).orElse(null);
        } else if (!userRooms.isEmpty()) {
            // Default behavior: Select the first available room automatically
            activeRoom = userRooms.getFirst();
        }

        // If an active room exists, fetch its history sorted by time
        if (activeRoom != null) {
            roomMessages = messageService.getMessages(activeRoom.getId());
        }

        // Show data on the page
        model.addAttribute("activeRoom", activeRoom);
        model.addAttribute("messages", roomMessages);

        return "index";
    }
}