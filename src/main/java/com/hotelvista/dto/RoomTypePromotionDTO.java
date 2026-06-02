package com.hotelvista.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypePromotionDTO {
    private RoomTypePromotionId id;
    private PromotionDTO promotion;
    private Double discountValue;
    private LocalDate startDate;
    private LocalDate endDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomTypePromotionId {
        private String promotionId;
        private String roomTypeId;
    }
}
