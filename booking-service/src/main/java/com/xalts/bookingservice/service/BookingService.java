package com.xalts.bookingservice.service;

import com.xalts.bookingservice.dto.BookingRequestDTO;
import com.xalts.bookingservice.dto.BookingResponseDTO;

import java.util.List;

public interface BookingService {

    BookingResponseDTO createBooking(BookingRequestDTO bookingRequest);

    BookingResponseDTO getBookingById(Long id);

    List<BookingResponseDTO> getBookingsByUserId(Long userId);

    void cancelBooking(Long id);
}
