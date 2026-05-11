package com.hotelvista.config;

import com.hotelvista.model.Promotion;
import com.hotelvista.model.Room;
import com.hotelvista.model.RoomChangeRequest;
import com.hotelvista.model.RoomType;
import com.hotelvista.model.RoomTypePromotion;
import com.hotelvista.model.SeasonalPrice;
import com.hotelvista.model.enums.DiscountType;
import com.hotelvista.model.enums.RequestStatus;
import com.hotelvista.model.enums.RoomStatus;
import com.hotelvista.repository.PromotionRepository;
import com.hotelvista.repository.RoomChangeRequestRepository;
import com.hotelvista.repository.RoomRepository;
import com.hotelvista.repository.RoomTypePromotionRepository;
import com.hotelvista.repository.RoomTypeRepository;
import com.hotelvista.repository.RoomTypeSeasonalPriceRepository;
import com.hotelvista.repository.SeasonalPriceRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final PromotionRepository promotionRepository;
    private final RoomTypePromotionRepository roomTypePromotionRepository;
    private final SeasonalPriceRepository seasonalPriceRepository;
    private final RoomTypeSeasonalPriceRepository roomTypeSeasonalPriceRepository;
    private final RoomChangeRequestRepository roomChangeRequestRepository;
    private final JdbcTemplate jdbcTemplate;

    public DataSeeder(RoomTypeRepository roomTypeRepository,
                      RoomRepository roomRepository,
                      PromotionRepository promotionRepository,
                      RoomTypePromotionRepository roomTypePromotionRepository,
                      SeasonalPriceRepository seasonalPriceRepository,
                      RoomTypeSeasonalPriceRepository roomTypeSeasonalPriceRepository,
                      RoomChangeRequestRepository roomChangeRequestRepository,
                      JdbcTemplate jdbcTemplate) {
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
        this.promotionRepository = promotionRepository;
        this.roomTypePromotionRepository = roomTypePromotionRepository;
        this.seasonalPriceRepository = seasonalPriceRepository;
        this.roomTypeSeasonalPriceRepository = roomTypeSeasonalPriceRepository;
        this.roomChangeRequestRepository = roomChangeRequestRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (roomTypeRepository.count() > 0L) {
            return;
        }

        ensureRoomTypeSeasonalPriceTable();
        seedRoomTypes();
        seedPromotions();
        seedRooms();
        seedRoomTypePromotions();
        seedSeasonalPrices();
        seedRoomChangeRequests();
    }

    private void seedRoomTypes() {
        roomTypeRepository.saveAll(List.of(
                new RoomType(
                        "RT-STD",
                        "Standard Twin",
                        "Phòng tiêu chuẩn cho 2 khách, phù hợp lưu trú ngắn ngày.",
                        24.0,
                        2,
                        List.of("Wifi tốc độ cao", "TV thông minh", "Điều hòa", "Bàn làm việc"),
                        650000.0,
                        null
                ),
                new RoomType(
                        "RT-DEL",
                        "Deluxe King",
                        "Phòng deluxe rộng rãi với giường king và minibar.",
                        32.0,
                        3,
                        List.of("Wifi tốc độ cao", "TV 50 inch", "Minibar", "Bồn tắm"),
                        950000.0,
                        null
                ),
                new RoomType(
                        "RT-SUI",
                        "Family Suite",
                        "Suite gia đình có khu vực sinh hoạt riêng và ban công.",
                        48.0,
                        4,
                        List.of("Wifi tốc độ cao", "Phòng khách riêng", "Minibar", "Ban công", "Bồn tắm"),
                        1600000.0,
                        null
                )
        ));
    }

    private void seedPromotions() {
        promotionRepository.saveAll(List.of(
                new Promotion("PROMO-TET", "Tết đoàn viên", "Giảm giá đặc biệt cho kỳ nghỉ Tết.", DiscountType.PERCENT, true),
                new Promotion("PROMO-WEEKEND", "Weekend Escape", "Khuyến mãi cố định cho cuối tuần.", DiscountType.FIXED, true),
                new Promotion("PROMO-SUMMER", "Summer Splash", "Ưu đãi mùa hè dành cho suite cao cấp.", DiscountType.PERCENT, true)
        ));
    }

    private void seedRooms() {
        RoomType standard = roomTypeRepository.findById("RT-STD").orElseThrow();
        RoomType deluxe = roomTypeRepository.findById("RT-DEL").orElseThrow();
        RoomType suite = roomTypeRepository.findById("RT-SUI").orElseThrow();

        roomRepository.saveAll(List.of(
                new Room("101", 1, RoomStatus.AVAILABLE, LocalDateTime.now().minusDays(1), "Gần thang máy", standard, List.of("https://img.hotelvista.vn/rooms/101-1.jpg", "https://img.hotelvista.vn/rooms/101-2.jpg")),
                new Room("102", 1, RoomStatus.CLEANING, LocalDateTime.now().minusHours(5), "Đang dọn phòng", standard, List.of("https://img.hotelvista.vn/rooms/102-1.jpg")),
                new Room("201", 2, RoomStatus.BOOKED, LocalDateTime.now().minusDays(2), "View thành phố", deluxe, List.of("https://img.hotelvista.vn/rooms/201-1.jpg", "https://img.hotelvista.vn/rooms/201-2.jpg")),
                new Room("202", 2, RoomStatus.AVAILABLE, LocalDateTime.now().minusHours(8), "Phòng góc yên tĩnh", deluxe, List.of("https://img.hotelvista.vn/rooms/202-1.jpg")),
                new Room("301", 3, RoomStatus.MAINTENANCE, LocalDateTime.now().minusDays(3), "Kiểm tra điều hòa", suite, List.of("https://img.hotelvista.vn/rooms/301-1.jpg")),
                new Room("302", 3, RoomStatus.AVAILABLE, LocalDateTime.now().minusHours(12), "Phòng suite gia đình", suite, List.of("https://img.hotelvista.vn/rooms/302-1.jpg", "https://img.hotelvista.vn/rooms/302-2.jpg"))
        ));
    }

    private void seedRoomTypePromotions() {
        if (roomTypePromotionRepository.findByRoomTypeId("RT-STD").isEmpty()) {
            roomTypePromotionRepository.save(new RoomTypePromotion(null, "RT-STD", "PROMO-TET", 10.0, LocalDate.of(2026, 1, 15), LocalDate.of(2026, 2, 15)));
        }
        if (roomTypePromotionRepository.findByRoomTypeId("RT-DEL").isEmpty()) {
            roomTypePromotionRepository.save(new RoomTypePromotion(null, "RT-DEL", "PROMO-WEEKEND", 12.5, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 8, 31)));
        }
        if (roomTypePromotionRepository.findByRoomTypeId("RT-SUI").isEmpty()) {
            roomTypePromotionRepository.save(new RoomTypePromotion(null, "RT-SUI", "PROMO-SUMMER", 18.0, LocalDate.of(2026, 6, 1), LocalDate.of(2026, 9, 30)));
        }
    }

    private void seedSeasonalPrices() {
        SeasonalPrice spring = seasonalPriceRepository.save(new SeasonalPrice(0, "SPRING", 1.10, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 4, 30), "Mùa lễ hội đầu năm"));
        SeasonalPrice summer = seasonalPriceRepository.save(new SeasonalPrice(0, "SUMMER", 1.25, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 8, 31), "Mùa cao điểm du lịch hè"));
        SeasonalPrice holiday = seasonalPriceRepository.save(new SeasonalPrice(0, "HOLIDAY", 1.35, LocalDate.of(2026, 12, 20), LocalDate.of(2027, 1, 5), "Mùa lễ cuối năm"));

        linkSeasonalPrice("RT-STD", spring.getId());
        linkSeasonalPrice("RT-DEL", summer.getId());
        linkSeasonalPrice("RT-SUI", summer.getId());
        linkSeasonalPrice("RT-SUI", holiday.getId());
    }

    private void seedRoomChangeRequests() {
        if (roomChangeRequestRepository.count() > 0L) {
            return;
        }

        roomChangeRequestRepository.saveAll(List.of(
                new RoomChangeRequest(
                        "RCR-001",
                        "BK-1001",
                        "CUST-001",
                        "101",
                        "201",
                        "Khách muốn nâng cấp lên phòng deluxe có view đẹp hơn.",
                        LocalDateTime.now().minusDays(2),
                        RequestStatus.COMPLETED,
                        "Đã chuyển sang phòng 201 và cập nhật booking.",
                        LocalDateTime.now().minusDays(1),
                        "admin01"
                ),
                new RoomChangeRequest(
                        "RCR-002",
                        "BK-1002",
                        "CUST-002",
                        "202",
                        "302",
                        "Gia đình cần phòng rộng hơn để ở dài ngày.",
                        LocalDateTime.now().minusHours(10),
                        RequestStatus.PENDING,
                        null,
                        null,
                        null
                )
        ));
    }

    private void linkSeasonalPrice(String roomTypeId, Integer seasonalPriceId) {
        roomTypeSeasonalPriceRepository.insertSeasonalPriceRoomType(roomTypeId, seasonalPriceId);
    }

    private void ensureRoomTypeSeasonalPriceTable() {
        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS room_type_seasonal_price (
                    room_type_id VARCHAR(255) NOT NULL,
                    seasonal_price_id INT NOT NULL,
                    PRIMARY KEY (room_type_id, seasonal_price_id)
                )
                """);
    }
}


