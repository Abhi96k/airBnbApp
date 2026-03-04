package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.dto.RoomDto;

import java.util.List;

public interface RoomService {
    RoomDto createRoom(Long hotelId, RoomDto roomDto);

    List<RoomDto> getRooms(Long hotelId);

    RoomDto getRoomById(Long roomId);

    void deleteRoomById(Long roomId);


}
