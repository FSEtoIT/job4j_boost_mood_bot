/*
package ru.job4j.bmb.services;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class DailyAdviceScheduler {

    private final UserRepository userRepository;
    private final MoodService moodService;
    private final TelegramBotService telegramBotService; // Сервис отправки сообщений

    public DailyAdviceScheduler(UserRepository userRepository,
                                MoodService moodService,
                                TelegramBotService telegramBotService) {
        this.userRepository = userRepository;
        this.moodService = moodService;
        this.telegramBotService = telegramBotService;
    }

    /**
     * Планировщик проверяет каждую минуту,
     * если текущее время совпадает с time пользователя — отправляет совет.
     */
/*
    @Scheduled(cron = "0 * * * * *")
    public void sendDailyAdviceToUsers() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);

        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.isDailyReminderEnabled() && user.getDailyReminderTime() != null) {
                try {
                    LocalTime reminderTime = LocalTime.parse(user.getDailyReminderTime());
                    if (now.equals(reminderTime)) {

                        var adviceContent = moodService.dailyAdvice(user);
                        telegramBotService.sent(adviceContent);
                    }
                } catch (DateTimeParseException e) {
                    // Неправильный формат времени в базе — игнорируем
                }
            }
        }
    }
}
*/