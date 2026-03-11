package ru.job4j.bmb.services;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.job4j.bmb.condition.FakeBotCondition;
import ru.job4j.bmb.model.Content;
import ru.job4j.bmb.model.SentContent;

@Service
@Conditional(FakeBotCondition.class)
public class FakeTelegramBotService extends TelegramLongPollingBot implements SentContent {

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Fake bot received message");
    }

    @Override
    public String getBotUsername() {
        return "fake-bot";
    }

    @Override
    public String getBotToken() {
        return "fake-token";
    }

    @Override
    public void sent(Content content) {
        System.out.println("FAKE BOT SEND:");
        System.out.println("chatId = " + content.getChatId());
        System.out.println("text = " + content.getText());
    }
}