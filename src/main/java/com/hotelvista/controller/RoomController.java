package com.hotelvista.controller;
import com.hotelvista.model.Room;
import com.hotelvista.service.RoomService;
import com.hotelvista.util.ValidatorsUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService service;
    public RoomController(RoomService service) {
        this.service = service;
    }
    @GetMapping("")
    public List<Room> selectAll() {
        return service.selectAll();
    }
    @GetMapping("/{id}")
    public Room selectById(@PathVariable String id) {
        Optional<Room> room = service.selectById(id);
        return room.orElse(null);
    }
    @PostMapping("/save")
    public ResponseEntity<?> insertOrUpdate(@RequestBody Room room) {
        String numberError = ValidatorsUtil.validateRoomNumber(room.getRoomNumber());
        if (numberError != null) {
            return ResponseEntity.badRequest().body(numberError);
        }
        String floorError = ValidatorsUtil.validateFloor(room.getFloor());
        if (floorError != null) {
            return ResponseEntity.badRequest().body(floorError);
        }
        if (room.getRoomType() == null || room.getRoomType().getRoomTypeID() == null) {
            return ResponseEntity.badRequest().body("Room type is required");
        }
        Room saved = service.insertOrUpdate(room);
        return ResponseEntity.ok(saved);
    }
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
