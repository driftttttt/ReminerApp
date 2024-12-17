package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reminders")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Уникальный идентификатор

    @Column(nullable = false)
    private String title; //Краткое описание

    @Column(nullable = false, length = 4096)
    private String description; //Полное описание

    @Column(nullable = false)
    private LocalDateTime remind; //Дата и время напоминания в формате ISO

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Идентификатор пользователя

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    private boolean sent =  false; //Было ли уведомление по напоминанию

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public LocalDateTime getRemind() {
        return remind;
    }

    public void setRemind(LocalDateTime remind) {
        this.remind = remind;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
