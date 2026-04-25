package com.hotelvista.service;

import com.hotelvista.model.RoomType;
import com.hotelvista.repository.RoomTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomTypeService {
    private final RoomTypeRepository roomTypeRepo;

    public RoomTypeService(RoomTypeRepository roomTypeRepo) {
        this.roomTypeRepo = roomTypeRepo;
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
}
