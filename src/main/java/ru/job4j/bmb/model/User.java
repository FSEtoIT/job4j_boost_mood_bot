package ru.job4j.bmb.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "mb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", unique = true)
    private long clientId;

    @Column(name = "chat_id")
    private long chatId;

    @Column(name = "daily_reminder_enabled")
    private boolean dailyReminderEnabled = false; // по умолчанию выключено

    @Column(name = "daily_reminder_time")
    private String dailyReminderTime; // хранится время в формате "HH:mm"

    public User() {
    }

    public User(Long id, long clientId, long chatId) {
        this.id = id;
        this.clientId = clientId;
        this.chatId = chatId;
    }

    public Long getId() {
        return id;
    }

    public long getClientId() {
        return clientId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public boolean isDailyReminderEnabled() {
        return dailyReminderEnabled;
    }

    public void setDailyReminderEnabled(boolean dailyReminderEnabled) {
        this.dailyReminderEnabled = dailyReminderEnabled;
    }

    public String getDailyReminderTime() {
        return dailyReminderTime;
    }

    public void setDailyReminderTime(String dailyReminderTime) {
        this.dailyReminderTime = dailyReminderTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return clientId == user.clientId
                && chatId == user.chatId
                && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, chatId);
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", clientId=" + clientId
                + ", chatId=" + chatId
                + '}';
    }
}
