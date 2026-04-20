package ru.job4j.bmb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Конфигурация приложения, содержащая основные настройки,
 * загружаемые из application.properties.
 */
@Component
public class AppConfig {

    /**
     * Название приложения.
     */
    @Value("${app.name}")
    private String appName;

    /**
     * Версия приложения.
     */
    @Value("${app.version}")
    private String appVersion;

    /**
     * URL приложения.
     */
    @Value("${app.url}")
    private String appUrl;

    /**
     * Таймаут запросов.
     */
    @Value("${app.timeout}")
    private int timeout;

    /**
     * Выводит текущую конфигурацию приложения в консоль.
     */
    public void printConfig() {
        System.out.println("App Name: " + appName);
        System.out.println("App Version: " + appVersion);
        System.out.println("App URL: " + appUrl);
        System.out.println("Timeout: " + timeout);
    }
}
