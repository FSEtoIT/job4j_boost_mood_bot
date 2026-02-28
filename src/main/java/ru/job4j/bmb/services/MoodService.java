package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Service;

@Service
public class MoodService implements BeanNameAware {

    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
        System.out.println("Имя бина в Spring-контексте: " + name);
    }

    @PostConstruct
    public void init() {
        System.out.println("MoodService инициализирован");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("MoodService уничтожается");
    }
}
