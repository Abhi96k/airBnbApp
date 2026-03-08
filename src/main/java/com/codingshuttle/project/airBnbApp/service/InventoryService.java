package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.entity.Hotel;
import com.codingshuttle.project.airBnbApp.entity.Room;

public interface InventoryService {
    void initializeRoomForYear(Room roomId);

    void deleteFutureInventories(Room roomId);

    void deleteFutureInventoriesByHotel(Hotel hotel);
}
