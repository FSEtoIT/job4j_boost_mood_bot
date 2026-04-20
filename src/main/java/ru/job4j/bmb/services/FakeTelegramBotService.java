package ru.job4j.bmb.services;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.job4j.bmb.condition.FakeBotCondition;
import ru.job4j.bmb.model.Content;
import ru.job4j.bmb.model.SentContent;

/**
 * Fake implementation of Telegram bot service.
 * Used for development and testing purposes instead of real Telegram API.
 * Does not send real messages, only prints them to console.
 */
@Service
@Conditional(FakeBotCondition.class)
public class FakeTelegramBotService extends
        TelegramLongPollingBot implements SentContent {

    /**
     * Handles incoming Telegram updates.
     * In fake implementation only logs received updates.
     *
     * @param update incoming update from Telegram
     */
    @Override
    public void onUpdateReceived(final Update update) {
        System.out.println("Fake bot received message");
    }

    /**
     * Returns bot username.
     *
     * @return fake bot username
     */
    @Override
    public String getBotUsername() {
        return "fake-bot";
    }

    /**
     * Returns bot token.
     *
     * @return fake bot token
     */
    @Override
    public String getBotToken() {
        return "fake-token";
    }

    /**
     * Sends content to user (fake implementation).
     * Instead of sending to Telegram, prints content to console.
     *
     * @param content message content to "send"
     */
    @Override
    public void sent(final Content content) {
        System.out.println("FAKE BOT SEND:");
        System.out.println("chatId = " + content.getChatId());
        System.out.println("text = " + content.getText());
    }
}
