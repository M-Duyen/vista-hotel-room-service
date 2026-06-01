package com.hotelvista.controller;

import com.hotelvista.model.RoomType;
import com.hotelvista.service.RoomTypeService;
import com.hotelvista.util.ValidatorsUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/room-types")
public class RoomTypeController {

    private final RoomTypeService service;

    public RoomTypeController(RoomTypeService service) {
        this.service = service;
    }

    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<RoomType> selectAll() {
        return service.selectAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public RoomType selectById(@PathVariable String id) {
        Optional<RoomType> roomType = service.selectById(id);
        return roomType.orElse(null);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('room_type_manage')")
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
    @PreAuthorize("hasAuthority('room_type_manage')")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }


    @GetMapping("/search")
    @PreAuthorize("permitAll()")
    public List<RoomType> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String roomTypeId,
            @RequestParam(required = false) Double minArea,
            @RequestParam(required = false) Double maxArea,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer maxOccupancy) {
        String query = q == null ? null : q.trim().toLowerCase();
        return service.selectAll().stream()
                .filter(rt -> roomTypeId == null
                        || (rt.getRoomTypeID() != null && rt.getRoomTypeID().equals(roomTypeId)))
                .filter(rt -> minArea == null || (rt.getArea() != null && rt.getArea() >= minArea))
                .filter(rt -> maxArea == null || (rt.getArea() != null && rt.getArea() <= maxArea))
                .filter(rt -> minPrice == null || (rt.getBasePrice() != null && rt.getBasePrice() >= minPrice))
                .filter(rt -> maxPrice == null || (rt.getBasePrice() != null && rt.getBasePrice() <= maxPrice))
                .filter(rt -> maxOccupancy == null
                        || (rt.getMaxOccupancy() != null && rt.getMaxOccupancy().equals(maxOccupancy)))
                .filter(rt -> {
                    if (query == null || query.isEmpty())
                        return true;
                    boolean inName = rt.getTypeName() != null && rt.getTypeName().toLowerCase().contains(query);
                    boolean inDesc = rt.getDescription() != null && rt.getDescription().toLowerCase().contains(query);
                    return inName || inDesc;
                })
                .toList();
    }
}
