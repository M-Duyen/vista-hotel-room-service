package com.hotelvista.controller;
import com.hotelvista.dto.RoomChangeRequestDTO;
import com.hotelvista.dto.RoomChangeResponseDTO;
import com.hotelvista.model.RoomChangeRequest;
import com.hotelvista.service.RoomChangeRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/room-change-requests")
public class RoomChangeRequestController {
    private final RoomChangeRequestService service;
    public RoomChangeRequestController(RoomChangeRequestService service) {
        this.service = service;
    }
    @GetMapping
    public ResponseEntity<List<RoomChangeRequest>> getAllRequests() {
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<RoomChangeRequest> getRequestById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<RoomChangeRequest>> getRequestsByBookingId(@PathVariable String bookingId) {
        return ResponseEntity.ok(service.findByBookingId(bookingId));
    }
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<RoomChangeRequest>> getRequestsByCustomerId(@PathVariable String customerId) {
        return ResponseEntity.ok(service.findByCustomerId(customerId));
    }
    @GetMapping("/pending")
    public ResponseEntity<List<RoomChangeRequest>> getPendingRequests() {
        return ResponseEntity.ok(service.findPendingRequests());
    }
    @PostMapping
    public ResponseEntity<RoomChangeRequest> createRequest(@RequestBody RoomChangeRequestDTO dto) {
        try {
            return ResponseEntity.ok(service.createRequest(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/{id}/process")
    public ResponseEntity<RoomChangeRequest> processRequest(@PathVariable String id, @RequestBody RoomChangeResponseDTO response) {
        try {
            return ResponseEntity.ok(service.processRequest(id, response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable String id) {
        service.deleteRequest(id);
        return ResponseEntity.ok().build();
    }
}