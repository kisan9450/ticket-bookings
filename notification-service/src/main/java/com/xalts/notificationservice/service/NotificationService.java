package com.xalts.notificationservice.service;

import com.xalts.notificationservice.dto.NotificationDTO;

public interface NotificationService {
    void sendNotification(NotificationDTO notificationDTO);
}
