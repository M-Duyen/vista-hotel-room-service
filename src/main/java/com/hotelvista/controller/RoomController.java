package com.hotelvista.controller;

import com.hotelvista.model.Room;
import com.hotelvista.model.enums.RoomStatus;
import com.hotelvista.service.RoomService;
import com.hotelvista.util.ValidatorsUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<Room> selectAll(@RequestParam(required = false) String roomTypeId) {
        if (roomTypeId != null && !roomTypeId.trim().isEmpty()) {
            return service.selectAll().stream()
                    .filter(r -> r.getRoomType() != null && roomTypeId.equals(r.getRoomType().getRoomTypeID()))
                    .toList();
        }
        return service.selectAll();
    }

    @GetMapping("/available")
    @PreAuthorize("permitAll()")
    public List<Room> checkAvailability(
            @RequestParam(required = false) String checkIn,
            @RequestParam(required = false) String checkOut,
            @RequestParam(required = false, defaultValue = "1") int guests) {
        return service.selectAll();
    }

    @GetMapping("/search")
    @PreAuthorize("permitAll()")
    public List<Room> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) RoomStatus status,
            @RequestParam(required = false) String roomTypeId) {
        String query = q == null ? null : q.trim().toLowerCase();
        return service.selectAll().stream()
                .filter(r -> roomNumber == null || roomNumber.trim().isEmpty()
                        || (r.getRoomNumber() != null && r.getRoomNumber().equals(roomNumber)))
                .filter(r -> floor == null || (r.getFloor() != null && floor.equals(r.getFloor())))
                .filter(r -> status == null || status.equals(r.getStatus()))
                .filter(r -> roomTypeId == null
                        || (r.getRoomType() != null && roomTypeId.equals(r.getRoomType().getRoomTypeID())))
                .filter(r -> {
                    if (query == null || query.isEmpty())
                        return true;
                    boolean inNumber = r.getRoomNumber() != null && r.getRoomNumber().toLowerCase().contains(query);
                    boolean inNotes = r.getNotes() != null && r.getNotes().toLowerCase().contains(query);
                    boolean inType = r.getRoomType() != null && r.getRoomType().getTypeName() != null
                            && r.getRoomType().getTypeName().toLowerCase().contains(query);
                    return inNumber || inNotes || inType;
                })
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public Room selectById(@PathVariable String id) {
        Optional<Room> room = service.selectById(id);
        return room.orElse(null);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('room_manage')")
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
    @PreAuthorize("hasAuthority('room_manage')")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('room_manage')")
    public ResponseEntity<?> updateStatus(@PathVariable String id, @RequestParam RoomStatus status) {
        try {
            service.updateStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
