package com.xalts.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDTO {

    private String recipientEmail;
    private String subject;
    private String message;

}
