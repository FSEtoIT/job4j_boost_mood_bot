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

        String text = message.getText();
        long chatId = message.getChatId();
        Long clientId = message.getFrom().getId();

        return switch (text) {
            case "/start" -> handleStartCommand(chatId, clientId);
            case "/week_mood_log" -> moodService.weekMoodLogCommand(chatId, clientId);
            case "/month_mood_log" -> moodService.monthMoodLogCommand(chatId, clientId);
            case "/award" -> moodService.awards(chatId, clientId);
            case "/daily_advice" -> userRepository.findByClientId(clientId)
                    .map(moodService::dailyAdvice);
            default -> Optional.empty();
        };
    }

    /**
     * Обработка выбора настроения через callback.
     */
    public Optional<Content> handleCallback(CallbackQuery callback) {
        Long moodId;
        try {
            moodId = Long.valueOf(callback.getData());
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        return userRepository.findByClientId(callback.getFrom().getId())
                .map(user -> moodService.chooseMood(user, moodId));
    }

    /**
     * /start команда — создает пользователя и показывает клавиатуру с настроениями.
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
        content.setText("""
                Как настроение? 
                Используй команды:
                /award - для вывода наград
                /week_mood_log - список настроения за неделю
                /month_mood_log - список настроения за месяц
                /daily_advice - случайный совет дня
                """);
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