package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class RecommendationEngine {

    @PostConstruct
    public void init() {
        System.out.println("RecommendationEngine инициализирован");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("RecommendationEngine уничтожается");
    }
}
