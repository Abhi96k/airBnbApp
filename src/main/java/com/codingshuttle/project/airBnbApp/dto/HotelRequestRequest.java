package com.codingshuttle.project.airBnbApp.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HotelRequestRequest {
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer roomCount;

    private Integer page=0;
    private Integer size=10;
}
