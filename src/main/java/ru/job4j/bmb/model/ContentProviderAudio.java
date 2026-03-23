/*package ru.job4j.bmb.model;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

@Component
public class ContentProviderAudio implements ContentProvider {

    @Override
    public Content byMood(Long chatId, Long moodId) {
        var content = new Content(chatId);
        var stream = getClass().getClassLoader().getResourceAsStream("audio/music.mp3");
        if (stream != null) {
            content.setAudio(new InputFile(stream, "music.mp3"));
        } else {
            content.setText("Аудиофайл music.mp3 не найден.");
        }
        return content;
    }
}

 */