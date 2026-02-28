package ru.job4j.bmb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mb_mood_content")
public class MoodContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mood_id")
    private Mood mood;
    private String text;

    public MoodContent(){
    }

    public MoodContent(Long id){
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
        if (!(o instanceof MoodContent moodContent)) return false;
        return id != null && id.equals(moodContent.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}