package com.hotelvista.repository;
import com.hotelvista.model.RoomTypePromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
public interface RoomTypePromotionRepository extends JpaRepository<RoomTypePromotion, Long> {
    List<RoomTypePromotion> findByRoomTypeId(String roomTypeId);
    List<RoomTypePromotion> findByPromotionId(String promotionId);
    List<RoomTypePromotion> findAllByStartDateAfterAndEndDateBefore(LocalDate startDateAfter, LocalDate endDateBefore);
    @Query("SELECT rtp FROM RoomTypePromotion rtp WHERE rtp.roomTypeId = :roomTypeId AND rtp.startDate <= :bookingDate AND rtp.endDate >= :bookingDate")
    List<RoomTypePromotion> findActivePromotionsByRoomTypeID(@Param("roomTypeId") String roomTypeId, @Param("bookingDate") LocalDate bookingDate);
}