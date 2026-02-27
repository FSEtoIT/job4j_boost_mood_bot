package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.model.Content;

@Service
public class BotCommandHandler {
    void receive(Content content) {
        System.out.println(content);
    }

    @PostConstruct
    public void init() {
        System.out.println("BotCommandHandler инициализирован");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("BotCommandHandler уничтожается");
    }
}