package ru.job4j.bmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.util.List;

/**
 * Repository for working with {@link MoodLog} entities.
 * Provides methods for retrieving mood logs and analytical queries
 * related to user activity.
 */
@Repository
public interface MoodLogRepository extends JpaRepository<MoodLog, Long> {

    /**
     * Finds all mood logs for a given user created after a specific timestamp.
     *
     * @param user user whose logs are being searched
     * @param timestamp lower bound of creation time
     * @return list of mood logs matching criteria
     */
    List<MoodLog> findByUserAndCreatedAtAfter(User user, long timestamp);

    /**
     * Finds all mood logs for a user ordered by creation time descending.
     *
     * @param user user whose logs are being retrieved
     * @return list of mood logs sorted from newest to oldest
     */
    List<MoodLog> findByUserOrderByCreatedAtDesc(User user);

    /**
     * Finds users who did not submit a mood log within a given time range.
     *
     * @param start start timestamp of period
     * @param end end timestamp of period
     * @return list of users who did not vote in the specified period
     */
    @Query("""
                SELECT u
                FROM User u
                WHERE u.id NOT IN (
                    SELECT m.user.id
                    FROM MoodLog m
                    WHERE m.createdAt BETWEEN :start AND :end
                )
            """)
    List<User> findUsersWhoDidNotVoteToday(
            @Param("start") long start,
            @Param("end") long end
    );
}
