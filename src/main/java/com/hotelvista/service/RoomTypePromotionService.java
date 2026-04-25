package com.hotelvista.service;
import com.hotelvista.model.RoomTypePromotion;
import com.hotelvista.repository.RoomTypePromotionRepository;
import java.time.LocalDate;
import java.util.List;
public class RoomTypePromotionService {
    private final RoomTypePromotionRepository repo;
    public RoomTypePromotionService(RoomTypePromotionRepository repo) {
        this.repo = repo;
    }
    public RoomTypePromotion add(RoomTypePromotion roomTypePromotion) {
        if (roomTypePromotion == null || roomTypePromotion.getRoomTypeId() == null || roomTypePromotion.getPromotionId() == null) {
            throw new IllegalArgumentException("Room type ID and promotion ID are required");
        }
        return repo.save(roomTypePromotion);
    }
    public List<RoomTypePromotion> findAll() {
        return repo.findAll();
    }
    public RoomTypePromotion findById(Long id) {
        return repo.findById(id).orElse(null);
    }
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
    public List<RoomTypePromotion> findByRoomTypeId(String roomTypeId) {
        return repo.findByRoomTypeId(roomTypeId);
    }
    public List<RoomTypePromotion> findByPromotionId(String promotionId) {
        return repo.findByPromotionId(promotionId);
    }
    public List<RoomTypePromotion> findAllByStartDateAfterAndEndDateBefore(LocalDate startDateAfter, LocalDate endDateBefore) {
        return repo.findAllByStartDateAfterAndEndDateBefore(startDateAfter, endDateBefore);
    }
    public List<RoomTypePromotion> findActivePromotionsByRoomTypeID(String roomTypeId, LocalDate bookingDate) {
        return repo.findActivePromotionsByRoomTypeID(roomTypeId, bookingDate);
    }
}