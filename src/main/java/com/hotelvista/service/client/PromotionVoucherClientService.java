package com.hotelvista.service.client;

import com.hotelvista.dto.PromotionDTO;
import com.hotelvista.dto.RoomTypePromotionDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class PromotionVoucherClientService {
    private final RestTemplate restTemplate;

    private final String baseUrl = "lb://SERVICE-PROMOTION-VOUCHER/api/room-type-promotions";
    private final String baseUrlPromotion = "lb://SERVICE-PROMOTION-VOUCHER/api/promotions";

    public PromotionVoucherClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PromotionDTO findById(String id) {
        return restTemplate.getForObject(baseUrlPromotion + "/find/" + id, PromotionDTO.class);
    }

    public List<RoomTypePromotionDTO> findAllByDateAndRoomTypeId(LocalDate date, String roomTypeId) {
        return restTemplate.exchange(
                baseUrl + "/date-room-type?date=" + date + "&roomTypeId=" + roomTypeId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RoomTypePromotionDTO>>() {
                }
        ).getBody();
    }
}
