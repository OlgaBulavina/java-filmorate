package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(Long userId, User user) {
        users.put(userId, user);
        return user;
    }

    @Override
    public User updateUser(Long userId, User updatedUser) {
        User oldUser = users.get(updatedUser.getId());
        oldUser.setName(updatedUser.getName() == null || updatedUser.getName().isEmpty()
                || updatedUser.getName().isBlank() ? updatedUser.getLogin() : updatedUser.getName());
        oldUser.setEmail(updatedUser.getEmail());
        oldUser.setLogin(updatedUser.getLogin());
        oldUser.setBirthday(updatedUser.getBirthday());

        return oldUser;
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }

    @Override
    public Collection<User> returnUsers() {
        return users.values();
    }

    @Override
    public boolean idExists(Long id) {
        return users.keySet().contains(id);
    }

    @Override
    public User returnUserById(Long userId) {
        return users.get(userId);
    }

    @Override
    public Collection<User> returnFriends(Long id) {
        User currentUser = returnUserById(id);
        return currentUser.getFriends().stream()
                .map(friendId -> returnUserById(friendId))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<User> mutualFriends(Long id, Long otherId) {
        User currentUser = returnUserById(id);
        User otherUser = returnUserById(otherId);

        Set<Long> userFriendsIds = new HashSet<>(currentUser.getFriends());
        Set<Long> otherFriendsIds = otherUser.getFriends();

        userFriendsIds.retainAll(otherFriendsIds);

        return userFriendsIds.stream()
                .map(friendId -> returnUserById(friendId))
                .collect(Collectors.toSet());
    }
}
