package ru.job4j.bmb.model;

import org.springframework.stereotype.Component;

@Component
public class ContentProviderText implements ContentProvider {

    @Override
    public Content byMood(Long chatId, Long moodId) {
        var content = new Content(chatId);
        content.setText("Кто я? \n"
                + "Где я? \n"
                + "И почему я Ёж?!");
        return content;
    }
}