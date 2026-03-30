package com.codingshuttle.project.airBnbApp.repository;

import com.codingshuttle.project.airBnbApp.entity.Hotel;
import com.codingshuttle.project.airBnbApp.entity.Inventory;
import com.codingshuttle.project.airBnbApp.entity.Room;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    void deleteByRoomAndDateGreaterThanEqual(Room room, LocalDate date);

    void deleteByHotel(Hotel hotel);

    @Query("""
            SELECT DISTINCT i.hotel
            FROM Inventory i
            WHERE i.city = :city
            AND i.date >= :startDate
            AND i.date < :endDate
            AND i.closed = false
            AND (i.totalCount - i.bookedCount-i.reservedCount) >= :roomsCount
            GROUP BY i.hotel
            HAVING COUNT(DISTINCT i.date) = :dateCount
            """)
    Page<Hotel> findHotelWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") Integer roomCount,
            @Param("dateCount") Integer dateCount,
            Pageable pageable
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT i
            FROM Inventory i
            WHERE i.room.id = :roomId
            AND i.date >= :startDate
            AND i.date < :endDate
            AND i.closed = false
            AND (i.totalCount - i.bookedCount-i.reservedCount) >= :roomsCount
            ORDER BY i.date
            """)
    List<Inventory> findAndLockAvailableInventoryByRoom(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") Integer roomsCount
    );

}