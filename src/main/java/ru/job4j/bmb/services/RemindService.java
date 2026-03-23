package ru.job4j.bmb.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.model.Content;
import ru.job4j.bmb.model.SentContent;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.time.LocalDate;
import java.time.ZoneId;

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

    @Scheduled(fixedRateString = "${recommendation.alert.period}")
    public void remindUsers() {
        // Границы текущего дня
        var startOfDay = LocalDate.now().atStartOfDay(); // LocalDateTime
        var endOfDay = LocalDate.now().plusDays(1).atStartOfDay().minusNanos(1); // LocalDateTime до конца дня

        for (var user : moodLogRepository.findUsersWhoDidNotVoteToday(startOfDay, endOfDay)) {
            var content = new Content(user.getChatId());
            content.setText("Как настроение?");
            content.setMarkup(tgUI.buildButtons());
            sentContent.sent(content);
        }
    }
}
