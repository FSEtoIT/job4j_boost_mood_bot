package ru.job4j.bmb.services;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.repository.MoodRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class TgUI {
    private final MoodRepository moodRepository;

    public TgUI(MoodRepository moodRepository) {
        this.moodRepository = moodRepository;
    }

    /**
     * Строит Inline-клавиатуру со всеми настроениями.
     */
    public InlineKeyboardMarkup buildButtons() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        // Кнопки настроений (каждая в отдельной строке)
        List<Mood> moods = moodRepository.findAll();
        for (Mood mood : moods) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(mood.getText());
            button.setCallbackData(String.valueOf(mood.getId())); // id настроения
            keyboard.add(List.of(button));
        }

        // Кнопки команд бота — каждая в отдельной строке
        keyboard.add(List.of(createCommandButton("Совет дня", "/daily_advice")));
        keyboard.add(List.of(createCommandButton("Мои награды", "/award")));
        keyboard.add(List.of(createCommandButton("Отчет за неделю", "/week_mood_log")));
        keyboard.add(List.of(createCommandButton("Отчет за месяц", "/month_mood_log")));

        // Кнопки включения/выключения напоминаний — в одной строке
        InlineKeyboardButton reminderOnBtn = createCommandButton("Вкл.напом.", "/reminder_on");
        InlineKeyboardButton reminderOffBtn = createCommandButton("Выкл.напом.", "/reminder_off");
        keyboard.add(List.of(reminderOnBtn, reminderOffBtn));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }

    /**
     * Вспомогательный метод для создания кнопок команд
     */
    private InlineKeyboardButton createCommandButton(String text, String command) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(command);
        return button;
    }
}