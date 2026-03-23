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
import java.util.Optional;

@Service
public class AchievementService implements ApplicationListener<UserEvent> {

    private final MoodLogRepository moodLogRepository;
    private final AwardRepository awardRepository;
    private final SentContent sentContent;

    public AchievementService(MoodLogRepository moodLogRepository,
                              AwardRepository awardRepository,
                              SentContent sentContent) {
        this.moodLogRepository = moodLogRepository;
        this.awardRepository = awardRepository;
        this.sentContent = sentContent;
    }

    @PostConstruct
    public void init() {
        System.out.println("AchievementService инициализирован");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("AchievementService уничтожается");
    }

    @Transactional
    @Override
    public void onApplicationEvent(UserEvent event) {
        var user = event.getUser(); // User, не Optional<User>

        // Получаем логи пользователя
        List<MoodLog> logs = moodLogRepository.findByUserOrderByCreatedAtDesc(user);

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
