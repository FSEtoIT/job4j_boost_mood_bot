/*package ru.job4j.bmb.model;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

@Component
public class ContentProviderImage implements ContentProvider {

    @Override
    public Content byMood(Long chatId, Long moodId) {
        var content = new Content(chatId);
        var stream = getClass().getClassLoader().getResourceAsStream("images/logo.png");
        if (stream != null) {
            content.setPhoto(new InputFile(stream, "logo.png"));
        } else {
            content.setText("Изображение logo.png не найдено.");
        }
        return content;
    }
}


 */