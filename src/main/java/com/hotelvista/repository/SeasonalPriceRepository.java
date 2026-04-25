package com.hotelvista.repository;
import com.hotelvista.model.SeasonalPrice;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SeasonalPriceRepository extends JpaRepository<SeasonalPrice, Integer> {
}
