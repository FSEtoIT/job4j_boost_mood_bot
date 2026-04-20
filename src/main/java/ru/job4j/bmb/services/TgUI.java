package ru.job4j.bmb.services;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.repository.MoodRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * UI-компонент Telegram-бота.
 *
 * <p>Отвечает за построение Inline-клавиатуры,
 * которая используется для взаимодействия пользователя
 * с ботом (выбор настроения, команды, напоминания).</p>
 */
@Component
public class TgUI {

    /**
     * Репозиторий настроений пользователей.
     */
    private final MoodRepository moodRepository;

    /**
     * Конструктор TgUI.
     *
     * @param moodRepository репозиторий настроений
     */
    public TgUI(final MoodRepository moodRepository) {
        this.moodRepository = moodRepository;
    }

    /**
     * Формирует Inline-клавиатуру для Telegram-бота.
     *
     * <p>Содержит:
     * <ul>
     *     <li>кнопки настроений пользователя</li>
     *     <li>кнопки команд (отчёты, награды, советы)</li>
     *     <li>кнопки управления напоминаниями</li>
     * </ul>
     * </p>
     *
     * @return InlineKeyboardMarkup с кнопками управления ботом
     */
    public InlineKeyboardMarkup buildButtons() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<Mood> moods = moodRepository.findAll();
        for (Mood mood : moods) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(mood.getText());
            button.setCallbackData(String.valueOf(mood.getId()));
            keyboard.add(List.of(button));
        }

        keyboard.add(List.of(createCommandButton(
                "Совет дня", "/daily_advice")));
        keyboard.add(List.of(createCommandButton(
                "Мои награды", "/award")));
        keyboard.add(List.of(createCommandButton(
                "Отчет за неделю", "/week_mood_log")));
        keyboard.add(List.of(createCommandButton(
                "Отчет за месяц", "/month_mood_log")));

        InlineKeyboardButton reminderOnBtn =
                createCommandButton("Вкл.напом.", "/reminder_on");

        InlineKeyboardButton reminderOffBtn =
                createCommandButton("Выкл.напом.", "/reminder_off");

        keyboard.add(List.of(reminderOnBtn, reminderOffBtn));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }

    /**
     * Создаёт кнопку Telegram-команды.
     *
     * @param text отображаемый текст кнопки
     * @param command callback-значение команды
     * @return кнопка InlineKeyboardButton
     */
    private InlineKeyboardButton createCommandButton(final String text,
                                                     final String command) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(command);
        return button;
    }
}
