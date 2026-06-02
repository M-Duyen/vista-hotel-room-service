package com.hotelvista.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.hotelvista.model.enums.DiscountType;
import lombok.Data;

@Data
public class PromotionDTO {
    @JsonAlias("promotionID")
    private String promotionId;
    private String promotionName;
    private String description;
    private DiscountType discountType;
    private boolean active;
}
