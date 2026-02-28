package com.codingshuttle.project.airBnbApp.dto;

import com.codingshuttle.project.airBnbApp.entity.HotelContactInfo;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
public class HotelDto {
    private Long id;
    private String name;
    private String city;
    private List<String> photos;
    private List<String> amenities;
    private HotelContactInfo contactInfo;
    private Boolean active;

}
