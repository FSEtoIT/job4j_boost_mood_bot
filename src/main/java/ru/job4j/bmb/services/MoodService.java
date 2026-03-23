package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.model.*;
import ru.job4j.bmb.repository.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class MoodService implements BeanNameAware {

    private String beanName;

    private final MoodLogRepository moodLogRepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final AwardRepository awardRepository;
    private final ApplicationEventPublisher publisher;
    private final MoodRepository moodRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public MoodService(MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository,
                       AwardRepository awardRepository,
                       ApplicationEventPublisher publisher,
                       MoodRepository moodRepository) {
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
        this.awardRepository = awardRepository;
        this.publisher = publisher;
        this.moodRepository = moodRepository;
    }

    public Content chooseMood(User user, Long moodId) {
        var moodLog = new MoodLog();
        moodLog.setUser(user);
        moodLog.setMood(new Mood(moodId));
        moodLog.setCreatedAt(Instant.now().getEpochSecond());

        moodLogRepository.save(moodLog);
        publisher.publishEvent(new UserEvent(this, user)); // Публикация события
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
        content.setText(formatMoodLogs(logs, "Ваше настроение за неделю:"));
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
        content.setText(formatMoodLogs(logs, "Ваше настроение за месяц:"));
        return Optional.of(content);
    }

    private String formatMoodLogs(List<MoodLog> logs, String title) {
        if (logs.isEmpty()) {
            return title + "\nЗаписей о настроении не найдено.";
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
        var userOpt = userRepository.findByClientId(clientId);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        var user = userOpt.get(); // получаем объект User

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
            content.setText("У вас еще нет наград. Отметьте свое настроение!");
        } else {
            var sb = new StringBuilder("Ваши награды:\n");
            achievedAwards.forEach(a ->
                    sb.append("- ")
                            .append(a.getTitle())
                            .append(" (")
                            .append(a.getDays())
                            .append(" дней)\n")
            );
            content.setText(sb.toString());
        }
        return Optional.of(content);
    }

    public Content dailyAdvice(User user) {
        // Находим последний MoodLog пользователя
        var lastMoodLogOpt = moodLogRepository.findTopByUserOrderByCreatedAtDesc(user);

        Long moodId;
        if (lastMoodLogOpt.isPresent()) {
            // Берем moodId из последнего лога
            moodId = lastMoodLogOpt.get().getMood().getId();
        } else {
            // Если логов нет, берем случайное настроение из MoodRepository
            List<Mood> allMoods = moodRepository.findAll();
            if (allMoods.isEmpty()) {
                Content content = new Content(user.getChatId());
                content.setText("Нет доступных настроений для совета сегодня.");
                return content;
            }
            moodId = allMoods.get(new Random().nextInt(allMoods.size())).getId();
        }

        // Получаем текст рекомендации через RecommendationEngine
        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

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
