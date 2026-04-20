package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository for working with {@link User} entities.
 * Provides CRUD operations and custom queries for users.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Returns all users from the database.
     *
     * @return list of all users
     */
    List<User> findAll();

    /**
     * Finds user by Telegram client id.
     *
     * @param clientId telegram client identifier
     * @return optional user if found
     */
    Optional<User> findByClientId(Long clientId);
}
