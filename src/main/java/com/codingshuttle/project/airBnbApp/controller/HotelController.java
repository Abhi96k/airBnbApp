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

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDto> updateHotel(@PathVariable Long hotelId, @RequestBody HotelDto hotelDto){
        log.info("Update Hotel Request Received: id={}, data={}", hotelId, hotelDto);
        HotelDto updatedHotel = hotelService.updateHotel(hotelId, hotelDto);
        log.info("Hotel Updated Successfully: {}", updatedHotel);
        return new ResponseEntity<>(updatedHotel, HttpStatus.OK);
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long hotelId) {
        log.info("Delete Hotel Request Received: {}", hotelId);
        hotelService.deleteHotelById(hotelId);
        log.info("Hotel Deleted Successfully: id={}", hotelId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{hotelId}/activate")
    public ResponseEntity<Void> activateHotel(@PathVariable Long hotelId) {
        log.info("Activate Hotel Request Received: {}", hotelId);
        hotelService.activateHotel(hotelId);
        log.info("Hotel Activated Successfully: id={}", hotelId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
