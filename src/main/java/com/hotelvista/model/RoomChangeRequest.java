package com.hotelvista.model;
import com.hotelvista.model.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Entity
@Table(name = "room_change_requests")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomChangeRequest {
    @Id
    @Column(name = "request_id")
    private String requestID;
    @Column(name = "booking_id")
    private String bookingId;
    @Column(name = "customer_id")
    private String customerId;
    @Column(name = "current_room_number")
    private String currentRoomNumber;
    @Column(name = "new_room_number")
    private String newRoomNumber;
    @Column(name = "reason", columnDefinition = "NVARCHAR(500)")
    private String reason;
    @Column(name = "request_date")
    private LocalDateTime requestDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;
    @Column(name = "response_note", columnDefinition = "NVARCHAR(500)")
    private String responseNote;
    @Column(name = "response_date")
    private LocalDateTime responseDate;
    @Column(name = "processed_by")
    private String processedBy;
}