package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.model.Content;
import ru.job4j.bmb.model.MoodContent;
import ru.job4j.bmb.repository.MoodContentRepository;

import java.util.List;
import java.util.Random;

/**
 * Сервис рекомендаций контента в зависимости от настроения пользователя.
 *
 * <p>Отвечает за выбор случайного контента {@link MoodContent}
 * по идентификатору настроения и формирование ответа пользователю.</p>
 */
@Service
public class RecommendationEngine implements BeanNameAware {

    /**
     * Репозиторий контента настроений.
     */
    private final MoodContentRepository moodContentRepository;

    /**
     * Генератор случайных чисел для выбора рекомендаций.
     */
    private final Random rnd = new Random();

    /**
     * Имя Spring-бина.
     */
    private String beanName;

    /**
     * Конструктор сервиса рекомендаций.
     *
     * @param moodContentRepository репозиторий контента настроений
     */
    public RecommendationEngine(
            final MoodContentRepository moodContentRepository) {
        this.moodContentRepository = moodContentRepository;
    }

    /**
     * Формирует рекомендацию для пользователя по выбранному настроению.
     *
     * @param chatId идентификатор чата Telegram
     * @param moodId идентификатор настроения
     * @return контент с текстовой рекомендацией
     */
    public Content recommendFor(final Long chatId,
                                final Long moodId) {
        Content content = new Content(chatId);

        List<MoodContent> moodContents = moodContentRepository
                .findAllByMoodId(moodId);

        if (moodContents != null && !moodContents.isEmpty()) {
            MoodContent chosen =
                    moodContents.get(rnd.nextInt(moodContents.size()));
            content.setText(chosen.getText());
        } else {
            content.setText("Рекомендация для выбранного настроения "
                    + "не найдена.");
        }

        return content;
    }

    /**
     * Устанавливает имя Spring-бина.
     *
     * @param name имя бина
     */
    @Override
    public void setBeanName(final String name) {
        this.beanName = name;
        System.out.println("Имя бина в Spring-контексте: " + name);
    }

    /**
     * Инициализация бина после создания.
     */
    @PostConstruct
    public void init() {
        System.out.println("RecommendationEngine инициализирован");
    }

    /**
     * Очистка перед уничтожением бина.
     */
    @PreDestroy
    public void destroy() {
        System.out.println("RecommendationEngine уничтожается");
    }
}
