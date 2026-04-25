package com.hotelvista.service;
import com.hotelvista.dto.PriceDTO;
import com.hotelvista.model.SeasonalPrice;
import com.hotelvista.repository.SeasonalPriceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
public class SeasonalPriceService {
    private final SeasonalPriceRepository repo;
    public SeasonalPriceService(SeasonalPriceRepository repo) {
        this.repo = repo;
    }
    public List<SeasonalPrice> getAllSeasonalPrices() {
        return repo.findAll();
    }
    public SeasonalPrice getSeasonalPriceById(Integer id) {
        return repo.findById(id).orElse(null);
    }
    public void saveSeasonalPrice(SeasonalPrice price) {
        repo.save(price);
    }
    public void deleteSeasonalPrice(int id) {
        repo.deleteById(id);
    }
    public List<PriceDTO> getAllSeasonalPrices_RoomType() {
        return repo.findAll().stream().map(this::convertToDTO).toList();
    }
    public PriceDTO getSeasonalPrice_RoomTypeById(int id) {
        SeasonalPrice sp = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Khong tim thay gia ID: " + id));
        return convertToDTO(sp);
    }
    @Transactional
    public SeasonalPrice createOrUpdateSeasonPrice(PriceDTO dto) {
        if (dto == null || dto.getSeasonalPrice() == null) {
            throw new IllegalArgumentException("Seasonal price is required");
        }
        return repo.saveAndFlush(dto.getSeasonalPrice());
    }
    private PriceDTO convertToDTO(SeasonalPrice sp) {
        return new PriceDTO(sp, List.of());
    }
}
