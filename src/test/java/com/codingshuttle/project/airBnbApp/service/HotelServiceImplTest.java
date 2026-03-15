package com.codingshuttle.project.airBnbApp.service;

import com.codingshuttle.project.airBnbApp.dto.HotelDto;
import com.codingshuttle.project.airBnbApp.entity.Hotel;
import com.codingshuttle.project.airBnbApp.entity.HotelContactInfo;
import com.codingshuttle.project.airBnbApp.entity.Room;
import com.codingshuttle.project.airBnbApp.exception.ResourceNotFoundException;
import com.codingshuttle.project.airBnbApp.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel hotel;
    private HotelDto hotelDto;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");
        hotel.setCity("Test City");
        hotel.setActive(false);

        hotelDto = new HotelDto();
        hotelDto.setName("Test Hotel");
        hotelDto.setCity("Test City");
        hotelDto.setActive(false);

        HotelContactInfo contactInfo = new HotelContactInfo();
        contactInfo.setEmail("test@hotel.com");
        contactInfo.setPhoneNumber("1234567890");
        hotelDto.setContactInfo(contactInfo);
    }

    @Test
    void createNewHotel_shouldCreateHotelWithActiveFalse() {
        when(modelMapper.map(hotelDto, Hotel.class)).thenReturn(hotel);
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        when(modelMapper.map(hotel, HotelDto.class)).thenReturn(hotelDto);

        HotelDto result = hotelService.createNewHotel(hotelDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Hotel");
        assertThat(hotel.getActive()).isFalse();
        verify(hotelRepository).save(hotel);
    }

    @Test
    void getHotelById_shouldReturnHotel_whenFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(modelMapper.map(hotel, HotelDto.class)).thenReturn(hotelDto);

        HotelDto result = hotelService.getHotelById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Hotel");
    }

    @Test
    void getHotelById_shouldThrowException_whenNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.getHotelById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Hotel not found with id: 1");
    }

    @Test
    void updateHotel_shouldUpdateAndReturnHotel() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        lenient().doReturn(hotelDto).when(modelMapper).map(any(), eq(HotelDto.class));

        HotelDto result = hotelService.updateHotel(1L, hotelDto);

        assertThat(result).isNotNull();
        verify(hotelRepository).save(hotel);
    }

    @Test
    void updateHotel_shouldThrowException_whenNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.updateHotel(1L, hotelDto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteHotelById_shouldDeleteHotel() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        hotelService.deleteHotelById(1L);

        verify(inventoryService).deleteFutureInventoriesByHotel(hotel);
        verify(hotelRepository).deleteById(1L);
    }

    @Test
    void deleteHotelById_shouldThrowException_whenNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.deleteHotelById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void activateHotel_shouldActivateAndInitializeInventory() {
        Room room = new Room();
        room.setId(1L);
        hotel.setRooms(Collections.singletonList(room));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        hotelService.activateHotel(1L);

        assertThat(hotel.getActive()).isTrue();
        verify(hotelRepository).save(hotel);
        verify(inventoryService).initializeRoomForYear(room);
    }

    @Test
    void activateHotel_shouldThrowException_whenNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.activateHotel(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
