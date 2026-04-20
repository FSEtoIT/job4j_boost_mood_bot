package ru.job4j.bmb.model;

/**
 * Interface for sending content.
 */
public interface SentContent {

    /**
     * Sends content to destination (e.g. Telegram chat).
     *
     * @param content content to be sent
     */
    void sent(Content content);
}
