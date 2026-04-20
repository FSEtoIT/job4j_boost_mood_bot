package ru.job4j.bmb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity representing user achievement.
 */
@Entity
@Table(name = "mb_achievement")
public final class Achievement {

    /**
     * Unique identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Creation timestamp.
     */
    private long createAt;

    /**
     * Owner of achievement.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Award linked to achievement.
     */
    @ManyToOne
    @JoinColumn(name = "award_id")
    private Award award;

    /**
     * Default constructor.
     */
    public Achievement() {
    }

    /**
     * Constructor for achievement.
     *
     * @param createAt creation time
     * @param user owner of achievement
     * @param award award entity
     */
    public Achievement(final long createAt,
                       final User user,
                       final Award award) {
        this.createAt = createAt;
        this.user = user;
        this.award = award;
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
     * Returns creation timestamp.
     *
     * @return createAt
     */
    public long getCreateAt() {
        return createAt;
    }

    /**
     * Sets creation timestamp.
     *
     * @param createAt timestamp
     */
    public void setCreateAt(final long createAt) {
        this.createAt = createAt;
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
     * @param user owner
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * Returns award.
     *
     * @return award
     */
    public Award getAward() {
        return award;
    }

    /**
     * Sets award.
     *
     * @param award award entity
     */
    public void setAward(final Award award) {
        this.award = award;
    }

    /**
     * Compares achievements by id.
     *
     * @param o other object
     * @return true if same
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Achievement achievement)) {
            return false;
        }
        return id != null && id.equals(achievement.id);
    }

    /**
     * Hash based on class.
     *
     * @return hash
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
