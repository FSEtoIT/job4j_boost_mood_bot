package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.Achievement;

import java.util.List;

/**
 * Repository for {@link Achievement} entities.
 */
@Repository
public interface AchievementRepository extends
        CrudRepository<Achievement, Long> {

    /**
     * Returns all achievements.
     *
     * @return list of achievements
     */
    @Override
    List<Achievement> findAll();
}
