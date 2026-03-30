package com.codingshuttle.project.airBnbApp.dto;

import com.codingshuttle.project.airBnbApp.entity.*;
import com.codingshuttle.project.airBnbApp.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;


@Data
public class BookingDto {
    private Long id;
    private Hotel hotel;
    private Room room;
    private User user;
    private Integer roomsCount;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Payment payment;
    private BookingStatus bookingStatus;
    private Set<GuestDto> guests;
}
