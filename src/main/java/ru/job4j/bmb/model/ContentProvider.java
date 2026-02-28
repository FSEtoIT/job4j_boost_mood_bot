package ru.job4j.bmb.model;

public interface ContentProvider {
    Content byMood(Long chatId, Long moodId);
}
