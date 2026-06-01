package com.hotelvista.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.hotelvista.model.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDTO {
    @JsonAlias("promotionID")
    private String promotionId;
    private String promotionName;
    private String description;
    private DiscountType discountType;
    private boolean active;
}
