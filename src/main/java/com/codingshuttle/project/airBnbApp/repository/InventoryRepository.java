package com.codingshuttle.project.airBnbApp.repository;

import com.codingshuttle.project.airBnbApp.entity.Hotel;
import com.codingshuttle.project.airBnbApp.entity.Inventory;
import com.codingshuttle.project.airBnbApp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    void deleteByRoomAndDateGreaterThanEqual(Room room, LocalDate date);

    void deleteByHotelAndDateGreaterThanEqual(Hotel hotel, LocalDate date);
}