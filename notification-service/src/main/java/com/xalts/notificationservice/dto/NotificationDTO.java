package com.xalts.notificationservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private String recipientEmail;
    private String subject;
    private String message;
    private LocalDateTime timestamp;
}
