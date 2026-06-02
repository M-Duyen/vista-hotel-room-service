package com.hotelvista.service;

import com.hotelvista.dto.HourlyRatePolicyDTO;
import com.hotelvista.dto.RoomTypePromotionDTO;
import com.hotelvista.dto.SeasonalPriceDTO;
import com.hotelvista.model.Room;
import com.hotelvista.model.RoomType;
import com.hotelvista.model.enums.DiscountType;
import com.hotelvista.model.enums.RoomStatus;
import com.hotelvista.repository.RoomRepository;
import com.hotelvista.repository.RoomTypeRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
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
    private final HourlyRatePolicyClientService hourlyRatePolicyClientService;

    public RoomService(RoomRepository roomRepo,
                       RoomTypeRepository roomTypeRepository,
                       SeasonalPriceClientService seasonalPriceClientService,
                       PromotionVoucherClientService promotionVoucherClientService,
                       HourlyRatePolicyClientService hourlyRatePolicyClientService) {
        this.roomRepo = roomRepo;
        this.roomTypeRepository = roomTypeRepository;
        this.seasonalPriceClientService = seasonalPriceClientService;
        this.promotionVoucherClientService = promotionVoucherClientService;
        this.hourlyRatePolicyClientService = hourlyRatePolicyClientService;
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
        if (roomType == null) {
            return 0.0;
        }

        String roomTypeId = roomType.getRoomTypeID();
        double basePrice = roomType.getBasePrice() != null ? roomType.getBasePrice() : 0.0;

        List<SeasonalPriceDTO> seasonalPrices =
                seasonalPriceClientService.findApplicableByRoomTypeIdAndDate(roomTypeId, date);
        List<RoomTypePromotionDTO> promotions =
                promotionVoucherClientService.findAllByDateAndRoomTypeId(date, roomTypeId);

        double seasonalIncrease = seasonalPrices.stream()
                .mapToDouble(season -> basePrice * (getMultiplier(season) - 1.0))
                .sum();
        double weekendIncrease = calculateWeekendSurcharge(basePrice, date);
        double promotionDiscount = promotions.stream()
                .mapToDouble(promotion -> calculatePromotionDiscount(promotion, basePrice))
                .sum();

        double finalPrice = basePrice + seasonalIncrease + weekendIncrease - promotionDiscount;
        System.out.println("roomTypeId: " + roomTypeId
                + ", basePrice: " + basePrice
                + ", seasonalIncrease: " + seasonalIncrease
                + ", weekendIncrease: " + weekendIncrease
                + ", promotionDiscount: " + promotionDiscount
                + ", finalPrice: " + finalPrice);
        return Math.max(0.0, finalPrice);
    }

    private double getMultiplier(SeasonalPriceDTO seasonalPrice) {
        return seasonalPrice != null && seasonalPrice.getPriceMultiplier() != null
                ? seasonalPrice.getPriceMultiplier()
                : 1.0;
    }

    private double calculatePromotionDiscount(RoomTypePromotionDTO roomTypePromotion, double basePrice) {
        if (roomTypePromotion == null || roomTypePromotion.getDiscountValue() == null) {
            return 0.0;
        }
        DiscountType discountType = roomTypePromotion.getPromotion() != null
                ? roomTypePromotion.getPromotion().getDiscountType()
                : null;
        if (discountType == DiscountType.FIXED) {
            return roomTypePromotion.getDiscountValue();
        }
        if (discountType == DiscountType.PERCENT) {
            return basePrice * roomTypePromotion.getDiscountValue() / 100.0;
        }
        return 0.0;
    }

    private double calculateWeekendSurcharge(double basePrice, LocalDate date) {
        if (date == null || basePrice <= 0) {
            return 0.0;
        }

        DayOfWeek bookingDay = date.getDayOfWeek();
        return hourlyRatePolicyClientService.findAll().stream()
                .filter(policy -> policy.getWeekendDays() != null && policy.getWeekendDays().contains(bookingDay))
                .findFirst()
                .map(policy -> basePrice * getWeekendSurchargePercent(policy) / 100.0)
                .orElse(0.0);
    }

    private double getWeekendSurchargePercent(HourlyRatePolicyDTO policy) {
        return policy.getWeekendSurcharge() != null ? policy.getWeekendSurcharge() : 0.0;
    }
}
