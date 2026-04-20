package ru.job4j.bmb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity representing award.
 */
@Entity
@Table(name = "mb_award")
public class Award {

    /**
     * Unique identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Award title.
     */
    private String title;

    /**
     * Award description.
     */
    private String description;

    /**
     * Required days for achievement.
     */
    private int days;

    /**
     * Default constructor.
     */
    public Award() {
    }

    /**
     * Constructor for award.
     *
     * @param title award title
     * @param description award description
     * @param days required days
     */
    public Award(final String title,
                 final String description,
                 final int days) {
        this.title = title;
        this.description = description;
        this.days = days;
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
     * Returns title.
     *
     * @return getTitle
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets creation title.
     *
     * @param title title
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Returns description.
     *
     * @return getDescription
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets creation description.
     *
     * @param description description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Returns days in the raw.
     *
     * @return getDays
     */
    public int getDays() {
        return days;
    }

    /**
     * Sets creation zero day.
     *
     * @param days days
     */
    public void setDays(final int days) {
        this.days = days;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Award award)) {
            return false;
        }
        return id != null && id.equals(award.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
