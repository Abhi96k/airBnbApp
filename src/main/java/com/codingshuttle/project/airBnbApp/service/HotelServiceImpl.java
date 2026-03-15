package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.dto.HotelDto;
import com.codingshuttle.project.airBnbApp.entity.Hotel;
import com.codingshuttle.project.airBnbApp.entity.Room;
import com.codingshuttle.project.airBnbApp.exception.ResourceNotFoundException;
import com.codingshuttle.project.airBnbApp.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;

    @Override
    @Transactional
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating new hotel with name: {}", hotelDto.getName());
        Hotel hotelEntity = modelMapper.map(hotelDto, Hotel.class);
        hotelEntity.setActive(false);
        Hotel savedHotel = hotelRepository.save(hotelEntity);
        log.info("Hotel created successfully with id: {}", savedHotel.getId());
        return modelMapper.map(savedHotel, HotelDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public HotelDto getHotelById(Long id) {
        log.info("Fetching hotel with id: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public HotelDto updateHotel(Long id, HotelDto hotelDto) {
        log.info("Updating hotel with id: {}", id);
        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        modelMapper.map(hotelDto, existingHotel);
        existingHotel.setId(id);
        Hotel updatedHotel = hotelRepository.save(existingHotel);
        log.info("Hotel updated successfully with id: {}", updatedHotel.getId());
        return modelMapper.map(updatedHotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        log.info("Deleting hotel with id: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        inventoryService.deleteFutureInventoriesByHotel(hotel);
        hotelRepository.deleteById(id);
        log.info("Hotel with id: {} deleted successfully", id);
    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("Activating hotel with id: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        hotel.setActive(true);
        hotelRepository.save(hotel);

        for (Room room : hotel.getRooms()) {
            inventoryService.initializeRoomForYear(room);
        }
        log.info("Hotel with id: {} activated successfully", hotelId);
    }
}
