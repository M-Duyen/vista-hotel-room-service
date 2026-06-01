package com.hotelvista.repository;

import com.hotelvista.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface RoomTypeRepository extends JpaRepository<RoomType, String> {
    /**
     * Tính giá khi đã áp dụng tăng giá theo mùa
     *
     * @param roomTypeId
     * @param bookingDate
     * @return
     */
    @Query("SELECT MAX(rt.basePrice * sp.priceMultiplier) " +
            "FROM RoomType rt JOIN rt.seasonalPrices sp " +
            "WHERE rt.roomTypeID = :roomTypeId " +
            "   AND sp.startDate <= :bookingDate " +
            "   AND sp.endDate >= :bookingDate")
    Double calculateDiscountedPrice(@Param("roomTypeId") String roomTypeId,
                                    @Param("bookingDate") LocalDate bookingDate);



}
