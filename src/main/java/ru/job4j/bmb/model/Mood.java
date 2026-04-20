package ru.job4j.bmb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Entity representing user mood.
 */
@Entity
@Table(name = "mb_mood")
public class Mood {

    /**
     * Unique identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Mood text description.
     */
    private String text;

    /**
     * Mood positivity flag.
     */
    private boolean good;

    /**
     * Related user.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    /**
     * Creation timestamp.
     */
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Default constructor.
     */
    public Mood() {
    }

    /**
     * Constructor by id.
     *
     * @param id mood identifier
     */
    public Mood(final Long id) {
        this.id = id;
    }

    /**
     * Constructor without user.
     *
     * @param text mood text
     * @param good mood positivity flag
     */
    public Mood(final String text, final boolean good) {
        this.text = text;
        this.good = good;
    }

    /**
     * Full constructor.
     *
     * @param text mood text
     * @param good mood positivity flag
     * @param user related user
     */
    public Mood(final String text, final boolean good, final User user) {
        this.text = text;
        this.good = good;
        this.user = user;
    }

    /**
     * Returns id.
     *
     * @return mood id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id mood id
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Returns mood text.
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets mood text.
     *
     * @param text mood text
     */
    public void setText(final String text) {
        this.text = text;
    }

    /**
     * Returns mood positivity flag.
     *
     * @return true if good mood
     */
    public boolean isGood() {
        return good;
    }

    /**
     * Sets mood positivity flag.
     *
     * @param good mood flag
     */
    public void setGood(final boolean good) {
        this.good = good;
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
     * @param user related user
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * Returns creation time.
     *
     * @return timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets creation time.
     *
     * @param createdAt timestamp
     */
    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Compares moods by id.
     *
     * @param o other object
     * @return true if same mood
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mood mood)) {
            return false;
        }
        return id != null && id.equals(mood.id);
    }

    /**
     * Hash based on class.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
