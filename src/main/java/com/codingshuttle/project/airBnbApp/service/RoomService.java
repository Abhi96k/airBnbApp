package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.dto.RoomDto;

import java.util.List;

public interface RoomService {
    RoomDto createRoom(RoomDto roomDto);

    List<RoomDto> getRooms();

    RoomDto getRoomById(Long roomId);

    void deleteRoomById(Long roomId);


}
