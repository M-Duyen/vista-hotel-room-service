package com.hotelvista.service;

import com.hotelvista.dto.HourlyRatePolicyDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class HourlyRatePolicyClientService {
    private static final String BASE_URL = "lb://principle-service/hourly-rate-policies";

    private final RestTemplate restTemplate;

    public HourlyRatePolicyClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<HourlyRatePolicyDTO> findAll() {
        try {
            HourlyRatePolicyDTO[] policies = restTemplate.getForObject(BASE_URL, HourlyRatePolicyDTO[].class);
            return policies == null ? List.of() : Arrays.asList(policies);
        } catch (RestClientException ex) {
            System.err.println("Cannot load hourly rate policies: " + ex.getMessage());
            return List.of();
        }
    }
}
