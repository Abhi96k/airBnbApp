package com.codingshuttle.project.airBnbApp.controller;
import com.codingshuttle.project.airBnbApp.dto.HotelDto;
import com.codingshuttle.project.airBnbApp.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
     public ResponseEntity<HotelDto> CreatNewHotel(@RequestBody HotelDto hotelDto){
        log.info("Create Hotel Request Received: {}", hotelDto);
        HotelDto createdHotel = hotelService.CreateNewHotel(hotelDto);
        log.info("Hotel Created Successfully: {}", createdHotel);
        return new ResponseEntity<>(createdHotel, HttpStatus.CREATED);
    }
    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDto>getHotelById(@PathVariable Long hotelId){
        log.info("Get Hotel Request Received: {}", hotelId);
        HotelDto hotelDto = hotelService.getHotelById(hotelId);
        log.info("Hotel Retrieved Successfully: {}", hotelDto);
        return new ResponseEntity<>(hotelDto, HttpStatus.OK);
    }
}
