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

        List<Mood> moods = moodRepository.findAll();
        for (Mood mood : moods) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(mood.getText()); // имя + смайл
            button.setCallbackData(String.valueOf(mood.getId()));

            keyboard.add(List.of(button)); // каждая кнопка в отдельной строке
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }
}