package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.dto.HotelDto;
import com.codingshuttle.project.airBnbApp.dto.HotelInfoDto;
import com.codingshuttle.project.airBnbApp.dto.RoomDto;
import com.codingshuttle.project.airBnbApp.entity.Hotel;
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
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;

    @Override
    public HotelDto CreateNewHotel(HotelDto hotelDto) {
        log.info("Creating new hotel with name: {}", hotelDto.getName());

        Hotel hotelentity = modelMapper.map(hotelDto, Hotel.class);
        hotelentity.setActive(false);
        Hotel savedHotel = hotelRepository.save(hotelentity);
        HotelDto hotelDtoResponse = modelMapper.map(savedHotel, HotelDto.class);


        log.info("Hotel created successfully with id: {}", savedHotel.getId());

        return hotelDtoResponse;
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Fetching hotel with id: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotel(Long id, HotelDto hotelDto) {
        log.info("Updating hotel with id: {}", id);
        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id)); //fetching the existance entity from database

        log.info("existing : {}",existingHotel.getName());
        log.info("existing id : {}",existingHotel.getId());
        modelMapper.map(hotelDto, existingHotel);

        //entity update with dto values



        Hotel updatedHotel = hotelRepository.save(existingHotel);
        log.info("Hotel updated successfully with id: {}", updatedHotel.getId());
        return modelMapper.map(updatedHotel, HotelDto.class);
    }

    @Override
    @Transactional
    public Boolean deleteHotelById(Long id) {
        log.info("Deleting hotel with id: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        var rooms = hotel.getRooms();
        if (rooms != null && !rooms.isEmpty()) {
            inventoryService.deleteAllInventoriesByHotel(hotel);
            roomRepository.deleteAll(rooms);
        }

        hotelRepository.delete(hotel);
        log.info("Hotel deleted successfully with id: {}", id);
        return true;
    }

    @Override
    public void activateHotel(Long hotelId) {
        log.info("Activating hotel with id: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        hotel.setActive(true);
        hotelRepository.save(hotel);

        // Create inventory for all rooms in this hotel (assuming done only once during activation)
        for(var room: hotel.getRooms()){
            inventoryService.initializeRoomForYear(room);
        }

        log.info("Hotel with id: {} activated successfully", hotelId);
    }

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        List<RoomDto> roomDtos = hotel.getRooms().stream()
                .map(room -> modelMapper.map(room, RoomDto.class))
                .toList();

        return new HotelInfoDto(
                modelMapper.map(hotel, HotelDto.class),
                roomDtos
        );

    }
}
