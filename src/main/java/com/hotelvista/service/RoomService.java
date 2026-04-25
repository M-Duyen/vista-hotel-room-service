package com.hotelvista.service;
import com.hotelvista.model.Room;
import com.hotelvista.model.RoomType;
import com.hotelvista.repository.RoomRepository;
import com.hotelvista.repository.RoomTypeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class RoomService {
    private final RoomRepository roomRepo;
    private final RoomTypeRepository roomTypeRepository;
    public RoomService(RoomRepository roomRepo, RoomTypeRepository roomTypeRepository) {
        this.roomRepo = roomRepo;
        this.roomTypeRepository = roomTypeRepository;
    }
    public Room findById(String id) {
        return roomRepo.findById(id).orElse(null);
    }
    public List<Room> selectAll() {
        return roomRepo.findAll();
    }
    public Optional<Room> selectById(String id) {
        return roomRepo.findById(id);
    }
    public Room insertOrUpdate(Room room) {
        if (room.getRoomType() != null && room.getRoomType().getRoomTypeID() != null) {
            RoomType managedType = roomTypeRepository.findById(room.getRoomType().getRoomTypeID())
                    .orElseThrow(() -> new IllegalArgumentException("Room type not found"));
            room.setRoomType(managedType);
        }
        return roomRepo.save(room);
    }
    public void delete(String id) {
        roomRepo.deleteById(id);
    }
    public void save(Room room) {
        roomRepo.save(room);
    }
}
