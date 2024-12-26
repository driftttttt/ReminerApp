package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Table(name = "reminders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reminder {
    /**
     * Уникальный идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Краткое описание
     */
    @Column(nullable = false)
    private String title;

    /**
     * Полное описание
     */
    @Column(nullable = false, length = 4096)
    private String description;

    /**
     * Дата и время
     */
    @Column(nullable = false)
    private LocalDateTime remind;

    /**
     * Идентификатор
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private boolean sent = false;

}
