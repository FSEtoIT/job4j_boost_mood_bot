package ru.job4j.bmb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mb_mood")
public class Mood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String text;
    private boolean good;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true) // nullable = true
    private User user;

    // 🔹 добавляем дату создания для фильтрации по дню
    private LocalDateTime createdAt = LocalDateTime.now();

    public Mood() {
    }

    public Mood(Long id) {
        this.id = id;
    }

    public Mood(String text, boolean good) {
        this.text = text;
        this.good = good;
    }

    public Mood(String text, boolean good, User user) {
        this.text = text;
        this.good = good;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mood mood)) return false;
        return id != null && id.equals(mood.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
