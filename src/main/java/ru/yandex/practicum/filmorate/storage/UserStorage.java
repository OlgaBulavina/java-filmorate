package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {


    User addUser(Long userId, User user);

    User updateUser(Long userId, User updatedUser);

    void deleteUser(Long userId);

    Collection<User> returnUsers();

    boolean idExists(Long id);

    User returnUserById(Long userId);

    Collection<User> returnFriends(Long id);

    Collection<User> mutualFriends(Long id, Long otherId);
}
