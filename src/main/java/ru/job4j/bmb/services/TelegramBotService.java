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

import java.util.Optional;

@Service
@Conditional(RealBotCondition.class)
public class TelegramBotService extends TelegramLongPollingBot implements SentContent {

    private final BotCommandHandler handler;

    @Value("${telegram.bot.name}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    public TelegramBotService(BotCommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            Optional<Content> commandContent = handler.commands(message);
            commandContent.ifPresent(this::sent);
            if (commandContent.isPresent()) {
                return;
            }
        }

        if (update.hasCallbackQuery()) {
            var callback = update.getCallbackQuery();
            Optional<Content> callbackContent = handler.handleCallback(callback);
            callbackContent.ifPresent(this::sent);
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

            } else if (content.getPhoto() != null) {
                var sendPhoto = new SendPhoto();
                sendPhoto.setChatId(String.valueOf(content.getChatId()));
                sendPhoto.setPhoto(content.getPhoto());

                if (content.getText() != null) {
                    sendPhoto.setCaption(content.getText());
                }

                execute(sendPhoto);

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
            }

        } catch (Exception e) {
            throw new SentContentException("Error while sending content", e);
        }
    }
}