package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.bmb.model.Award;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.SentContent;
import ru.job4j.bmb.model.UserEvent;
import ru.job4j.bmb.model.Content;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.util.List;

/**
 * Service responsible for processing user achievements.
 * Listens to {@link UserEvent} and calculates user progress
 * based on mood logs, then assigns awards if conditions are met.
 */
@Service
public class AchievementService implements ApplicationListener<UserEvent> {

    /**
     * Repository for mood logs.
     */
    private final MoodLogRepository moodLogRepository;

    /**
     * Repository for awards.
     */
    private final AwardRepository awardRepository;

    /**
     * Service for sending content/messages.
     */
    private final SentContent sentContent;

    /**
     * Constructs AchievementService.
     *
     * @param moodLogRepository repository for mood logs
     * @param awardRepository repository for awards
     * @param sentContent service for sending messages
     */
    public AchievementService(final MoodLogRepository moodLogRepository,
                              final AwardRepository awardRepository,
                              final SentContent sentContent) {
        this.moodLogRepository = moodLogRepository;
        this.awardRepository = awardRepository;
        this.sentContent = sentContent;
    }

    /**
     * Initializes service after construction.
     */
    @PostConstruct
    public void init() {
        System.out.println("AchievementService initialized");
    }

    /**
     * Cleanup before bean destruction.
     */
    @PreDestroy
    public void destroy() {
        System.out.println("AchievementService destroyed");
    }

    /**
     * Handles user event, calculates streak of good moods
     * and sends awarded achievements if applicable.
     *
     * @param event user event containing user data
     */
    @Transactional
    @Override
    public void onApplicationEvent(final UserEvent event) {
        var user = event.getUser();

        List<MoodLog> logs =
                moodLogRepository.findByUserOrderByCreatedAtDesc(user);

        int goodDays = 0;
        for (var log : logs) {
            if (log.getMood().isGood()) {
                goodDays++;
            } else {
                break;
            }
        }

        int finalGoodDays = goodDays;

        List<Award> allAwards = awardRepository.findAll();
        List<Award> newAwards = allAwards.stream()
                .filter(a -> a.getDays() <= finalGoodDays)
                .toList();

        if (!newAwards.isEmpty()) {
            var sb = new StringBuilder("Поздравляем! Вы получили награды:\n");
            newAwards.forEach(a -> sb.append("- ")
                    .append(a.getTitle())
                    .append(" (")
                    .append(a.getDays())
                    .append(" дней)\n")
            );

            var content = new Content(user.getChatId());
            content.setText(sb.toString());
            sentContent.sent(content);
        }
    }
}
