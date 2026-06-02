package com.hotelvista.service;

import com.hotelvista.dto.RoomTypePromotionDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class PromotionVoucherClientService {
    private static final String BASE_URL = "lb://service-promotion-voucher/api/room-type-promotions";

    private final RestTemplate restTemplate;

    public PromotionVoucherClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RoomTypePromotionDTO> findAllByDateAndRoomTypeId(LocalDate date, String roomTypeId) {
        try {
            RoomTypePromotionDTO[] response = restTemplate.getForObject(
                    BASE_URL + "/date-room-type?date={date}&roomTypeId={roomTypeId}",
                    RoomTypePromotionDTO[].class,
                    date,
                    roomTypeId
            );
            return response == null ? List.of() : Arrays.asList(response);
        } catch (RestClientException ex) {
            System.err.println("Cannot load promotions for roomType " + roomTypeId + ": " + ex.getMessage());
            return List.of();
        }
    }
}
