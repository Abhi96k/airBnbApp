package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.dto.HotelDto;
import com.codingshuttle.project.airBnbApp.entity.Hotel;
import com.codingshuttle.project.airBnbApp.exception.ResourceNotFoundException;
import com.codingshuttle.project.airBnbApp.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

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
    public Boolean deleteHotelById(Long id) {
        log.info("Deleting hotel with id: {}", id);
      boolean  exists= hotelRepository.existsById(id);
        if (!exists) {
            log.warn("Hotel with id: {} not found for deletion", id);
            throw new ResourceNotFoundException("Hotel not found with id: " + id);
        }
        hotelRepository.deleteById(id);
        //TODO: deleted from future inventory from this hotel
        log.info("Hotel with id: {} deleted successfully", id);
        return true;
    }
}
