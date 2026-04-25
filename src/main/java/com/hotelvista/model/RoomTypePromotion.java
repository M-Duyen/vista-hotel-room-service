package com.hotelvista.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room_type_promotions")
public class RoomTypePromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "room_type_id", nullable = false)
    private String roomTypeId;
    @Column(name = "promotion_id", nullable = false)
    private String promotionId;
    @Column(name = "discount_value")
    private Double discountValue;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
}