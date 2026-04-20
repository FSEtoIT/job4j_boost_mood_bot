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

/**
 * Реальная реализация Telegram-бота.
 *
 * <p>Обрабатывает входящие обновления от Telegram API,
 * передаёт команды в {@link BotCommandHandler}
 * и отправляет пользователю сформированный {@link Content}.</p>
 *
 * <p>Поднимается только при выполнении условия {@link RealBotCondition}.</p>
 */
@Service
@Conditional(RealBotCondition.class)
public class TelegramBotService extends
        TelegramLongPollingBot implements SentContent {

    /**
     * Обработчик команд пользователя.
     */
    private final BotCommandHandler handler;

    /**
     * Имя Telegram-бота.
     */
    @Value("${telegram.bot.name}")
    private String botUsername;

    /**
     * Токен Telegram-бота.
     */
    @Value("${telegram.bot.token}")
    private String botToken;

    /**
     * Конструктор TelegramBotService.
     *
     * @param handler обработчик команд пользователя
     */
    public TelegramBotService(final BotCommandHandler handler) {
        this.handler = handler;
    }

    /**
     * Обрабатывает входящие обновления от Telegram.
     *
     * <p>Поддерживает обработку текстовых сообщений и callback-запросов.</p>
     *
     * @param update обновление Telegram API
     */
    @Override
    public void onUpdateReceived(final Update update) {
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
            Optional<Content> callbackContent = handler
                    .handleCallback(callback);
            callbackContent.ifPresent(this::sent);
        }
    }

    /**
     * Возвращает имя Telegram-бота.
     *
     * @return имя бота
     */
    @Override
    public String getBotUsername() {
        return botUsername;
    }

    /**
     * Возвращает токен Telegram-бота.
     *
     * @return токен бота
     */
    @Override
    public String getBotToken() {
        return botToken;
    }

    /**
     * Отправляет контент пользователю в Telegram.
     *
     * <p>Поддерживает отправку текста, фото и аудио.</p>
     *
     * @param content контент для отправки
     * @throws SentContentException если произошла ошибка отправки
     */
    @Override
    public void sent(final Content content) {
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

            } else if (content.getText() != null
                    && content.getMarkup() != null) {
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
