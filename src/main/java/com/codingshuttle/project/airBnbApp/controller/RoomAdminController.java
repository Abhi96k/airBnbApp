package com.codingshuttle.project.airBnbApp.controller;

import com.codingshuttle.project.airBnbApp.dto.RoomDto;
import com.codingshuttle.project.airBnbApp.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@Slf4j
@RequiredArgsConstructor
public class RoomAdminController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDto> createRoom(@PathVariable Long hotelId, @RequestBody RoomDto roomDto){
        log.info("Create Room Request Received: hotelId={}, roomDto={}", hotelId, roomDto);
        RoomDto createdRoom = roomService.createRoom(hotelId, roomDto);
        log.info("Room Created Successfully: {}", createdRoom);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRooms(@PathVariable Long hotelId){
        log.info("Get All Rooms Request Received: hotelId={}", hotelId);
        List<RoomDto> rooms = roomService.getRooms(hotelId);
        log.info("Rooms Retrieved Successfully: count={}", rooms.size());
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
        log.info("Get Room By Id Request Received: hotelId={}, roomId={}", hotelId, roomId);
        RoomDto roomDto = roomService.getRoomById(roomId);
        log.info("Room Retrieved Successfully: {}", roomDto);
        return new ResponseEntity<>(roomDto, HttpStatus.OK);
    }
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
        log.info("Delete Room Request Received: hotelId={}, roomId={}", hotelId, roomId);
        roomService.deleteRoomById(roomId);
        log.info("Room Deleted Successfully: roomId={}", roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
