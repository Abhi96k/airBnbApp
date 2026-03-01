package com.codingshuttle.project.airBnbApp.service;
import com.codingshuttle.project.airBnbApp.dto.RoomDto;
import com.codingshuttle.project.airBnbApp.entity.Room;
import com.codingshuttle.project.airBnbApp.exception.ResourceNotFoundException;
import com.codingshuttle.project.airBnbApp.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    @Override
    public RoomDto createRoom(RoomDto roomDto) {
        log.info("Creating room with room id {}", roomDto.getId());
        // Here you would typically convert the RoomDto to a Room entity, save it using the repository, and then convert it back to a RoomDto to return.
        Room newRoom=modelMapper.map(roomDto, Room.class);
        Room savedRoom=roomRepository.save(newRoom);
        RoomDto roomDtoResponse=modelMapper.map(savedRoom, RoomDto.class);
        log.info("Room created successfully with id: {}", savedRoom.getId());
        return roomDtoResponse;
    }

    @Override
    public List<RoomDto> getRooms() {
       log.info("Retrieving all rooms");
       List<Room> rooms=roomRepository.findAll();
       List<RoomDto> roomDtos=rooms.stream()
                .map(room -> modelMapper.map(room, RoomDto.class))
                .toList();
       log.info("Retrieved {} rooms", roomDtos.size());
       return roomDtos;
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
       log.info("Retrieving room with room id {}", roomId);
       Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
       RoomDto roomDto=modelMapper.map(room, RoomDto.class);
       log.info("Room retrieved successfully with id: {}", roomId);
       return roomDto;
    }

    @Override
    public void deleteRoomById(Long roomId) {
        log.info("Deleting room with room id {}", roomId);
        if (!roomRepository.existsById(roomId)) {
            log.warn("Room with id {} not found for deletion", roomId);
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        }
        roomRepository.deleteById(roomId);
        log.info("Room deleted successfully with id: {}", roomId);
    }
}
