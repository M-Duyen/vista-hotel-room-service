package com.hotelvista.controller;
import com.hotelvista.model.RoomTypePromotion;
import com.hotelvista.service.RoomTypePromotionService;
import com.hotelvista.util.ValidatorsUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
public class RoomTypePromotionController {
    private final RoomTypePromotionService service;
    public RoomTypePromotionController(RoomTypePromotionService service) {
        this.service = service;
    }
    @GetMapping
    @PreAuthorize("permitAll()")
    public List<RoomTypePromotion> findAll() {
        return service.findAll();
    }
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public RoomTypePromotion findById(@PathVariable Long id) {
        return service.findById(id);
    }
    @GetMapping("/room-type/{roomTypeId}")
    @PreAuthorize("permitAll()")
    public List<RoomTypePromotion> findByRoomTypeId(@PathVariable String roomTypeId) {
        return service.findByRoomTypeId(roomTypeId);
    }
    @GetMapping("/promotion/{promotionId}")
    @PreAuthorize("permitAll()")
    public List<RoomTypePromotion> findByPromotionId(@PathVariable String promotionId) {
        return service.findByPromotionId(promotionId);
    }
    @GetMapping("/active/{roomTypeId}")
    @PreAuthorize("permitAll()")
    public List<RoomTypePromotion> findActivePromotionsByRoomTypeID(@PathVariable String roomTypeId,
                                                                    @RequestParam LocalDate bookingDate) {
        return service.findActivePromotionsByRoomTypeID(roomTypeId, bookingDate);
    }
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('promotion_manage')")
    public ResponseEntity<?> save(@RequestBody RoomTypePromotion roomTypePromotion) {
        String roomTypeError = ValidatorsUtil.validateRequired(
                roomTypePromotion.getRoomTypeId(),
                "Room type"
        );
        if (roomTypeError != null) return ResponseEntity.badRequest().body(roomTypeError);
        String promotionError = ValidatorsUtil.validateRequired(
                roomTypePromotion.getPromotionId(),
                "Promotion"
        );
        if (promotionError != null) return ResponseEntity.badRequest().body(promotionError);
        String discountError = ValidatorsUtil.validateDiscountPercentage(roomTypePromotion.getDiscountValue());
        if (discountError != null) return ResponseEntity.badRequest().body(discountError);
        String startDateError = ValidatorsUtil.validateStartDate(roomTypePromotion.getStartDate());
        if (startDateError != null) return ResponseEntity.badRequest().body(startDateError);
        String endDateError = ValidatorsUtil.validateEndDate(roomTypePromotion.getEndDate());
        if (endDateError != null) return ResponseEntity.badRequest().body(endDateError);
        String dateRangeError = ValidatorsUtil.validatePromotionDateRange(roomTypePromotion.getStartDate(), roomTypePromotion.getEndDate());
        if (dateRangeError != null) return ResponseEntity.badRequest().body(dateRangeError);
        return ResponseEntity.ok(service.add(roomTypePromotion));
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('promotion_manage')")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
}
