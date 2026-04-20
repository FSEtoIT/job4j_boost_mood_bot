package ru.job4j.bmb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.job4j.bmb.model.Award;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodContent;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.repository.MoodContentRepository;
import ru.job4j.bmb.repository.MoodRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point of application.
 */
@EnableAspectJAutoProxy
@EnableScheduling
@SpringBootApplication
public class Main {

    /**
     * Number of days in a week.
     */
    private static final int WEEK_DAYS = 7;

    /**
     * Number of days in a month.
     */
    private static final int MONTH_DAYS = 30;

    /**
     * Application start point.
     * @param args input arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(Main.class, args);
    }

    /**
     * Registers Telegram bot.
     * @param bot telegram bot.
     * @return runner.
     */
    @Bean
    public CommandLineRunner commandLineRunner(
            final TelegramLongPollingBot bot) {
        return args -> {
            var botsApi = new TelegramBotsApi(DefaultBotSession.class);
            try {
                botsApi.registerBot(bot);
                System.out.println("Бот успешно зарегистрирован");
            } catch (TelegramApiException e) {
                System.out.println("Ошибка регистрации бота: "
                        + e.getMessage());
            }
        };
    }

    /**
     * Prints environment variable.
     * @param ctx application context.
     * @return runner.
     */
    @Bean
    public CommandLineRunner checkEnv(final ApplicationContext ctx) {
        return args -> System.out.println(
                ctx.getEnvironment().getProperty("telegram.bot.name")
        );
    }

    /**
     * Loads initial database data.
     * @param moodRepository mood repo.
     * @param moodContentRepository mood content repo.
     * @param awardRepository award repo.
     * @return runner.
     */
    @Bean
    public CommandLineRunner loadDatabase(
            final MoodRepository moodRepository,
            final MoodContentRepository moodContentRepository,
            final AwardRepository awardRepository
    ) {
        return args -> {
            loadMoods(moodRepository, moodContentRepository);
            loadAwards(awardRepository);
        };
    }

    /**
     * Loads moods.
     * @param moodRepository repo.
     * @param moodContentRepository repo.
     */
    private void loadMoods(
            final MoodRepository moodRepository,
            final MoodContentRepository moodContentRepository
    ) {
        if (!moodRepository.findAll().isEmpty()) {
            return;
        }

        List<MoodContent> data = new ArrayList<>();

        data.add(new MoodContent(new Mood(
                "Счастливейший на свете 😎", true),
                "Невероятно! Вы сияете от счастья."));
        data.add(new MoodContent(new Mood(
                "Воодушевленное настроение 🌟", true),
                "Вы на высоте!"));
        data.add(new MoodContent(new Mood(
                "Тревожное настроение 😬", false),
                "Попробуйте расслабиться."));
        data.add(new MoodContent(new Mood(
                "Усталое настроение 😴", false),
                "Время отдохнуть."));

        moodRepository.saveAll(
                data.stream().map(MoodContent::getMood).toList());
        moodContentRepository.saveAll(data);
    }

    /**
     * Loads awards.
     * @param awardRepository repo.
     */
    private void loadAwards(final AwardRepository awardRepository) {
        if (!awardRepository.findAll().isEmpty()) {
            return;
        }

        List<Award> awards = new ArrayList<>();

        awards.add(new Award(
                "Смайлик дня", "За 1 день хорошего настроения.", 1));
        awards.add(new Award(
                "Неделя", "За 7 дней настроения.", WEEK_DAYS));
        awards.add(new Award(
                "Месяц", "За 30 дней.", MONTH_DAYS));

        awardRepository.saveAll(awards);
    }
}
