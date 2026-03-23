package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.job4j.bmb.model.Content;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

import java.util.Optional;

@Service
public class BotCommandHandler implements BeanNameAware {

    private final UserRepository userRepository;
    private final MoodService moodService;
    private final TgUI tgUI;
    private String beanName;
    private final String defaultReminderTime = "09:00"; // дефолтное время для напоминаний

    public BotCommandHandler(UserRepository userRepository,
                             MoodService moodService,
                             TgUI tgUI) {
        this.userRepository = userRepository;
        this.moodService = moodService;
        this.tgUI = tgUI;
    }

    /**
     * Обработка текстовых команд бота.
     */
    public Optional<Content> commands(Message message) {
        if (message == null || message.getText() == null) {
            return Optional.empty();
        }
        return handleCommand(message.getText(), message.getChatId(), message.getFrom().getId());
    }

    /**
     * Обработка callback-кнопок
     */
    public Optional<Content> handleCallback(CallbackQuery callback) {
        String data = callback.getData();
        long chatId = callback.getMessage().getChatId();
        Long clientId = callback.getFrom().getId();

        try {
            // если это число — выбор настроения
            Long moodId = Long.valueOf(data);
            return userRepository.findByClientId(clientId)
                    .map(user -> moodService.chooseMood(user, moodId));
        } catch (NumberFormatException e) {
            // иначе это команда
            return handleCommand(data, chatId, clientId);
        }
    }

    /**
     * Универсальная обработка команд (текст или callback)
     */
    private Optional<Content> handleCommand(String text, long chatId, Long clientId) {
        if ("/start".equals(text)) {
            return handleStartCommand(chatId, clientId);
        } else if ("/week_mood_log".equals(text)) {
            return moodService.weekMoodLogCommand(chatId, clientId);
        } else if ("/month_mood_log".equals(text)) {
            return moodService.monthMoodLogCommand(chatId, clientId);
        } else if ("/award".equals(text)) {
            return moodService.awards(chatId, clientId);
        } else if ("/daily_advice".equals(text)) {
            return userRepository.findByClientId(clientId)
                    .map(moodService::dailyAdvice);
        } else if (text.startsWith("/reminder_on")) {
            Content content = new Content(chatId);
            userRepository.findByClientId(clientId).ifPresent(user -> {
                user.setDailyReminderEnabled(true);
                // если время передано через текст команды, используем его
                String[] parts = text.split(" ");
                if (parts.length == 2) {
                    user.setDailyReminderTime(parts[1]);
                    content.setText("Напоминания включены на " + parts[1]);
                } else {
                    user.setDailyReminderTime(defaultReminderTime);
                    content.setText("Напоминания включены на " + defaultReminderTime);
                }
                userRepository.save(user);
            });
            return Optional.of(content);
        } else if ("/reminder_off".equals(text)) {
            Content content = new Content(chatId);
            userRepository.findByClientId(clientId).ifPresent(user -> {
                user.setDailyReminderEnabled(false);
                userRepository.save(user);
            });
            content.setText("Напоминания отключены.");
            return Optional.of(content);
        }
        return Optional.empty();
    }

    /**
     * /start команда — создает пользователя и показывает клавиатуру
     */
    private Optional<Content> handleStartCommand(long chatId, Long clientId) {
        User user = userRepository.findByClientId(clientId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setClientId(clientId);
                    newUser.setChatId(chatId);
                    return userRepository.save(newUser);
                });

        Content content = new Content(user.getChatId());
        content.setText("Как настроение? Используй кнопки ниже или команды:\n"
                + "/award - награды\n"
                + "/week_mood_log - отчет за неделю\n"
                + "/month_mood_log - отчет за месяц\n"
                + "/daily_advice - совет дня\n"
                + "/reminder_on HH:mm - включить напоминания\n"
                + "/reminder_off - выключить напоминания\n");
        content.setMarkup(tgUI.buildButtons());
        return Optional.of(content);
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
        System.out.println("Имя бина в Spring-контексте: " + name);
    }

    @PostConstruct
    public void init() {
        System.out.println("BotCommandHandler инициализирован");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("BotCommandHandler уничтожается");
    }
}