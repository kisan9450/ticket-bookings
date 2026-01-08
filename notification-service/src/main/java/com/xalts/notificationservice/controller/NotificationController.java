package com.xalts.notificationservice.controller;

import com.xalts.notificationservice.dto.NotificationDTO;
import com.xalts.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    // If we want event dirven architecture, we can implement kafka queue and consume events here to send email async way
    // We can also send notifications on whatsapp as well if want
    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationDTO notificationDTO) {
        notificationService.sendNotification(notificationDTO);
        return ResponseEntity.ok("Notification sent successfully.");
    }
}
