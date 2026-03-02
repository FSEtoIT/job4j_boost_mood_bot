package ru.job4j.bmb.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.job4j.bmb.repository.UserRepository;

@Service
public class RemindService {

    private final TgRemoteService bot;
    private final TgMessageService messageService;
    private final UserRepository userRepository;

    public RemindService(TgRemoteService bot, TgMessageService messageService, UserRepository userRepository) {
        this.bot = bot;
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRateString = "${remind.period}")
    public void ping() {
        for (var user : userRepository.findAll()) {

            var msg = new SendMessage();
            msg.setChatId(user.getChatId());
            msg.setText("Не забывайте отмечать настроение ;)");

            messageService.send(bot, msg);
        }
    }
}