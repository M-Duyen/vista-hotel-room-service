package com.hotelvista.service;
import com.hotelvista.dto.RoomChangeRequestDTO;
import com.hotelvista.dto.RoomChangeResponseDTO;
import com.hotelvista.model.Room;
import com.hotelvista.model.RoomChangeRequest;
import com.hotelvista.model.enums.RequestStatus;
import com.hotelvista.model.enums.RoomStatus;
import com.hotelvista.repository.RoomChangeRequestRepository;
import com.hotelvista.repository.RoomRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class RoomChangeRequestService {
    private final RoomChangeRequestRepository repository;
    private final RoomRepository roomRepository;
    public RoomChangeRequestService(RoomChangeRequestRepository repository, RoomRepository roomRepository) {
        this.repository = repository;
        this.roomRepository = roomRepository;
    }
    public List<RoomChangeRequest> findAll() {
        return repository.findAllByOrderByRequestDateDesc();
    }
    public Optional<RoomChangeRequest> findById(String id) {
        return repository.findById(id);
    }
    public List<RoomChangeRequest> findByBookingId(String bookingId) {
        return repository.findByBookingId(bookingId);
    }
    public List<RoomChangeRequest> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }
    public List<RoomChangeRequest> findPendingRequests() {
        return repository.findPendingRequests();
    }
    public RoomChangeRequest createRequest(RoomChangeRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Request data is required");
        }
        Room currentRoom = roomRepository.findByRoomNumber(dto.getCurrentRoomNumber())
                .orElseThrow(() -> new RuntimeException("Current room not found"));
        Room newRoom = roomRepository.findByRoomNumber(dto.getNewRoomNumber())
                .orElseThrow(() -> new RuntimeException("New room not found"));
        RoomChangeRequest request = new RoomChangeRequest();
        request.setRequestID(generateRequestId());
        request.setBookingId(dto.getBookingId());
        request.setCustomerId(dto.getCustomerId());
        request.setCurrentRoomNumber(currentRoom.getRoomNumber());
        request.setNewRoomNumber(newRoom.getRoomNumber());
        request.setReason(dto.getReason());
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(RequestStatus.PENDING);
        return repository.save(request);
    }
    public RoomChangeRequest processRequest(String id, RoomChangeResponseDTO response) {
        RoomChangeRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        if (response.isApprove()) {
            Room currentRoom = roomRepository.findByRoomNumber(request.getCurrentRoomNumber())
                    .orElseThrow(() -> new RuntimeException("Current room not found"));
            Room newRoom = roomRepository.findByRoomNumber(request.getNewRoomNumber())
                    .orElseThrow(() -> new RuntimeException("New room not found"));
            currentRoom.setStatus(RoomStatus.AVAILABLE);
            newRoom.setStatus(RoomStatus.BOOKED);
            roomRepository.save(currentRoom);
            roomRepository.save(newRoom);
            request.setStatus(RequestStatus.COMPLETED);
        } else {
            request.setStatus(RequestStatus.FAILED);
        }
        request.setResponseNote(response.getResponseNote());
        request.setResponseDate(LocalDateTime.now());
        request.setProcessedBy(response.getProcessedBy());
        return repository.save(request);
    }
    public void deleteRequest(String id) {
        repository.deleteById(id);
    }
    private String generateRequestId() {
        return "RC-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}