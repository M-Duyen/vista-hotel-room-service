package com.hotelvista.controller;
import com.hotelvista.dto.PriceDTO;
import com.hotelvista.model.SeasonalPrice;
import com.hotelvista.service.SeasonalPriceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
public class SeasonalPriceController {
    private final SeasonalPriceService service;
    public SeasonalPriceController(SeasonalPriceService service) {
        this.service = service;
    }
    @GetMapping
    public List<SeasonalPrice> getSeasonalPrices() {
        return service.getAllSeasonalPrices();
    }
    @GetMapping("/{id}")
    public SeasonalPrice getSeasonalPrice(@PathVariable("id") int id) {
        return service.getSeasonalPriceById(id);
    }
    @PostMapping("/save")
    public void saveSeasonalPrice(@RequestBody SeasonalPrice price) {
        service.saveSeasonalPrice(price);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSeasonalPrice(@PathVariable("id") int id) {
        try {
            service.deleteSeasonalPrice(id);
            return ResponseEntity.ok("Xoa muc gia thanh cong!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/room-types")
    public List<PriceDTO> getSeasonalPricesWithRoomTypes() {
        return service.getAllSeasonalPrices_RoomType();
    }
    @GetMapping("/room-types/{id}")
    public PriceDTO getSeasonalPrice_RoomTypeById(@PathVariable int id) {
        return service.getSeasonalPrice_RoomTypeById(id);
    }
    @PostMapping("/save-with-room-types")
    public ResponseEntity<?> create(@RequestBody PriceDTO req) {
        try {
            SeasonalPrice sp = service.createOrUpdateSeasonPrice(req);
            return ResponseEntity.ok(sp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
