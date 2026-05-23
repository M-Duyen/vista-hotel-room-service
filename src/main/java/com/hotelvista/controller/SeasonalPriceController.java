package com.hotelvista.controller;
import com.hotelvista.dto.PriceDTO;
import com.hotelvista.model.SeasonalPrice;
import com.hotelvista.service.SeasonalPriceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
public class SeasonalPriceController {
    private final SeasonalPriceService service;
    public SeasonalPriceController(SeasonalPriceService service) {
        this.service = service;
    }
    @GetMapping
    @PreAuthorize("hasAuthority('pricing_manage')")
    public List<SeasonalPrice> getSeasonalPrices() {
        return service.getAllSeasonalPrices();
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('pricing_manage')")
    public SeasonalPrice getSeasonalPrice(@PathVariable("id") int id) {
        return service.getSeasonalPriceById(id);
    }
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('pricing_manage')")
    public void saveSeasonalPrice(@RequestBody SeasonalPrice price) {
        service.saveSeasonalPrice(price);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('pricing_manage')")
    public ResponseEntity<String> deleteSeasonalPrice(@PathVariable("id") int id) {
        try {
            service.deleteSeasonalPrice(id);
            return ResponseEntity.ok("Xoa muc gia thanh cong!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/room-types")
    @PreAuthorize("hasAuthority('pricing_manage')")
    public List<PriceDTO> getSeasonalPricesWithRoomTypes() {
        return service.getAllSeasonalPrices_RoomType();
    }
    @GetMapping("/room-types/{id}")
    @PreAuthorize("hasAuthority('pricing_manage')")
    public PriceDTO getSeasonalPrice_RoomTypeById(@PathVariable int id) {
        return service.getSeasonalPrice_RoomTypeById(id);
    }
    @PostMapping("/save-with-room-types")
    @PreAuthorize("hasAuthority('pricing_manage')")
    public ResponseEntity<?> create(@RequestBody PriceDTO req) {
        try {
            SeasonalPrice sp = service.createOrUpdateSeasonPrice(req);
            return ResponseEntity.ok(sp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
