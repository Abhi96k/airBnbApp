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
    public RoomDto createRoom(Long hotelId, RoomDto roomDto) {
        log.info("Creating room with room id {}", roomDto.getId());
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
         Room roomentity=modelMapper.map(roomDto, Room.class);
         roomentity.setHotel(hotel);
         Room savedRoom=roomRepository.save(roomentity);
         RoomDto roomDtoResponse=modelMapper.map(savedRoom, RoomDto.class);
         if(hotel.getActive()){
             log.info("Creating room with hotel id {}", hotelId);
             inventoryService.initializeRoomForYear(savedRoom);
         }
         log.info("Room created successfully with id: {}", savedRoom.getId());
         return roomDtoResponse;
    }

    @Override
    public List<RoomDto> getRooms(Long hotelId) {
     log.info("Retrieving rooms for hotel with id {}", hotelId);
     Hotel hotel= hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
     return hotel.getRooms().stream().map(room -> modelMapper.map(room, RoomDto.class)).toList();
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
       log.info("Retrieving room with room id {}", roomId);
       Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
       log.info("Room found with room id {}", roomId);
       return modelMapper.map(room, RoomDto.class);


    }

    @Override
    public void deleteRoomById(Long roomId) {
        log.info("Deleting room with room id {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

        // Delete future inventory for this room
        inventoryService.deleteFutureInventories(room);

        roomRepository.delete(room);
        log.info("Room with id {} deleted successfully", roomId);

    }
}
