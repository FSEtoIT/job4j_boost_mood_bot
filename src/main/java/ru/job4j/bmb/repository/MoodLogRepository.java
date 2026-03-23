package ru.job4j.bmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MoodLogRepository extends JpaRepository<MoodLog, Long> {

    List<MoodLog> findByUserAndCreatedAtAfter(User user, long timestamp);

    List<MoodLog> findByUserOrderByCreatedAtDesc(User user);

    Optional<MoodLog> findTopByUserOrderByCreatedAtDesc(User user); // новый метод для последнего настроения

    @Query("""
        SELECT u
        FROM User u
        WHERE u.id NOT IN (
            SELECT m.user.id
            FROM Mood m
            WHERE m.createdAt BETWEEN :startOfDay AND :endOfDay
        )
    """)
    List<User> findUsersWhoDidNotVoteToday(@Param("startOfDay") LocalDateTime startOfDay,
                                           @Param("endOfDay") LocalDateTime endOfDay);
}