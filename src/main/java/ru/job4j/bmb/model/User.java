package ru.job4j.bmb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * Entity representing bot user.
 */
@Entity
@Table(name = "mb_user")
public class User {

    /**
     * Unique identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * External client id (Telegram user id).
     */
    @Column(name = "client_id", unique = true)
    private long clientId;

    /**
     * Chat id for messaging.
     */
    @Column(name = "chat_id")
    private long chatId;

    /**
     * Flag for daily reminder.
     */
    @Column(name = "daily_reminder_enabled")
    private boolean dailyReminderEnabled = false;

    /**
     * Time of daily reminder in format HH:mm.
     */
    @Column(name = "daily_reminder_time")
    private String dailyReminderTime;

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Constructor with basic fields.
     *
     * @param id user id
     * @param clientId telegram client id
     * @param chatId chat id
     */
    public User(final Long id,
                final long clientId,
                final long chatId) {
        this.id = id;
        this.clientId = clientId;
        this.chatId = chatId;
    }

    /**
     * Returns id.
     *
     * @return user id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id user id
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Returns client id.
     *
     * @return client id
     */
    public long getClientId() {
        return clientId;
    }

    /**
     * Sets client id.
     *
     * @param clientId telegram client id
     */
    public void setClientId(final long clientId) {
        this.clientId = clientId;
    }

    /**
     * Returns chat id.
     *
     * @return chat id
     */
    public long getChatId() {
        return chatId;
    }

    /**
     * Sets chat id.
     *
     * @param chatId chat id
     */
    public void setChatId(final long chatId) {
        this.chatId = chatId;
    }

    /**
     * Returns whether daily reminder is enabled.
     *
     * @return true if enabled
     */
    public boolean isDailyReminderEnabled() {
        return dailyReminderEnabled;
    }

    /**
     * Sets daily reminder flag.
     *
     * @param dailyReminderEnabled flag
     */
    public void setDailyReminderEnabled(final boolean dailyReminderEnabled) {
        this.dailyReminderEnabled = dailyReminderEnabled;
    }

    /**
     * Returns reminder time.
     *
     * @return time in HH:mm
     */
    public String getDailyReminderTime() {
        return dailyReminderTime;
    }

    /**
     * Sets reminder time.
     *
     * @param dailyReminderTime time in HH:mm
     */
    public void setDailyReminderTime(final String dailyReminderTime) {
        this.dailyReminderTime = dailyReminderTime;
    }

    @Override
    public boolean equals(final Object o) {
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
