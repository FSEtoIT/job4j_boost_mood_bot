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

/**
 * Handler responsible for processing Telegram bot commands and callbacks.
 * Supports commands such as start, mood logs, awards and reminder settings.
 */
@Service
public class BotCommandHandler implements BeanNameAware {

    /**
     * Repository for user persistence operations.
     */
    private final UserRepository userRepository;

    /**
     * Service responsible for mood-related operations.
     */
    private final MoodService moodService;

    /**
     * Service responsible for building Telegram UI elements.
     */
    private final TgUI tgUI;

    /**
     * Spring bean name.
     */
    private String beanName;

    /**
     * Default reminder time used when user does not provide one.
     */
    private final String defaultReminderTime = "09:00";

    /**
     * Constructs handler with required dependencies.
     *
     * @param userRepository repository for users
     * @param moodService service for mood logic
     * @param tgUI service for Telegram UI
     */
    public BotCommandHandler(final UserRepository userRepository,
                             final MoodService moodService,
                             final TgUI tgUI) {
        this.userRepository = userRepository;
        this.moodService = moodService;
        this.tgUI = tgUI;
    }

    /**
     * Processes incoming Telegram message commands.
     *
     * @param message incoming message
     * @return optional response content
     */
    public Optional<Content> commands(final Message message) {
        if (message == null || message.getText() == null) {
            return Optional.empty();
        }
        return handleCommand(
                message.getText(),
                message.getChatId(),
                message.getFrom().getId()
        );
    }

    /**
     * Processes callback queries from inline buttons.
     *
     * @param callback callback query
     * @return optional response content
     */
    public Optional<Content> handleCallback(final CallbackQuery callback) {
        String data = callback.getData();
        long chatId = callback.getMessage().getChatId();
        Long clientId = callback.getFrom().getId();

        try {
            Long moodId = Long.valueOf(data);
            return userRepository.findByClientId(clientId)
                    .map(user -> moodService.chooseMood(user, moodId));
        } catch (NumberFormatException e) {
            return handleCommand(data, chatId, clientId);
        }
    }

    /**
     * Handles textual bot commands.
     *
     * @param text command text
     * @param chatId telegram chat id
     * @param clientId telegram user id
     * @return optional response content
     */
    private Optional<Content> handleCommand(final String text,
                                            final long chatId,
                                            final Long clientId) {
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
                String[] parts = text.split(" ");
                if (parts.length == 2) {
                    user.setDailyReminderTime(parts[1]);
                    content.setText("Напоминания включены на " + parts[1]);
                } else {
                    user.setDailyReminderTime(defaultReminderTime);
                    content.setText("Напоминания включены на "
                            + defaultReminderTime);
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
     * Handles /start command and registers user if needed.
     *
     * @param chatId telegram chat id
     * @param clientId telegram user id
     * @return welcome message content
     */
    private Optional<Content> handleStartCommand(final long chatId,
                                                 final Long clientId) {
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

    /**
     * Sets Spring bean name.
     *
     * @param name bean name
     */
    @Override
    public void setBeanName(final String name) {
        this.beanName = name;
        System.out.println("Имя бина в Spring-контексте: " + name);
    }

    /**
     * Initializes bean after construction.
     */
    @PostConstruct
    public void init() {
        System.out.println("BotCommandHandler инициализирован");
    }

    /**
     * Cleanup before bean destruction.
     */
    @PreDestroy
    public void destroy() {
        System.out.println("BotCommandHandler уничтожается");
    }
}
