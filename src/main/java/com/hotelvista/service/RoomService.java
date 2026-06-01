package com.hotelvista.service;

import com.hotelvista.dto.PromotionDTO;
import com.hotelvista.dto.RoomTypePromotionDTO;
import com.hotelvista.dto.SeasonalPriceDTO;
import com.hotelvista.model.Room;
import com.hotelvista.model.RoomType;
import com.hotelvista.model.enums.DiscountType;
import com.hotelvista.model.enums.RoomStatus;
import com.hotelvista.repository.RoomRepository;
import com.hotelvista.repository.RoomTypeRepository;
import com.hotelvista.service.client.PromotionVoucherClientService;
import com.hotelvista.service.client.SeasonalPriceClientService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepo;
    private final RoomTypeRepository roomTypeRepository;
    private final SeasonalPriceClientService seasonalPriceClientService;
    private final PromotionVoucherClientService promotionVoucherClientService;

    public RoomService(RoomRepository roomRepo,
                       RoomTypeRepository roomTypeRepository,
                       SeasonalPriceClientService seasonalPriceClientService,
                       PromotionVoucherClientService promotionVoucherClientService) {
        this.roomRepo = roomRepo;
        this.roomTypeRepository = roomTypeRepository;
        this.seasonalPriceClientService = seasonalPriceClientService;
        this.promotionVoucherClientService = promotionVoucherClientService;
    }

    public Room findById(String id) {
        return roomRepo.findById(id).orElse(null);
    }

    public List<Room> selectAll() {
        return roomRepo.findAll();
    }

    public Optional<Room> selectById(String id) {
        return roomRepo.findById(id);
    }

    public Room insertOrUpdate(Room room) {
        if (room.getRoomType() != null && room.getRoomType().getRoomTypeID() != null) {
            RoomType managedType = roomTypeRepository.findById(room.getRoomType().getRoomTypeID())
                    .orElseThrow(() -> new IllegalArgumentException("Room type not found"));
            room.setRoomType(managedType);
        }
        return roomRepo.save(room);
    }

    public void delete(String id) {
        roomRepo.deleteById(id);
    }

    public void save(Room room) {
        roomRepo.save(room);
    }

    public void updateStatus(String id, RoomStatus status) {
        Room room = roomRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Room not found"));
        room.setStatus(status);
        roomRepo.save(room);
    }

    public Map<String, Double> calculateRoomPrice(List<String> roomIds, LocalDate date) {
        Map<String, Double> prices = new HashMap<>();
        for (String roomId : roomIds) {
            Room room = roomRepo.findById(roomId)
                    .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
            prices.put(roomId, calculatePriceByRoomType(room.getRoomType(), date));
        }
        return prices;
    }

    public double calculatePriceByRoomType(RoomType roomType, LocalDate date) {
        if (roomType == null || roomType.getRoomTypeID() == null) {
            throw new IllegalArgumentException("Room type is required");
        }

        double basePrice = roomType.getBasePrice() != null ? roomType.getBasePrice() : 0.0;
        String roomTypeId = roomType.getRoomTypeID();

        List<SeasonalPriceDTO> seasonalPrices = Optional
                .ofNullable(seasonalPriceClientService.getSeasonalPriceByRoomTypeId(roomTypeId, date))
                .orElse(Collections.emptyList());
        double seasonalAdjustment = seasonalPrices.stream()
                .mapToDouble(seasonalPrice -> {
                    double multiplier = seasonalPrice.getPriceMultiplier() != null
                            ? seasonalPrice.getPriceMultiplier()
                            : 1.0;
                    return basePrice * (multiplier - 1.0);
                })
                .sum();

        List<RoomTypePromotionDTO> promotionList = Optional
                .ofNullable(promotionVoucherClientService.findAllByDateAndRoomTypeId(date, roomTypeId))
                .orElse(Collections.emptyList());
        double promotionDiscount = promotionList.stream()
                .mapToDouble(promotion -> calculatePromotionDiscount(promotion, basePrice))
                .sum();

        return Math.max(0.0, basePrice + seasonalAdjustment - promotionDiscount);
    }

    private double calculatePromotionDiscount(RoomTypePromotionDTO roomTypePromotion, double basePrice) {
        if (roomTypePromotion == null || roomTypePromotion.getDiscountValue() == null) {
            return 0.0;
        }

        PromotionDTO promotion = roomTypePromotion.getPromotion();
        if (promotion == null && roomTypePromotion.getId() != null) {
            promotion = promotionVoucherClientService.findById(roomTypePromotion.getId().getPromotionId());
        }
        if (promotion == null || promotion.getDiscountType() == null) {
            return 0.0;
        }

        DiscountType discountType = promotion.getDiscountType();
        return switch (discountType) {
            case PERCENT -> basePrice * roomTypePromotion.getDiscountValue() / 100.0;
            case FIXED -> roomTypePromotion.getDiscountValue();
        };
    }
}
