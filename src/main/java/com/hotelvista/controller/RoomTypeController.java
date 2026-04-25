package com.hotelvista.controller;
import com.hotelvista.model.RoomType;
import com.hotelvista.service.RoomTypeService;
import com.hotelvista.util.ValidatorsUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/room-types")
public class RoomTypeController {
    private final RoomTypeService service;
    public RoomTypeController(RoomTypeService service) {
        this.service = service;
    }
    @GetMapping("")
    public List<RoomType> selectAll() {
        return service.selectAll();
    }
    @GetMapping("/{id}")
    public RoomType selectById(@PathVariable String id) {
        Optional<RoomType> roomType = service.selectById(id);
        return roomType.orElse(null);
    }
    @PostMapping("/save")
    public ResponseEntity<?> insertOrUpdate(@RequestBody RoomType roomType) {
        String idError = ValidatorsUtil.validateRoomTypeId(roomType.getRoomTypeID());
        if (idError != null) {
            return ResponseEntity.badRequest().body(idError);
        }
        String nameError = ValidatorsUtil.validateRoomTypeName(roomType.getTypeName());
        if (nameError != null) {
            return ResponseEntity.badRequest().body(nameError);
        }
        String occupancyError = ValidatorsUtil.validateCapacity(roomType.getMaxOccupancy());
        if (occupancyError != null) {
            return ResponseEntity.badRequest().body(occupancyError);
        }
        String priceError = ValidatorsUtil.validateRoomPrice(roomType.getBasePrice());
        if (priceError != null) {
            return ResponseEntity.badRequest().body(priceError);
        }
        String areaError = ValidatorsUtil.validateRoomSize(roomType.getArea());
        if (areaError != null) {
            return ResponseEntity.badRequest().body(areaError);
        }
        RoomType saved = service.insertOrUpdate(roomType);
        return ResponseEntity.ok(saved);
    }
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
