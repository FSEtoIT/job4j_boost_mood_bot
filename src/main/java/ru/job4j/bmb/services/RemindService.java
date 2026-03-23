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

@Service
public class RemindService {
    private final SentContent sentContent;
    private final MoodLogRepository moodLogRepository;
    private final TgUI tgUI;

    public RemindService(SentContent sentContent,
                           MoodLogRepository moodLogRepository, TgUI tgUI) {
        this.sentContent = sentContent;
        this.moodLogRepository = moodLogRepository;
        this.tgUI = tgUI;
    }

    @Scheduled(fixedRateString = "60000") // проверяем каждую минуту
    public void remindUsers() {
        LocalDate today = LocalDate.now();
        long startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endOfDay = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        List<User> users = moodLogRepository.findUsersWhoDidNotVoteToday(startOfDay, endOfDay);

        LocalDateTime now = LocalDateTime.now();

        for (User user : users) {
            if (user.isDailyReminderEnabled() && user.getDailyReminderTime() != null) {
                LocalTime reminderTime = LocalTime.parse(user.getDailyReminderTime());
                LocalDateTime reminderDateTime = now.toLocalDate().atTime(reminderTime);

                // если сейчас в пределах одной минуты после времени напоминания
                if (!now.isBefore(reminderDateTime) && now.isBefore(reminderDateTime.plusMinutes(1))) {
                    Content content = new Content(user.getChatId());
                    content.setText("А мы напоминаем Вам, пора отметить настроение!");
                    sentContent.sent(content);
                }
            }
        }
    }
}
