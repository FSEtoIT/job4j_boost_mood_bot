package ru.job4j.bmb.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {
    List<MoodLog> findAll();

    List<MoodLog> findByUserAndCreatedAtAfter(User user, long createdAt);

    List<MoodLog> findByUserOrderByCreatedAtDesc(Optional<User> user);

    List<MoodLog> findByUserId(Long userId);

    Stream<MoodLog> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("""
           select u from User u
           where u not in (
               select ml.user from MoodLog ml
               where ml.createdAt between :startOfDay and :endOfDay
           )
           """)
    List<User> findUsersWhoDidNotVoteToday(long startOfDay, long endOfDay);

    List<MoodLog> findMoodLogsForWeek(Long userId, long weekStart);

    List<MoodLog> findMoodLogsForMonth(Long userId, long monthStart);
}