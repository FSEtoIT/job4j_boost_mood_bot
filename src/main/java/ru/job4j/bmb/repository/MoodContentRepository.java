package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.MoodContent;

import java.util.List;

/**
 * Repository for {@link MoodContent} entities.
 */
@Repository
public interface MoodContentRepository extends
        CrudRepository<MoodContent, Long> {

    /**
     * Finds all mood contents by mood id.
     *
     * @param moodId mood identifier
     * @return list of mood contents
     */
    List<MoodContent> findAllByMoodId(Long moodId);
}
