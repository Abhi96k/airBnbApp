package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.dto.HotelDto;


public interface HotelService {
    HotelDto CreateNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    HotelDto updateHotel(Long id, HotelDto hotelDto);

    Boolean deleteHotelById(Long id);
}
