package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.dto.BookingDto;
import com.codingshuttle.project.airBnbApp.dto.BookingRequest;
import com.codingshuttle.project.airBnbApp.dto.GuestDto;
import com.codingshuttle.project.airBnbApp.entity.Booking;
import com.codingshuttle.project.airBnbApp.entity.Guest;
import com.codingshuttle.project.airBnbApp.entity.Hotel;
import com.codingshuttle.project.airBnbApp.entity.Inventory;
import com.codingshuttle.project.airBnbApp.entity.Room;
import com.codingshuttle.project.airBnbApp.entity.enums.BookingStatus;
import com.codingshuttle.project.airBnbApp.repository.BookingRepository;
import com.codingshuttle.project.airBnbApp.repository.HotelRepository;
import com.codingshuttle.project.airBnbApp.repository.InventoryRepository;
import com.codingshuttle.project.airBnbApp.repository.GuestRepository;
import com.codingshuttle.project.airBnbApp.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookingDto initialiseBooking(BookingRequest bookingRequest) {

        if (bookingRequest.getRoomsCount() == null || bookingRequest.getRoomsCount() <= 0) {
            throw new RuntimeException("Rooms count must be greater than zero");
        }

        if (bookingRequest.getStartDate() == null || bookingRequest.getEndDate() == null) {
            throw new RuntimeException("Start and end date are required");
        }

        if (!bookingRequest.getEndDate().isAfter(bookingRequest.getStartDate())) {
            throw new RuntimeException("End date must be after start date");
        }

        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        Room room = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!room.getHotel().getId().equals(hotel.getId())) {
            throw new RuntimeException("Room does not belong to the provided hotel");
        }

        List<Inventory> inventoryList =
                inventoryRepository.findAndLockAvailableInventoryByRoom(
                        room.getId(),
                        bookingRequest.getStartDate(),
                        bookingRequest.getEndDate(),
                        bookingRequest.getRoomsCount()
                );

        long expectedNights = ChronoUnit.DAYS.between(
                bookingRequest.getStartDate(),
                bookingRequest.getEndDate()
        );

        if (inventoryList.size() != expectedNights) {
            throw new RuntimeException("Insufficient inventory for selected dates");
        }

        // extra validation aligned with query (respects reserved + booked)
        for (Inventory inventory : inventoryList) {
            int available = inventory.getTotalCount() - inventory.getBookedCount() - inventory.getReservedCount();
            if (available < bookingRequest.getRoomsCount()) {
                throw new RuntimeException("Not enough rooms available");
            }
        }

        // reserve (track in reservedCount; bookedCount stays for confirmed bookings)
        for (Inventory inventory : inventoryList) {
            inventory.setReservedCount(
                    inventory.getReservedCount() + bookingRequest.getRoomsCount()
            );
        }

        inventoryRepository.saveAll(inventoryList);

        Booking booking = Booking.builder()
                .hotel(hotel)
                .room(room)
                .user(null) // replace with actual user
                .roomsCount(bookingRequest.getRoomsCount())
                .checkInDate(bookingRequest.getStartDate())
                .checkOutDate(bookingRequest.getEndDate())
                .bookingStatus(BookingStatus.RESERVED)
                .build();

        booking = bookingRepository.save(booking);

        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    public BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList) {
        log.info("Adding guests for booking {}", bookingId);

        if (guestDtoList == null || guestDtoList.isEmpty()) {
            throw new RuntimeException("Guest list cannot be empty");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        final Booking bookingRef = booking;

        if (booking.getUser() == null) {
            throw new RuntimeException("Booking has no associated user to attach guests");
        }

        Set<Guest> guests = new HashSet<>(guestRepository.saveAll(
                guestDtoList.stream().map(guestDto -> {
                    Guest guest = new Guest();
                    guest.setName(guestDto.getName());
                    guest.setGender(guestDto.getGender());
                    guest.setAge(guestDto.getAge());
                    // Use provided user if present; otherwise fall back to booking user
                    guest.setUser(guestDto.getUser() != null ? guestDto.getUser() : bookingRef.getUser());
                    return guest;
                }).toList()
        ));

        Set<Guest> existingGuests = booking.getGuests() != null
                ? new HashSet<>(booking.getGuests())
                : new HashSet<>();
        existingGuests.addAll(guests);
        booking.setGuests(existingGuests);
        booking.setBookingStatus(BookingStatus.GUEST_CHECKED_ADDED);

        Booking savedBooking = bookingRepository.save(booking);
        return modelMapper.map(savedBooking, BookingDto.class);
    }
}