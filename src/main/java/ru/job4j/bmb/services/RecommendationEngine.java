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

@Service
public class RecommendationEngine implements BeanNameAware {

    private final MoodContentRepository moodContentRepository;
    private final Random rnd = new Random();
    private String beanName;

    public RecommendationEngine(MoodContentRepository moodContentRepository) {
        this.moodContentRepository = moodContentRepository;
    }

    public Content recommendFor(Long chatId, Long moodId) {
        Content content = new Content(chatId);

        List<MoodContent> moodContents = moodContentRepository.findAllByMoodId(moodId);

        if (moodContents != null && !moodContents.isEmpty()) {
            MoodContent chosen = moodContents.get(rnd.nextInt(moodContents.size()));
            content.setText(chosen.getText()); // возвращаем текст совета
        } else {
            content.setText("Рекомендация для выбранного настроения не найдена.");
        }

        return content;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
        System.out.println("Имя бина в Spring-контексте: " + name);
    }

    @PostConstruct
    public void init() {
        System.out.println("RecommendationEngine инициализирован");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("RecommendationEngine уничтожается");
    }
}
