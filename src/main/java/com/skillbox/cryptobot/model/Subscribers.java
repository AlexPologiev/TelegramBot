package com.skillbox.cryptobot.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscribers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "userId")
    Long userId;

    BigDecimal price;
}
