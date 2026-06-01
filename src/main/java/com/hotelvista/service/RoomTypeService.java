package com.hotelvista.service;

import com.hotelvista.model.RoomType;
import com.hotelvista.repository.RoomTypeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RoomTypeService {
    private final RoomTypeRepository roomTypeRepo;
    private final RoomService roomService;

    public RoomTypeService(RoomTypeRepository roomTypeRepo, RoomService roomService) {
        this.roomTypeRepo = roomTypeRepo;
        this.roomService = roomService;
    }

    public List<RoomType> selectAll() {
        return roomTypeRepo.findAll();
    }

    public Optional<RoomType> selectById(String id) {
        return roomTypeRepo.findById(id);
    }

    public RoomType insertOrUpdate(RoomType roomType) {
        return roomTypeRepo.save(roomType);
    }

    public void delete(String id) {
        roomTypeRepo.deleteById(id);
    }

    public Double calculateDiscountedPrice(String roomTypeId, LocalDate bookingDate) {
        RoomType roomType = roomTypeRepo.findById(roomTypeId).orElse(null);
        if (roomType == null) {
            return 0.0;
        }
        return roomService.calculatePriceByRoomType(roomType, bookingDate);
    }

}
