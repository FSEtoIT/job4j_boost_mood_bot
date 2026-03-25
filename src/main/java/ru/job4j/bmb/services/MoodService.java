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
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Сервис для работы с настроениями пользователей, их журналами и рекомендациями.
 * Реализует BeanNameAware для получения имени бина в Spring-контексте.
 */
@Service
public class MoodService implements BeanNameAware {

    /** Имя бина в Spring-контексте */
    private String beanName;

    /** Репозиторий для работы с журналом настроений */
    private final MoodLogRepository moodLogRepository;

    /** Движок рекомендаций контента по настроению */
    private final RecommendationEngine recommendationEngine;

    /** Репозиторий пользователей */
    private final UserRepository userRepository;

    /** Репозиторий наград */
    private final AwardRepository awardRepository;

    /** Публикатор событий Spring */
    private final ApplicationEventPublisher publisher;

    /** Репозиторий для контента настроений */
    private final MoodContentRepository moodContentRepository;

    /** Форматтер для отображения даты и времени */
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    /** Генератор случайных чисел для выбора советов */
    private final Random rnd = new Random();

    /**
     * Конструктор MoodService.
     *
     * @param moodLogRepository репозиторий журнала настроений
     * @param recommendationEngine движок рекомендаций
     * @param userRepository репозиторий пользователей
     * @param awardRepository репозиторий наград
     * @param publisher публикатор событий
     * @param moodContentRepository репозиторий контента настроений
     */
    public MoodService(MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AwardRepository awardRepository,
                       ApplicationEventPublisher publisher,
                       MoodContentRepository moodContentRepository) {
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.awardRepository = awardRepository;
        this.publisher = publisher;
        this.moodContentRepository = moodContentRepository;
    }

    /**
     * Записывает выбранное пользователем настроение и возвращает рекомендацию контента.
     *
     * @param user пользователь
     * @param moodId идентификатор настроения
     * @return контент с рекомендацией
     */
    public Content chooseMood(User user, Long moodId) {
        var moodLog = new MoodLog();
        moodLog.setUser(user);
        moodLog.setMood(new Mood(moodId));
        moodLog.setCreatedAt(Instant.now().getEpochSecond());

        moodLogRepository.save(moodLog);
        publisher.publishEvent(new UserEvent(this, user));
        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

    /**
     * Формирует отчет о настроении пользователя за последнюю неделю.
     *
     * @param chatId идентификатор чата
     * @param clientId идентификатор клиента
     * @return контент с отчетом или Optional.empty(), если пользователь не найден
     */
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

    /**
     * Формирует отчет о настроении пользователя за последний месяц.
     *
     * @param chatId идентификатор чата
     * @param clientId идентификатор клиента
     * @return контент с отчетом или Optional.empty(), если пользователь не найден
     */
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

    /**
     * Форматирует список MoodLog в текст для отправки пользователю.
     *
     * @param logs список записей настроений
     * @param title заголовок отчета
     * @return форматированный текст
     */
    private String formatMoodLogs(List<MoodLog> logs, String title) {
        if (logs.isEmpty()) {
            return title + "\nЗаписей о настроении не найдено.";
        }

        var sb = new StringBuilder(title + ":\n");
        logs.forEach(log -> {
            var formattedDate = formatter.format(Instant.ofEpochSecond(log.getCreatedAt()));
            sb.append(formattedDate)
                    .append(": ")
                    .append(log.getMood().getText())
                    .append("\n");
        });
        return sb.toString();
    }

    /**
     * Возвращает список достижений пользователя в виде текста.
     *
     * @param chatId идентификатор чата
     * @param clientId идентификатор клиента
     * @return контент с достижениями или Optional.empty(), если пользователь не найден
     */
    public Optional<Content> awards(long chatId, Long clientId) {
        var userOpt = userRepository.findByClientId(clientId);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        var user = userOpt.get();

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
                    sb.append("- ").append(a.getTitle()).append(" (").append(a.getDays()).append(" дней)\n")
            );
            content.setText(sb.toString());
        }
        return Optional.of(content);
    }

    /**
     * Возвращает случайный совет пользователю на день.
     *
     * @param user пользователь
     * @return контент с советом
     */
    public Content dailyAdvice(User user) {
        Content content = new Content(user.getChatId());

        List<MoodContent> allContents = StreamSupport.stream(
                moodContentRepository.findAll().spliterator(), false
        ).collect(Collectors.toList());

        if (!allContents.isEmpty()) {
            MoodContent randomContent = allContents.get(rnd.nextInt(allContents.size()));
            content.setText(randomContent.getText());
        } else {
            content.setText("Нет доступных настроений для совета сегодня.");
        }

        return content;
    }

    /**
     * Устанавливает имя бина Spring.
     *
     * @param name имя бина
     */
    @Override
    public void setBeanName(String name) {
        this.beanName = name;
        System.out.println("Имя бина в Spring-контексте: " + name);
    }

    /** Метод вызывается после инициализации бина */
    @PostConstruct
    public void init() {
        System.out.println("MoodService инициализирован");
    }

    /** Метод вызывается перед уничтожением бина */
    @PreDestroy
    public void destroy() {
        System.out.println("MoodService уничтожается");
    }
}