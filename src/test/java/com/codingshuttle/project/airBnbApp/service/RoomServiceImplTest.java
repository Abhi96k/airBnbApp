package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.dto.RoomDto;
import com.codingshuttle.project.airBnbApp.entity.Hotel;
import com.codingshuttle.project.airBnbApp.entity.Room;
import com.codingshuttle.project.airBnbApp.exception.ResourceNotFoundException;
import com.codingshuttle.project.airBnbApp.repository.HotelRepository;
import com.codingshuttle.project.airBnbApp.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private RoomServiceImpl roomService;

    private Hotel hotel;
    private Room room;
    private RoomDto roomDto;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");
        hotel.setActive(false);

        room = new Room();
        room.setId(1L);
        room.setType("DELUXE");
        room.setBasePrice(BigDecimal.valueOf(100));
        room.setTotalCount(5);
        room.setCapacity(2);
        room.setHotel(hotel);

        roomDto = new RoomDto();
        roomDto.setId(1L);
        roomDto.setType("DELUXE");
        roomDto.setBasePrice(BigDecimal.valueOf(100));
        roomDto.setTotalCount(5);
        roomDto.setCapacity(2);
    }

    @Test
    void createRoom_shouldCreateRoom_whenHotelIsInactive() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(modelMapper.map(roomDto, Room.class)).thenReturn(room);
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(modelMapper.map(room, RoomDto.class)).thenReturn(roomDto);

        RoomDto result = roomService.createRoom(1L, roomDto);

        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("DELUXE");
        verify(roomRepository).save(room);
        verify(inventoryService, never()).initializeRoomForYear(any());
    }

    @Test
    void createRoom_shouldInitializeInventory_whenHotelIsActive() {
        hotel.setActive(true);
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(modelMapper.map(roomDto, Room.class)).thenReturn(room);
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(modelMapper.map(room, RoomDto.class)).thenReturn(roomDto);

        RoomDto result = roomService.createRoom(1L, roomDto);

        assertThat(result).isNotNull();
        verify(inventoryService).initializeRoomForYear(room);
    }

    @Test
    void createRoom_shouldThrowException_whenHotelNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomService.createRoom(1L, roomDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Hotel not found with id: 1");
    }

    @Test
    void getRooms_shouldReturnRoomList() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(roomRepository.findByHotelId(1L)).thenReturn(List.of(room));
        when(modelMapper.map(room, RoomDto.class)).thenReturn(roomDto);

        List<RoomDto> result = roomService.getRooms(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo("DELUXE");
    }

    @Test
    void getRooms_shouldThrowException_whenHotelNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomService.getRooms(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getRoomById_shouldReturnRoom_whenFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(modelMapper.map(room, RoomDto.class)).thenReturn(roomDto);

        RoomDto result = roomService.getRoomById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("DELUXE");
    }

    @Test
    void getRoomById_shouldThrowException_whenNotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomService.getRoomById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Room not found with id: 1");
    }

    @Test
    void deleteRoomById_shouldDeleteRoom() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        roomService.deleteRoomById(1L);

        verify(inventoryService).deleteFutureInventories(room);
        verify(roomRepository).delete(room);
    }

    @Test
    void deleteRoomById_shouldThrowException_whenNotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomService.deleteRoomById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
