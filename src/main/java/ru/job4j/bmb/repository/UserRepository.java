package ru.job4j.bmb.repository;

import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    User findByClientId(Long clientId);
}