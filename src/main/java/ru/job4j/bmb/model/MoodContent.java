package ru.job4j.bmb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity representing mood content (message for a specific mood).
 */
@Entity
@Table(name = "mb_mood_content")
public class MoodContent {

    /**
     * Unique identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Related mood.
     */
    @ManyToOne
    @JoinColumn(name = "mood_id")
    private Mood mood;

    /**
     * Content text.
     */
    private String text;

    /**
     * Default constructor.
     */
    public MoodContent() {
    }

    /**
     * Full constructor with id.
     *
     * @param id identifier
     * @param mood related mood
     * @param text content text
     */
    public MoodContent(final Long id, final Mood mood, final String text) {
        this.id = id;
        this.mood = mood;
        this.text = text;
    }

    /**
     * Constructor without id.
     *
     * @param mood related mood
     * @param text content text
     */
    public MoodContent(final Mood mood, final String text) {
        this.mood = mood;
        this.text = text;
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
     * @param mood related mood
     */
    public void setMood(final Mood mood) {
        this.mood = mood;
    }

    /**
     * Returns text content.
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets text content.
     *
     * @param text content text
     */
    public void setText(final String text) {
        this.text = text;
    }

    /**
     * Returns content text (alias).
     *
     * @return text content
     */
    public String getContent() {
        return text;
    }

    /**
     * Compares objects by id.
     *
     * @param o other object
     * @return true if same entity
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoodContent moodContent)) {
            return false;
        }
        return id != null && id.equals(moodContent.id);
    }

    /**
     * Hash based on class type.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
