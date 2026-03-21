package com.codingshuttle.project.airBnbApp.controller;
import com.codingshuttle.project.airBnbApp.dto.HotelDto;
import com.codingshuttle.project.airBnbApp.dto.HotelInfoDto;
import com.codingshuttle.project.airBnbApp.dto.HotelRequestRequest;
import com.codingshuttle.project.airBnbApp.service.HotelService;
import com.codingshuttle.project.airBnbApp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {

    private final InventoryService  inventoryService;
    private final HotelService hotelService;

    @PostMapping("/search")
    public ResponseEntity<Page<HotelDto>> searchHotels(@RequestBody HotelRequestRequest hotelRequestRequest) {
       Page<HotelDto> hotels = inventoryService.searchHotels(hotelRequestRequest);
       return ResponseEntity.ok(hotels);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelinfo(@PathVariable Long hotelId) {
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }
}
