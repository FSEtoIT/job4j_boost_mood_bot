package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.bmb.repository.UserRepository;
import ru.job4j.bmb.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TgRemoteService extends TelegramLongPollingBot {

    private final String botName;
    private final String botToken;
    private final UserRepository userRepository;

    public TgRemoteService(@Value("${telegram.bot.name}") String botName,
                           @Value("${telegram.bot.token}") String botToken,
                           UserRepository userRepository) {
        this.botName = botName;
        this.botToken = botToken;
        this.userRepository = userRepository;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
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
                send(sendButtons(chatId));  // Метод для отправки кнопок пользователю
            }
        }

        if (update.hasCallbackQuery()) {
            var callback = update.getCallbackQuery();
            var data = callback.getData();      // lost_sock и т.д.
            var chatId = callback.getMessage().getChatId();

            String response = MOOD_RESP.get(data);

            if (response != null) {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText(response);
                send(message);
            }
        }
    }

    private static final Map<String, String> MOOD_RESP = new HashMap<>();

    static {
        MOOD_RESP.put("lost_sock", "Щенок белоснежный, лишь рыжие пятна.\n " +
                "Лишь рыжие пятна и кисточкой хвост.\n" +
                "Он очень занятный, он очень занятный.\n" +
                "Совсем еще глупый доверчивый пёс.\n" +
                "Он очень занятный, он очень занятный.\n " +
                "Совсем еще глупый, доверчивый пёс!\n");
        MOOD_RESP.put("cucumber", "Спят усталые игрушки,книжки спят.\n " +
                "Одеяла и подушки ждут ребят.\n" +
                "Даже сказка спать ложится.\n " +
                "Чтобы ночью нам присниться. \n" +
                "Ты ей пожелай: баю-бай.\n");
        MOOD_RESP.put("dance_ready", "На танцующих утят быть похожими хотят.\n" +
                "Быть похожими хотят не зря, не зря.\n" +
                "Даже бабушка и дед, сбросив восемьдесят лет.\n" +
                "За утятами вослед кричат «кря-кря».\n" +
                "Вместе солнце, речка, дом кружат в танце озорном.\n" +
                "Кружат в танце озорном не зря, не зря.\n" +
                "Неуклюжий бегемот, ничего не разберет.\n" +
                "Но старательно поет «кря-кря-кря-кря».");
        MOOD_RESP.put("need_coffee", "Вместе весело шагать по просторам \n" +
                "По просторам,по просторам!\n" +
                "И конечно припевать лучше хором\n" +
                "Лучше хором, лучше хором!\n" +
                "Спой-ка с нами, перепелка-перепелочка\n" +
                "Раз иголка, два иголка — будет елочка!\n" +
                "Раз дощечка, два дощечка — будет лесенка!\n" +
                "Раз словечко, два словечко — будет песенка!\n" +
                "В небесах зари полоска заполощется\n" +
                "Раз березка, два березка — будет рощица!\n" +
                "Раз дощечка,два дощечка — будет лесенка!\n" +
                "Раз словечко,два словечко — будет песенка!\n" +
                "Нам счастливую тропинку выбрать надобно.\n" +
                "Раз дождинка, два дождинка — будет радуга!\n" +
                "Раз дощечка, два дощечка — будет лесенка!\n" +
                "Раз словечко, два словечко — будет песенка!");
        MOOD_RESP.put("sleepy", "Ложкой снег мешая, ночь идет большая\n" +
                "Что же ты, глупышка, не спишь?\n" +
                "Спят твои соседи, белые медведи\n" +
                "Спи скорей и ты, малыш...");
    }

    public void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    InlineKeyboardButton createBtn(String name, String data) {
        var inline = new InlineKeyboardButton();
        inline.setText(name);
        inline.setCallbackData(data);
        return inline;
    }

    public SendMessage sendButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Какую песенку спеть ?");

        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(createBtn("Пропала собака \uD83D\uDE22", "lost_sock")));
        keyboard.add(List.of(createBtn("Спят усталые игрушки \uD83D\uDE10", "cucumber")));
        keyboard.add(List.of(createBtn("Танец маленьких утят \uD83D\uDE04", "dance_ready")));
        keyboard.add(List.of(createBtn("Вместе весело шагать \uD83D\uDE23", "need_coffee")));
        keyboard.add(List.of(createBtn("Колыбельная медведицы \uD83D\uDE29", "sleepy")));

        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);

        return message;
    }
}