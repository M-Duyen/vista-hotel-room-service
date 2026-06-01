package com.hotelvista.service.client;

import com.hotelvista.dto.SeasonalPriceDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class SeasonalPriceClientService {
    private final RestTemplate restTemplate;

    private final String baseUrl = "lb://PRINCIPLE-SERVICE/seasonal-prices";

    public SeasonalPriceClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<SeasonalPriceDTO> getSeasonalPriceByRoomTypeId(String roomTypeId, LocalDate date) {
        return restTemplate.exchange(
                baseUrl + "/room-type/" + roomTypeId + "?date=" + date,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SeasonalPriceDTO>>() {
                }
        ).getBody();
    }
}
