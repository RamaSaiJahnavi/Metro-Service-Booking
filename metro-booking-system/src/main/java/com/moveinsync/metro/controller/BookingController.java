package com.moveinsync.metro.controller;

import com.moveinsync.metro.dto.BookingRequestDTO;
import com.moveinsync.metro.dto.BookingResponseDTO;
import com.moveinsync.metro.dto.TravelStatusRequestDTO;
import com.moveinsync.metro.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Create a new booking
     */
    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Valid @RequestBody BookingRequestDTO request,
            Authentication authentication) {

        BookingResponseDTO response = bookingService.createBooking(request, authentication.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * Get booking by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBooking(
            @PathVariable Long id,
            Authentication authentication) {

        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        BookingResponseDTO response = bookingService.getBookingResponseByIdForUser(id, authentication.getName(), admin);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<BookingResponseDTO>> myBookings(Authentication authentication) {
        return ResponseEntity.ok(bookingService.getBookingsForUser(authentication.getName()));
    }

    @PutMapping("/{id}/travelled")
    public ResponseEntity<BookingResponseDTO> updateTravelStatus(
            @PathVariable Long id,
            @RequestBody TravelStatusRequestDTO request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                bookingService.updateTravelStatusForUser(id, authentication.getName(), request.isTravelled())
        );
    }
}
