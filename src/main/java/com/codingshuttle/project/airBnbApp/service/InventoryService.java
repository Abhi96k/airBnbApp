package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.dto.HotelDto;
import com.codingshuttle.project.airBnbApp.dto.HotelRequestRequest;
import com.codingshuttle.project.airBnbApp.entity.Hotel;
import com.codingshuttle.project.airBnbApp.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {
    void initializeRoomForYear(Room roomId);

    void deleteFutureInventories(Room roomId);


    void deleteAllInventoriesByHotel(Hotel hotel);

    Page<HotelDto> searchHotels(HotelRequestRequest hotelRequestRequest);


}
