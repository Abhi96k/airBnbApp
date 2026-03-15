package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.dto.RoomDto;
import com.codingshuttle.project.airBnbApp.entity.Hotel;
import com.codingshuttle.project.airBnbApp.entity.Room;
import com.codingshuttle.project.airBnbApp.exception.ResourceNotFoundException;
import com.codingshuttle.project.airBnbApp.repository.HotelRepository;
import com.codingshuttle.project.airBnbApp.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;

    @Override
    @Transactional
    public RoomDto createRoom(Long hotelId, RoomDto roomDto) {
        log.info("Creating room for hotel with id {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        Room roomEntity = modelMapper.map(roomDto, Room.class);
        roomEntity.setHotel(hotel);
        Room savedRoom = roomRepository.save(roomEntity);

        if (Boolean.TRUE.equals(hotel.getActive())) {
            inventoryService.initializeRoomForYear(savedRoom);
        }

        log.info("Room created successfully with id: {}", savedRoom.getId());
        return modelMapper.map(savedRoom, RoomDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getRooms(Long hotelId) {
        log.info("Retrieving rooms for hotel with id {}", hotelId);
        hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        return roomRepository.findByHotelId(hotelId).stream()
                .map(room -> modelMapper.map(room, RoomDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDto getRoomById(Long roomId) {
        log.info("Retrieving room with id {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    @Transactional
    public void deleteRoomById(Long roomId) {
        log.info("Deleting room with id {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
        inventoryService.deleteFutureInventories(room);
        roomRepository.delete(room);
        log.info("Room with id {} deleted successfully", roomId);
    }
}
