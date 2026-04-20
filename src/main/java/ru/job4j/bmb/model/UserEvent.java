package ru.job4j.bmb.model;

import org.springframework.context.ApplicationEvent;

/**
 * Event fired when user-related action occurs.
 */
public class UserEvent extends ApplicationEvent {

    /**
     * User associated with the event.
     */
    private final User user;

    /**
     * Constructs a new UserEvent.
     *
     * @param source event source
     * @param user user related to event
     */
    public UserEvent(final Object source,
                     final User user) {
        super(source);
        this.user = user;
    }

    /**
     * Returns user associated with this event.
     *
     * @return user
     */
    public User getUser() {
        return user;
    }
}
