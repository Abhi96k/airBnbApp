package com.codingshuttle.project.airBnbApp.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    private Long hotelId;
    private Long roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer roomsCount;

}
