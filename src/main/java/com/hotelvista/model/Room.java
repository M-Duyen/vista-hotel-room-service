package com.hotelvista.model;
import com.hotelvista.model.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rooms")
public class Room {
    @Id
    @Column(name = "room_number")
    private String roomNumber;
    private Integer floor;
    @Enumerated(EnumType.STRING)
    private RoomStatus status;
    @Column(name = "last_cleaned")
    private LocalDateTime lastCleaned;
    private String notes;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;
    @ElementCollection
    @CollectionTable(name = "room_images", joinColumns = @JoinColumn(name = "room_number"))
    @Column(name = "images_url")
    private List<String> images;
}
