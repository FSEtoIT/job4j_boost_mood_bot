package ru.job4j.bmb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity representing mood log record.
 */
@Entity
@Table(name = "mb_mood_log")
public class MoodLog {

    /**
     * Unique identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Related user.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Related mood.
     */
    @ManyToOne
    @JoinColumn(name = "mood_id")
    private Mood mood;

    /**
     * Creation timestamp.
     */
    private long createdAt;

    /**
     * Default constructor.
     */
    public MoodLog() {
    }

    /**
     * Constructor by id.
     *
     * @param id identifier
     */
    public MoodLog(final Long id) {
        this.id = id;
    }

    /**
     * Returns id.
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id identifier
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Returns user.
     *
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user user entity
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * Returns mood.
     *
     * @return mood
     */
    public Mood getMood() {
        return mood;
    }

    /**
     * Sets mood.
     *
     * @param mood mood entity
     */
    public void setMood(final Mood mood) {
        this.mood = mood;
    }

    /**
     * Returns creation timestamp.
     *
     * @return createdAt
     */
    public long getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets creation timestamp.
     *
     * @param createdAt timestamp
     */
    public void setCreatedAt(final long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoodLog moodLog)) {
            return false;
        }
        return id != null && id.equals(moodLog.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
