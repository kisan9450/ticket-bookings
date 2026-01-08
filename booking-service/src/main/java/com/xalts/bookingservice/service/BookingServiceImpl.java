package com.xalts.bookingservice.service;

import com.xalts.bookingservice.dto.BookingRequestDTO;
import com.xalts.bookingservice.dto.BookingResponseDTO;
import com.xalts.bookingservice.dto.NotificationDTO;
import com.xalts.bookingservice.dto.PaymentDTO;
import com.xalts.bookingservice.exception.BookingNotFoundException;
import com.xalts.bookingservice.exception.InvalidBookingException;
import com.xalts.bookingservice.model.Booking;
import com.xalts.bookingservice.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingRepository bookingRepository;

    private final RestTemplate restTemplate;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, RestTemplate restTemplate) {
        this.bookingRepository = bookingRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequest) {

        // Skipping validations for userId, eventId, venueId assuming values will be valid
        // In real application, we should add validator for them

        // Validate number of seats
        if (bookingRequest.getNumberOfSeats() <= 0) {
            throw new InvalidBookingException("Invalid number of seats specified.");
        }

        Booking booking = new Booking();
        booking.setUserId(bookingRequest.getUserId());
        booking.setEventId(bookingRequest.getEventId());
        booking.setVenueId(bookingRequest.getVenueId());
        booking.setNumberOfSeats(bookingRequest.getNumberOfSeats());
        booking.setStatus("PENDING"); // Set initial status to "PENDING"
        booking.setBookingTime(LocalDateTime.now()); // Set booking time to now
        booking.setAmount(bookingRequest.getAmount());
        Booking savedBooking = bookingRepository.save(booking);

        // I am doing synchronous call to payment and notification service
        // It can be done via async approach. We can use Kafka,
        // For simplicity, I'm skipping async approach

        // Prepare payment request
        PaymentDTO paymentResponse = processPayment(bookingRequest, savedBooking);

        // If payment is successful, proceed to send notification
        if ("SUCCESS".equals(paymentResponse.getStatus())) {

            // Send notification, ignore failure
            try {
                sendNotification();
            } catch (Exception e) {
                // Log notification failure but continue with the booking flow
                log.error("Notification sending failed: {}", e.getMessage());
            }


            // Update booking status to "CONFIRMED"
            savedBooking.setStatus("CONFIRMED");
            bookingRepository.save(savedBooking);


        } else {
            // If payment fails, set status to "FAILED" and return
            savedBooking.setStatus("FAILED");
            bookingRepository.save(savedBooking);
            throw new InvalidBookingException("Payment failed. Booking could not be confirmed.");
        }


        return new BookingResponseDTO(
                savedBooking.getId(),
                savedBooking.getUserId(),
                savedBooking.getEventId(),
                savedBooking.getVenueId(),
                savedBooking.getNumberOfSeats(),
                savedBooking.getStatus(),
                savedBooking.getBookingTime(),
                savedBooking.getAmount()
        );
    }

    private  void sendNotification() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setRecipientEmail("user@example.com");  // You can get this from the user details
        notificationDTO.setSubject("Booking Confirmation");
        notificationDTO.setMessage("Your booking is confirmed and payment was successful.");

        // Call Notification Service to send confirmation
        String notificationUrl = "http://localhost:8080/notifications/send";  // Notification Service URL
        restTemplate.postForObject(notificationUrl, notificationDTO, String.class);
    }

    private  PaymentDTO processPayment(BookingRequestDTO bookingRequest, Booking savedBooking) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setBookingId(savedBooking.getId());
        paymentDTO.setAmount(bookingRequest.getAmount());  // Assuming bookingRequest contains amount
        paymentDTO.setTransactionId("txn-" + savedBooking.getId());

        // Call Payment Service to process payment
        String paymentUrl = "http://localhost:8085/payments/process";  // Payment Service URL
        return restTemplate.postForObject(paymentUrl, paymentDTO, PaymentDTO.class);
    }


    @Override
    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        return new BookingResponseDTO(
                booking.getId(),
                booking.getUserId(),
                booking.getEventId(),
                booking.getVenueId(),
                booking.getNumberOfSeats(),
                booking.getStatus(),
                booking.getBookingTime(),
                booking.getAmount()
        );
    }



    @Override
    public List<BookingResponseDTO> getBookingsByUserId(Long userId) {
        List<Booking> bookings = bookingRepository.findBookingsByUserId(userId);

        if (bookings.isEmpty()) {
            throw new BookingNotFoundException("No bookings found for this user.");
        }

        return bookings.stream().map(booking -> new BookingResponseDTO(
                booking.getId(),
                booking.getUserId(),
                booking.getEventId(),
                booking.getVenueId(),
                booking.getNumberOfSeats(),
                booking.getStatus(),
                booking.getBookingTime(),
                booking.getAmount()
        )).collect(Collectors.toList());
    }



    @Override
    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        // Update status to CANCELLED
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }
}
