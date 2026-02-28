package ru.job4j.bmb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mb_mood_log")
public class MoodLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "mood_id")
    private Mood mood;
    private long createdAt;

    public MoodLog(){
    }

    public MoodLog(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoodLog moodLog)) return false;
        return id != null && id.equals(moodLog.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}