package com.hotelvista.dto;

import lombok.Data;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class HourlyRatePolicyDTO {
    private Long id;
    private String policyName;
    private Double weekendSurcharge;
    private Set<DayOfWeek> weekendDays = new HashSet<>();
    private Map<Integer, Double> baseRates = new HashMap<>();
}
