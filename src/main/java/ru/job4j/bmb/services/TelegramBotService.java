package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.job4j.bmb.condition.RealBotCondition;
import ru.job4j.bmb.model.Content;
import ru.job4j.bmb.model.SentContent;
import ru.job4j.bmb.model.SentContentException;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

@Service
@Conditional(RealBotCondition.class)
public class TelegramBotService extends TelegramLongPollingBot implements SentContent {
    private final BotCommandHandler handler;
    private final UserRepository userRepository;
    private final TgMoodButtonService moodButtonService;
    private final TgMessageService messageService;

    @Value("${telegram.bot.name}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    public TelegramBotService(BotCommandHandler handler, UserRepository userRepository,
                              TgMoodButtonService moodButtonService,
                              TgMessageService messageService) {
        this.handler = handler;
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

    public void receive(Content content) {
        handler.receive(content);
    }

    @Override
    public void sent(Content content) {
        try {
            if (content.getAudio() != null) {
                var sendAudio = new SendAudio();
                sendAudio.setChatId(String.valueOf(content.getChatId()));
                sendAudio.setAudio(content.getAudio());

                if (content.getText() != null) {
                    sendAudio.setCaption(content.getText());
                }

                execute(sendAudio);

            } else if (content.getText() != null && content.getMarkup() != null) {
                var sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(content.getChatId()));
                sendMessage.setText(content.getText());
                sendMessage.setReplyMarkup(content.getMarkup());

                execute(sendMessage);

            } else if (content.getText() != null) {
                var sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(content.getChatId()));
                sendMessage.setText(content.getText());

                execute(sendMessage);

            } else if (content.getPhoto() != null) {
                var sendPhoto = new SendPhoto();
                sendPhoto.setChatId(String.valueOf(content.getChatId()));
                sendPhoto.setPhoto(content.getPhoto());

                if (content.getText() != null) {
                    sendPhoto.setCaption(content.getText());
                }

                execute(sendPhoto);
            }

        } catch (Exception e) {
            throw new SentContentException("Error while sending content", e);
        }
    }
}