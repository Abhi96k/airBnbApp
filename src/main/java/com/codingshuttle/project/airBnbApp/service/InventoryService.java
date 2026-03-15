package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.entity.Hotel;
import com.codingshuttle.project.airBnbApp.entity.Room;

public interface InventoryService {

    void initializeRoomForYear(Room room);

    void deleteFutureInventories(Room room);

    void deleteFutureInventoriesByHotel(Hotel hotel);
}
