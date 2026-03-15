package com.codingshuttle.project.airBnbApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoomDto {

    private Long id;

    @NotBlank(message = "Room type is required")
    private String type;

    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private BigDecimal basePrice;

    private List<String> photos;
    private List<String> amenities;

    @NotNull(message = "Total count is required")
    @Positive(message = "Total count must be positive")
    private Integer totalCount;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be positive")
    private Integer capacity;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
