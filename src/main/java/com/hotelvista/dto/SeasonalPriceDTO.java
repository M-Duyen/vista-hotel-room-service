package com.hotelvista.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class SeasonalPriceDTO {
    private Integer id;
    private String seasonName;
    private Double priceMultiplier;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Set<String> roomTypeIds = new HashSet<>();
}
