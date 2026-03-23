package ru.job4j.bmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.util.List;

@Repository
public interface MoodLogRepository extends JpaRepository<MoodLog, Long> {

    List<MoodLog> findByUserAndCreatedAtAfter(User user, long timestamp);

    List<MoodLog> findByUserOrderByCreatedAtDesc(User user);

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