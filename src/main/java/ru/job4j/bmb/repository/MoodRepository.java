package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.Mood;

import java.util.List;

/**
 * Repository for working with {@link Mood} entities.
 * Provides CRUD operations and custom queries for moods.
 */
@Repository
public interface MoodRepository extends CrudRepository<Mood, Long> {

    /**
     * Returns all moods from the database.
     *
     * @return list of all moods
     */
    List<Mood> findAll();
}
