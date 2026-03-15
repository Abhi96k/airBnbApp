package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.entity.Hotel;
import com.codingshuttle.project.airBnbApp.entity.Inventory;
import com.codingshuttle.project.airBnbApp.entity.Room;
import com.codingshuttle.project.airBnbApp.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public void initializeRoomForYear(Room room) {
        log.info("Initializing inventory for room with id {}", room.getId());
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);

        List<Inventory> inventoryList = new ArrayList<>();
        for (LocalDate date = today; date.isBefore(endDate); date = date.plusDays(1)) {
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .date(date)
                    .bookedCount(0)
                    .totalCount(room.getTotalCount())
                    .surgeFactor(BigDecimal.ONE)
                    .price(room.getBasePrice())
                    .city(room.getHotel().getCity())
                    .closed(false)
                    .build();
            inventoryList.add(inventory);
        }
        inventoryRepository.saveAll(inventoryList);
        log.info("Inventory initialized successfully for room with id {}", room.getId());
    }

    @Override
    @Transactional
    public void deleteFutureInventories(Room room) {
        log.info("Deleting future inventories for room with id {}", room.getId());
        LocalDate today = LocalDate.now();
        inventoryRepository.deleteByRoomAndDateGreaterThanEqual(room, today);
        log.info("Future inventories deleted successfully for room with id {}", room.getId());
    }

    @Override
    @Transactional
    public void deleteFutureInventoriesByHotel(Hotel hotel) {
        log.info("Deleting future inventories for hotel with id {}", hotel.getId());
        LocalDate today = LocalDate.now();
        inventoryRepository.deleteByHotelAndDateGreaterThanEqual(hotel, today);
        log.info("Future inventories deleted successfully for hotel with id {}", hotel.getId());
    }
}
