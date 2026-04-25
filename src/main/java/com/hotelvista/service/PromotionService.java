package com.hotelvista.service;
import com.hotelvista.model.Promotion;
import com.hotelvista.repository.PromotionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class PromotionService {
    private final PromotionRepository repo;
    public PromotionService(PromotionRepository repo) {
        this.repo = repo;
    }
    public Promotion save(Promotion promotion) {
        return repo.save(promotion);
    }
    public Promotion findById(String id) {
        return repo.findById(id).orElse(null);
    }
    public List<Promotion> findAll() {
        return repo.findAll();
    }
    public List<Promotion> findAllByActive(boolean active) {
        return repo.findAllByIsActive(active);
    }
    public List<Promotion> findAllByPromotionNameContainingIgnoreCase(String promotionName) {
        return repo.findAllByPromotionNameContainingIgnoreCase(promotionName);
    }
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}