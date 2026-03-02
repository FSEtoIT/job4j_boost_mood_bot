package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.model.Content;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class MoodService implements BeanNameAware {

    private final MoodLogRepository moodLogRepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final AwardRepository awardRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public MoodService(MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository,
                       AwardRepository awardRepository) {
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
        this.awardRepository = awardRepository;
    }

    public Content chooseMood(User user, Long moodId) {
        var moodLog = new MoodLog();
        moodLog.setUser(user);
        moodLog.setMood(new Mood(moodId));
        moodLog.setCreatedAt(Instant.now().getEpochSecond());

        moodLogRepository.save(moodLog);

        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        var userOpt = userRepository.findByClientId(clientId);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        var user = userOpt.get();

        var weekAgo = Instant.now().minus(7, ChronoUnit.DAYS).getEpochSecond();
        var logs = moodLogRepository.findByUserAndCreatedAtAfter(user, weekAgo);

        var content = new Content(chatId);
        content.setText(formatMoodLogs(logs, "Mood log for last week"));
        return Optional.of(content);
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        var userOpt = userRepository.findByClientId(clientId);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        var user = userOpt.get();

        var monthAgo = Instant.now().minus(30, ChronoUnit.DAYS).getEpochSecond();
        var logs = moodLogRepository.findByUserAndCreatedAtAfter(user, monthAgo);

        var content = new Content(chatId);
        content.setText(formatMoodLogs(logs, "Mood log for last month"));
        return Optional.of(content);
    }

    private String formatMoodLogs(List<MoodLog> logs, String title) {
        if (logs.isEmpty()) {
            return title + ":\nNo mood logs found.";
        }

        var sb = new StringBuilder(title + ":\n");

        logs.forEach(log -> {
            var formattedDate = formatter.format(
                    Instant.ofEpochSecond(log.getCreatedAt())
            );
            sb.append(formattedDate)
                    .append(": ")
                    .append(log.getMood().getText())
                    .append("\n");
        });
        return sb.toString();
    }

    public Optional<Content> awards(long chatId, Long clientId) {
        var user = userRepository.findByClientId(clientId);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        var logs = moodLogRepository.findByUserOrderByCreatedAtDesc(user);
        int goodDays = 0;
        for (var log : logs) {
            if (log.getMood().isGood()) {
                goodDays++;
            } else {
                break;
            }
        }
        var allAwards = awardRepository.findAll();
        int finalGoodDays = goodDays;
        var achievedAwards = allAwards.stream()
                .filter(a -> a.getDays() <= finalGoodDays)
                .toList();
        var content = new Content(chatId);
        if (achievedAwards.isEmpty()) {
            content.setText("You have no awards yet. Keep going!");
        } else {
            var sb = new StringBuilder("Your awards:\n");
            achievedAwards.forEach(a ->
                    sb.append("- ")
                            .append(a.getTitle())
                            .append(" (")
                            .append(a.getDays())
                            .append(" days)\n")
            );
            content.setText(sb.toString());
        }
        return Optional.of(content);
    }

    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
        System.out.println("Имя бина в Spring-контексте: " + name);
    }

    @PostConstruct
    public void init() {
        System.out.println("MoodService инициализирован");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("MoodService уничтожается");
    }

    public Content weekMoodLog(User user) {
        var content = new Content(user.getChatId());
        content.setText("Лог настроений за неделю (заглушка)");
        return content;
    }

    public Content monthMoodLog(User user) {
        var content = new Content(user.getChatId());
        content.setText("Лог настроений за месяц (заглушка)");
        return content;
    }

    public Content getAwards(User user) {
        var content = new Content(user.getChatId());
        content.setText("Список наград пользователя (заглушка)");
        return content;
    }
}
