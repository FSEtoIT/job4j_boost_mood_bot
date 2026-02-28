package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.model.Content;

@Service
public class BotCommandHandler implements BeanNameAware {

    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
        System.out.println("Имя бина в Spring-контексте: " + name);
    }

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