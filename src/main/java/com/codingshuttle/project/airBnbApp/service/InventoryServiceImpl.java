package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.dto.HotelDto;
import com.codingshuttle.project.airBnbApp.dto.HotelRequestRequest;
import com.codingshuttle.project.airBnbApp.entity.Hotel;
import com.codingshuttle.project.airBnbApp.entity.Inventory;
import com.codingshuttle.project.airBnbApp.entity.Room;
import com.codingshuttle.project.airBnbApp.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public void initializeRoomForYear(Room roomId) {
        log.info("Initializing inventory for room with id {}", roomId.getId());
        LocalDate today= LocalDate.now();
        LocalDate endDate=today.plusYears(1);

        for(LocalDate date=today; date.isBefore(endDate); date=date.plusDays(1)){
            Inventory inventory = Inventory.builder()
                    .hotel(roomId.getHotel())
                    .room(roomId)
                    .date(date)
                    .bookedCount(0)
                    .totalCount(roomId.getTotalcount())
                    .surgeFactor(BigDecimal.ONE)
                    .price(roomId.getBasePrice())
                    .city(roomId.getHotel().getCity())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);
        }
        log.info("Inventory initialized successfully for room with id {}", roomId.getId());

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
    public void deleteAllInventoriesByHotel(Hotel hotel) {
        log.info("Deleting all inventories for hotel with id {}", hotel.getId());
        inventoryRepository.deleteByHotel(hotel);
        log.info("All inventories deleted for hotel with id {}", hotel.getId());
    }

    @Override
    public Page<HotelDto> searchHotels(HotelRequestRequest hotelRequestRequest) {
        Pageable pageable = PageRequest.of(hotelRequestRequest.getPage(), hotelRequestRequest.getSize());
        long dateCount = ChronoUnit.DAYS.between(hotelRequestRequest.getStartDate(), hotelRequestRequest.getEndDate());

        Page<Hotel> hotelPage = inventoryRepository.findHotelWithAvailableInventory(
                hotelRequestRequest.getCity(),
                hotelRequestRequest.getStartDate(),
                hotelRequestRequest.getEndDate(),
                hotelRequestRequest.getRoomCount(),
                (int) dateCount,
                pageable
        );

        return hotelPage.map(hotel -> modelMapper.map(hotel, HotelDto.class));
    }
}
