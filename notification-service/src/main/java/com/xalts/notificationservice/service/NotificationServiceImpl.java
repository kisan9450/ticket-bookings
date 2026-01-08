package com.xalts.notificationservice.service;

import com.xalts.notificationservice.dto.NotificationDTO;
import com.xalts.notificationservice.model.Notification;
import com.xalts.notificationservice.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepository;

    @Override
    public void sendNotification(NotificationDTO notificationDTO) {
        try {
            // Create and send an email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(notificationDTO.getRecipientEmail());
            helper.setSubject(notificationDTO.getSubject());
            helper.setText(notificationDTO.getMessage(), true);
            mailSender.send(message);

            // Save notification to the database
            Notification notification = Notification.builder()
                    .recipientEmail(notificationDTO.getRecipientEmail())
                    .subject(notificationDTO.getSubject())
                    .message(notificationDTO.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            notificationRepository.save(notification);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
