package ru.job4j.bmb.model;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * DTO for sending content via Telegram bot.
 */
public class Content {

    /**
     * Chat identifier.
     */
    private final Long chatId;

    /**
     * Text message content.
     */
    private String text;

    /**
     * Photo content.
     */
    private InputFile photo;

    /**
     * Inline keyboard markup.
     */
    private InlineKeyboardMarkup markup;

    /**
     * Audio content.
     */
    private InputFile audio;

    /**
     * Creates content instance with chat id.
     *
     * @param chatId chat identifier
     */
    public Content(final Long chatId) {
        this.chatId = chatId;
    }

    /**
     * Returns chat id.
     *
     * @return chat id
     */
    public Long getChatId() {
        return chatId;
    }

    /**
     * Returns text content.
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Returns photo content.
     *
     * @return photo file
     */
    public InputFile getPhoto() {
        return photo;
    }

    /**
     * Returns inline keyboard markup.
     *
     * @return keyboard markup
     */
    public InlineKeyboardMarkup getMarkup() {
        return markup;
    }

    /**
     * Returns audio content.
     *
     * @return audio file
     */
    public InputFile getAudio() {
        return audio;
    }

    /**
     * Sets text content.
     *
     * @param text message text
     */
    public void setText(final String text) {
        this.text = text;
    }

    /**
     * Sets photo content.
     *
     * @param photo photo file
     */
    public void setPhoto(final InputFile photo) {
        this.photo = photo;
    }

    /**
     * Sets inline keyboard markup.
     *
     * @param markup keyboard markup
     */
    public void setMarkup(final InlineKeyboardMarkup markup) {
        this.markup = markup;
    }

    /**
     * Sets audio content.
     *
     * @param audio audio file
     */
    public void setAudio(final InputFile audio) {
        this.audio = audio;
    }
}
