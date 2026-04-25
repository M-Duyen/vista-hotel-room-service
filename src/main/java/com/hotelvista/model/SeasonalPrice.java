package com.hotelvista.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seasonal_prices")
public class SeasonalPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String seasonName;
    @Column(name = "price_multiplier")
    private double priceMultiplier;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
