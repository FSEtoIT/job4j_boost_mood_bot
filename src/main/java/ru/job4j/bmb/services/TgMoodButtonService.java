/*package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TgMoodButtonService {

    private static final Map<String, String> MOOD_RESP = new HashMap<>();

    static {
        MOOD_RESP.put("lost_sock", "Щенок белоснежный, лишь рыжие пятна...\n"
                + "Он очень занятный, доверчивый пёс!");
        MOOD_RESP.put("cucumber", "Спят усталые игрушки, книжки спят...\n"
                + "Ты ей пожелай: баю-бай.");
        MOOD_RESP.put("dance_ready", "На танцующих утят быть похожими хотят...\n"
                + "Неуклюжий бегемот поет «кря-кря-кря».");
        MOOD_RESP.put("need_coffee", "Вместе весело шагать по просторам...\n"
                + "Раз дощечка, два дощечка — будет лесенка!");
        MOOD_RESP.put("sleepy", "Ложкой снег мешая, ночь идет большая...\n"
                + "Спи скорей и ты, малыш...");
    }

    public SendMessage createButtonsMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(createBtn("Пропала собака \uD83D\uDE22", "lost_sock")));
        keyboard.add(List.of(createBtn("Спят усталые игрушки \uD83D\uDE10", "cucumber")));
        keyboard.add(List.of(createBtn("Танец маленьких утят \uD83D\uDE04", "dance_ready")));
        keyboard.add(List.of(createBtn("Вместе весело шагать \uD83D\uDE23", "need_coffee")));
        keyboard.add(List.of(createBtn("Колыбельная медведицы \uD83D\uDE29", "sleepy")));

        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);
        return message;
    }

    private InlineKeyboardButton createBtn(String name, String callbackData) {
        InlineKeyboardButton btn = new InlineKeyboardButton();
        btn.setText(name);
        btn.setCallbackData(callbackData);
        return btn;
    }

    public String getResponse(String callbackData) {
        return MOOD_RESP.getOrDefault(callbackData, "Неизвестная кнопка");
    }
}

 */