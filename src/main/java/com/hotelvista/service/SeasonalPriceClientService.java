package com.hotelvista.service;

import com.hotelvista.dto.SeasonalPriceDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class SeasonalPriceClientService {
    private static final String BASE_URL = "lb://principle-service/seasonal-prices";

    private final RestTemplate restTemplate;

    public SeasonalPriceClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<SeasonalPriceDTO> findApplicableByRoomTypeIdAndDate(String roomTypeId, LocalDate date) {
        try {
            SeasonalPriceDTO[] response = restTemplate.getForObject(
                    BASE_URL + "/room-type/{roomTypeId}?date={date}",
                    SeasonalPriceDTO[].class,
                    roomTypeId,
                    date
            );
            return response == null ? List.of() : Arrays.asList(response);
        } catch (RestClientException ex) {
            System.err.println("Cannot load seasonal prices for roomType " + roomTypeId + ": " + ex.getMessage());
            return List.of();
        }
    }
}
