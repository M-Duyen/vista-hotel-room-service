package com.hotelvista.priciple.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// Duplicate copy of the model under com.hotelvista.model. Keep as a POJO
// reference only: remove JPA annotations so it is not treated as an entity
// during auto-scanning and to avoid name collisions with the primary
// `com.hotelvista.model.RoomTypePromotion` entity.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypePromotion {
    private Long id;

    private String roomTypeId;

    private String promotionId;

    private Double discountValue;

    private LocalDate startDate;

    private LocalDate endDate;
}

