package com.xalts.bookingservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {

    private Long id;
    private Long userId;
    private Long eventId;
    private Long venueId;
    private int numberOfSeats;
    private String status;
    private LocalDateTime bookingTime;
    private Double amount;
}
