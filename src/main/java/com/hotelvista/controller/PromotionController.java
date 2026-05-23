package com.hotelvista.controller;
import com.hotelvista.model.Promotion;
import com.hotelvista.service.PromotionService;
import com.hotelvista.util.ValidatorsUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/promotions")
public class PromotionController {
    private final PromotionService service;
    public PromotionController(PromotionService service) {
        this.service = service;
    }
    @GetMapping
    @PreAuthorize("permitAll()")
    public List<Promotion> findAll() {
        return service.findAll();
    }
    @GetMapping("/active")
    @PreAuthorize("permitAll()")
    public List<Promotion> findAllByActive(@RequestParam(defaultValue = "true") boolean active) {
        return service.findAllByActive(active);
    }
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public Promotion findById(@PathVariable String id) {
        return service.findById(id);
    }
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('promotion_manage')")
    public ResponseEntity<?> save(@RequestBody Promotion promotion) {
        String idError = ValidatorsUtil.validatePromotionId(promotion.getPromotionID());
        if (idError != null) return ResponseEntity.badRequest().body(idError);
        String nameError = ValidatorsUtil.validatePromotionName(promotion.getPromotionName());
        if (nameError != null) return ResponseEntity.badRequest().body(nameError);
        String descError = ValidatorsUtil.validateDescription(promotion.getDescription());
        if (descError != null) return ResponseEntity.badRequest().body(descError);
        return ResponseEntity.ok(service.save(promotion));
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('promotion_manage')")
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }
}
