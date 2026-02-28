package ru.job4j.bmb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mb_achievement")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private long createAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "award_id")
    private Award award;

    public Achievement() {
    }

    public Achievement(long id, long createAt) {
        this.id = id;
        this.createAt = createAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Achievement achievement)) return false;
        return id != null && id.equals(achievement.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

