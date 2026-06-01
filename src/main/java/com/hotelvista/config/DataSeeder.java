package com.hotelvista.config;

import com.hotelvista.model.Promotion;
import com.hotelvista.model.Room;
import com.hotelvista.model.RoomChangeRequest;
import com.hotelvista.model.RoomType;
import com.hotelvista.model.SeasonalPrice;
import com.hotelvista.model.enums.DiscountType;
import com.hotelvista.model.enums.RequestStatus;
import com.hotelvista.model.enums.RoomStatus;
import com.hotelvista.repository.RoomChangeRequestRepository;
import com.hotelvista.repository.RoomRepository;
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
    private final SeasonalPriceRepository seasonalPriceRepository;
    private final RoomTypeSeasonalPriceRepository roomTypeSeasonalPriceRepository;
    private final RoomChangeRequestRepository roomChangeRequestRepository;
    private final JdbcTemplate jdbcTemplate;

    public DataSeeder(RoomTypeRepository roomTypeRepository,
                      RoomRepository roomRepository,
                      SeasonalPriceRepository seasonalPriceRepository,
                      RoomTypeSeasonalPriceRepository roomTypeSeasonalPriceRepository,
                      RoomChangeRequestRepository roomChangeRequestRepository,
                      JdbcTemplate jdbcTemplate) {
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
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
        seedRooms();
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
                        null,
                        null,
                        null,
                        null,
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
                        null,
                        null,
                        null,
                        null,
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
                        null,
                        null,
                        null,
                        null,
                        null
                )
        ));
    }


    private void seedRooms() {
        RoomType standard = roomTypeRepository.findById("RT-STD").orElseThrow();
        RoomType deluxe = roomTypeRepository.findById("RT-DEL").orElseThrow();
        RoomType suite = roomTypeRepository.findById("RT-SUI").orElseThrow();

        roomRepository.saveAll(List.of(
                new Room("101", 1, RoomStatus.AVAILABLE, LocalDateTime.now().minusDays(1), "Gần thang máy", standard, List.of(
                        "https://res.cloudinary.com/dk8gvar3y/image/upload/v1765326155/pexels-photo-1457842_bxn0q2.jpg",
                        "https://res.cloudinary.com/dk8gvar3y/image/upload/v1765326150/pexels-photo-164595_ldxdih.jpg"
                )),
                new Room("102", 1, RoomStatus.CLEANING, LocalDateTime.now().minusHours(5), "Đang dọn phòng", standard, List.of(
                        "https://res.cloudinary.com/dk8gvar3y/image/upload/v1765326134/pexels-photo-1838554_zgrpcj.jpg"
                )),
                new Room("201", 2, RoomStatus.BOOKED, LocalDateTime.now().minusDays(2), "View thành phố", deluxe, List.of(
                        "https://res.cloudinary.com/dk8gvar3y/image/upload/v1765326092/pexels-photo-262048_fpdy6s.jpg",
                        "https://res.cloudinary.com/dk8gvar3y/image/upload/v1763549658/cosy-2648851_1280_hborix.jpg"
                )),
                new Room("202", 2, RoomStatus.AVAILABLE, LocalDateTime.now().minusHours(8), "Phòng góc yên tĩnh", deluxe, List.of(
                        "https://res.cloudinary.com/dk8gvar3y/image/upload/v1763549646/living-room-4809590_1280_avtvye.jpg"
                )),
                new Room("301", 3, RoomStatus.MAINTENANCE, LocalDateTime.now().minusDays(3), "Kiểm tra điều hòa", suite, List.of(
                        "https://res.cloudinary.com/dk8gvar3y/image/upload/v1763549643/curtain-1758853_1280_exc0tv.jpg",
                        "https://res.cloudinary.com/dssdirbwj/image/upload/v1778302825/vistal-hotel/iynseaopqclfrtxhl2tr.jpg",
                        "https://res.cloudinary.com/dssdirbwj/image/upload/v1778302824/vistal-hotel/cfgjyajn5hvpgrieojr8.jpg"
                )),
                new Room("302", 3, RoomStatus.AVAILABLE, LocalDateTime.now().minusHours(12), "Phòng suite gia đình", suite, List.of(
                        "https://res.cloudinary.com/dk8gvar3y/image/upload/v1763549637/hotel-room-5858067_1280_auoi0o.jpg",
                        "https://res.cloudinary.com/dk8gvar3y/image/upload/v1763548293/interior-8813803_1280_vrvnji.jpg",
                        "https://res.cloudinary.com/dssdirbwj/image/upload/v1778296990/vistal-hotel/f6qvomay7u2c2ocejg2s.webp"
                ))
        ));
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


