package com.hotelvista.model;
import com.hotelvista.model.enums.DiscountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "promotions")
public class Promotion {
    @Id
    @Column(name = "promotion_id")
    private String promotionID;
    @Column(name = "promotion_name", columnDefinition = "NVARCHAR(255)")
    private String promotionName;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;
    @Column(name = "is_active")
    private boolean isActive = true;
}