package com.codingshuttle.project.airBnbApp.dto;

import com.codingshuttle.project.airBnbApp.entity.HotelContactInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class HotelDto {

    @NotBlank(message = "Hotel name is required")
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    private List<String> photos;
    private List<String> amenities;

    @NotNull(message = "Contact info is required")
    private HotelContactInfo contactInfo;

    private Boolean active;
}
