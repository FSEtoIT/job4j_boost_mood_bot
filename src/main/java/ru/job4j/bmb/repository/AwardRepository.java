package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.Award;

import java.util.List;

/**
 * Repository for {@link Award} entities.
 */
@Repository
public interface AwardRepository extends CrudRepository<Award, Long> {

    /**
     * Returns all awards.
     *
     * @return list of awards
     */
    @Override
    List<Award> findAll();
}
