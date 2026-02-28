package ru.job4j.bmb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mb_award")
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String title;
    private String description;
    private int days;

    public Award() {
    }

    public Award(String title, String description, int days) {
        this.title = title;
        this.description = description;
        this.days = days;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Award award)) return false;
        return id != null && id.equals(award.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}