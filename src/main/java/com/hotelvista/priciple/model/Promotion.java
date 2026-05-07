package com.hotelvista.priciple.model;

import com.hotelvista.priciple.model.enums.DiscountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// This class is a duplicate copy of the main entity in com.hotelvista.model
// and is kept here for reference only. Remove JPA annotations so it is
// not picked up as an entity during scanning and does not conflict with
// the primary `com.hotelvista.model.Promotion` entity.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Promotion {
    private String promotionId;

    @Column(name = "promotion_name", nullable = false)
    private String promotionName;

    @Column(name = "description")
    private String description;

    private DiscountType discountType;

    private boolean active = true;
}

