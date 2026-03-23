package ru.job4j.bmb.services;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodContent;
import ru.job4j.bmb.repository.MoodRepository;
import ru.job4j.bmb.repository.MoodContentRepository;

import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final MoodRepository moodRepository;
    private final MoodContentRepository moodContentRepository;

    public DatabaseInitializer(MoodRepository moodRepository,
                               MoodContentRepository moodContentRepository) {
        this.moodRepository = moodRepository;
        this.moodContentRepository = moodContentRepository;
    }

    @Override
    public void run(String... args) {
        loadMoods();
    }

    private void loadMoods() {
        if (!moodRepository.findAll().isEmpty()) {
            return; // база уже заполнена
        }

        // 1️⃣ Создаем настроения
        var happy = new Mood("Счастливейший на свете 😎", true);
        var inspired = new Mood("Воодушевленное настроение 🌟", true);
        var calm = new Mood("Успокоение и гармония 🧘‍♂️", true);

        moodRepository.saveAll(List.of(happy, inspired, calm));

        // 2️⃣ Создаем рекомендации для каждого настроения
        moodContentRepository.saveAll(List.of(
                new MoodContent(happy, "Ты сияешь, твоя энергия впечатляет!"),
                new MoodContent(happy, "Пусть этот счастливый момент будет с тобой весь день."),
                new MoodContent(inspired, "Воодушевление — твой двигатель успеха."),
                new MoodContent(inspired, "Идеи бьют ключом — реализуй их!"),
                new MoodContent(calm, "Спокойствие — твоя сила. Оставайся гармоничным."),
                new MoodContent(calm, "Дыхание, тишина, твой внутренний мир — всё в порядке.")
        ));

        System.out.println("База настроений и рекомендаций успешно инициализирована!");
    }
}