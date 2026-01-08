package com.xalts.bookingservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {

    private Long userId;
    private Long eventId;
    private Long venueId;
    private int numberOfSeats;
    private Double amount;
}
