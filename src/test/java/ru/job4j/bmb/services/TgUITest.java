package ru.job4j.bmb.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.repository.MoodFakeRepository;
import ru.job4j.bmb.repository.MoodRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {TgUI.class, MoodFakeRepository.class})
class TgUITest {
    @Autowired
    @Qualifier("moodFakeRepository")
    private MoodRepository moodRepository;

    private Mood mood1;
    private Mood mood2;

    @BeforeEach
    void setup() {
        moodRepository.deleteAll();

        mood1 = new Mood();
        mood1.setId(1L);
        mood1.setText("Good");

        mood2 = new Mood();
        mood2.setId(2L);
        mood2.setText("Bad");

        moodRepository.save(mood1);
        moodRepository.save(mood2);
    }

    @Test
    void whenBtnGood_thenRepositoryIsInjected() {
        assertThat(moodRepository).isNotNull();
    }

    @Test
    void whenFindAll_thenReturnAllMoods() {
        List<Mood> moods = moodRepository.findAll();
        assertThat(moods).hasSize(2)
                .containsExactlyInAnyOrder(mood1, mood2);
    }

    @Test
    void whenSaveMood_thenItIsPersisted() {
        Mood mood3 = new Mood();
        mood3.setId(3L);
        mood3.setText("Neutral");

        moodRepository.save(mood3);

        List<Mood> moods = moodRepository.findAll();
        assertThat(moods).hasSize(3)
                .contains(mood3);
    }
}