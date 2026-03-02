package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

@Service
public class TgRemoteService extends TelegramLongPollingBot {

    private final UserRepository userRepository;
    private final TgMoodButtonService moodButtonService;
    private final TgMessageService messageService;

    @Value("${telegram.bot.name}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    public TgRemoteService(UserRepository userRepository,
                           TgMoodButtonService moodButtonService,
                           TgMessageService messageService) {
        this.userRepository = userRepository;
        this.moodButtonService = moodButtonService;
        this.messageService = messageService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();

            if ("/start".equals(message.getText())) {
                long chatId = message.getChatId();
                var user = new User();
                user.setClientId(message.getFrom().getId());
                user.setChatId(chatId);
                userRepository.save(user);

                SendMessage msg = moodButtonService.createButtonsMessage(chatId, "Привет!\n"
                        + "Я бот для улучшения твоего настроения.");
                messageService.send(this, msg);
            }
        }

        if (update.hasCallbackQuery()) {
            var callback = update.getCallbackQuery();
            long chatId = callback.getMessage().getChatId();
            String response = moodButtonService.getResponse(callback.getData());

            SendMessage msg = new SendMessage();
            msg.setChatId(String.valueOf(chatId));
            msg.setText(response);
            messageService.send(this, msg);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}