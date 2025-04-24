package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(long userId, User user) {
        users.put(userId, user);
        return user;
    }

    @Override
    public User updateUser(long userId, User updatedUser) {
        User oldUser = users.get(updatedUser.getId());
        oldUser.setName(updatedUser.getName() == null || updatedUser.getName().isEmpty()
                || updatedUser.getName().isBlank() ? updatedUser.getLogin() : updatedUser.getName());
        oldUser.setEmail(updatedUser.getEmail());
        oldUser.setLogin(updatedUser.getLogin());
        oldUser.setBirthday(updatedUser.getBirthday());

        return oldUser;
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
    }

    @Override
    public Collection<User> returnUsers() {
        return users.values();
    }

    @Override
    public Collection<Long> returnIDs() {
        return users.keySet();
    }

    @Override
    public User returnUserById(long userId) {
        return users.get(userId);
    }
}
