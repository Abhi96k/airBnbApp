package com.codingshuttle.project.airBnbApp.config;

import com.codingshuttle.project.airBnbApp.entity.Hotel;
import com.codingshuttle.project.airBnbApp.entity.Inventory;
import com.codingshuttle.project.airBnbApp.entity.Room;
import com.codingshuttle.project.airBnbApp.repository.HotelRepository;
import com.codingshuttle.project.airBnbApp.repository.InventoryRepository;
import com.codingshuttle.project.airBnbApp.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (hotelRepository.count() > 0) {
            log.info("Hotels already present, skipping data initialization");
            return;
        }

        log.info("Initializing dummy data...");
        
        for(int i=1; i<=15; i++) {
             // Create Hotel
            Hotel hotel = new Hotel();
            hotel.setName("Grand Hotel " + i);
            hotel.setCity("New York");
            hotel.setPhotos(List.of("https://placehold.co/600x400", "https://placehold.co/600x400"));
            hotel.setAmenities(List.of("Gym", "Pool", "WiFi"));
            hotel.setActive(true);
            
            Hotel savedHotel = hotelRepository.save(hotel);
            log.info("Hotel created: {}", savedHotel.getId());
    
            // Create Room
            Room room = new Room();
            room.setHotel(savedHotel);
            room.setType("Deluxe");
            room.setBasePrice(new BigDecimal("200.00"));
            room.setTotalcount(10);
            room.setCapacity(2);
            room.setPhotos(List.of("https://placehold.co/600x400"));
            room.setAmenities(List.of("AC", "TV"));
            
            Room savedRoom = roomRepository.save(room);
            log.info("Room created: {}", savedRoom.getId());
    
            // Create Inventory for the next 30 days
            LocalDate today = LocalDate.now();
            for (int j = 0; j < 30; j++) {
                LocalDate date = today.plusDays(j);
                Inventory inventory = Inventory.builder()
                        .hotel(savedHotel)
                        .room(savedRoom)
                        .date(date)
                        .bookedCount(0) // Initially 0 booked
                        .totalCount(savedRoom.getTotalcount())
                        .surgeFactor(BigDecimal.ONE)
                        .price(savedRoom.getBasePrice()) // price = basePrice * surgeFactor
                        .city(savedHotel.getCity())
                        .closed(false)
                        .build();
                inventoryRepository.save(inventory);
            }
        }
        log.info("Inventory initialized for 30 days.");
        
        log.info("Dummy data initialization completed");
    }
}




