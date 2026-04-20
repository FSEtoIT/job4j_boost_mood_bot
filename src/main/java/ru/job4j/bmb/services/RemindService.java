package ru.job4j.bmb.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.model.Content;
import ru.job4j.bmb.model.SentContent;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

/**
 * Сервис напоминаний пользователям о необходимости
 * отметить своё настроение.
 *
 * <p>Периодически (раз в минуту) проверяет пользователей,
 * которые не оставили запись о настроении за текущий день,
 * и отправляет им уведомление в Telegram в заданное время.</p>
 */
@Service
public class RemindService {

    /**
     * Сервис отправки сообщений в Telegram.
     */
    private final SentContent sentContent;

    /**
     * Репозиторий журнала настроений пользователей.
     */
    private final MoodLogRepository moodLogRepository;

    /**
     * UI-компонент Telegram-бота для формирования интерфейса.
     */
    private final TgUI tgUI;

    /**
     * Конструктор сервиса напоминаний.
     *
     * @param sentContent сервис отправки сообщений
     * @param moodLogRepository репозиторий журналов настроений
     * @param tgUI Telegram UI компонент
     */
    public RemindService(final SentContent sentContent,
                         final MoodLogRepository moodLogRepository,
                         final TgUI tgUI) {
        this.sentContent = sentContent;
        this.moodLogRepository = moodLogRepository;
        this.tgUI = tgUI;
    }

    /**
     * Периодическая проверка пользователей и отправка напоминаний.
     *
     * <p>Запускается каждую минуту. Проверяет пользователей,
     * которые не отметили настроение за текущий день,
     * и отправляет им сообщение в заданное время напоминания.</p>
     */
    @Scheduled(fixedRateString = "60000")
    public void remindUsers() {
        LocalDate today = LocalDate.now();

        long startOfDay = today
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        long endOfDay = today
                .plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        List<User> users = moodLogRepository
                .findUsersWhoDidNotVoteToday(startOfDay, endOfDay);

        LocalDateTime now = LocalDateTime.now();

        for (User user : users) {
            if (user.isDailyReminderEnabled()
                    && user.getDailyReminderTime() != null) {

                LocalTime reminderTime =
                        LocalTime.parse(user.getDailyReminderTime());

                LocalDateTime reminderDateTime =
                        now.toLocalDate().atTime(reminderTime);

                // отправка только в пределах 1 минуты от времени напоминания
                if (!now.isBefore(reminderDateTime)
                        && now.isBefore(reminderDateTime.plusMinutes(1))) {

                    Content content = new Content(user.getChatId());
                    content.setText("А мы напоминаем Вам, "
                            + "пора отметить настроение!");
                    sentContent.sent(content);
                }
            }
        }
    }
}
