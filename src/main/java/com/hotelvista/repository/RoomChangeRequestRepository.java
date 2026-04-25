package com.hotelvista.repository;
import com.hotelvista.model.RoomChangeRequest;
import com.hotelvista.model.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface RoomChangeRequestRepository extends JpaRepository<RoomChangeRequest, String> {
    List<RoomChangeRequest> findByBookingId(String bookingId);
    List<RoomChangeRequest> findByCustomerId(String customerId);
    List<RoomChangeRequest> findByStatus(RequestStatus status);
    @Query("SELECT r FROM RoomChangeRequest r WHERE r.status = 'PENDING' ORDER BY r.requestDate DESC")
    List<RoomChangeRequest> findPendingRequests();
    List<RoomChangeRequest> findAllByOrderByRequestDateDesc();
}