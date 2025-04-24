package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {


    User addUser(long userId, User user);

    User updateUser(long userId, User updatedUser);

    void deleteUser(long userId);

    Collection<User> returnUsers();

    Collection<Long> returnIDs();

    User returnUserById(long userId);
}
