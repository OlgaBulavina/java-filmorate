package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserStorage userStorage;

    public Collection<User> getAllUsers() {
        log.info("provided list of users: " + userStorage.returnUsers());
        return userStorage.returnUsers();
    }

    public User addUser(User user) {
        validateAllParameters(user);
        user.setId(getNextId());
        user.setName(StringUtils.hasText(user.getName()) ? user.getName() : user.getLogin());

        log.info("added new user: {}", user);
        return userStorage.addUser(user.getId(), user);
    }

    public User updateUser(User updatedUser) {
        if (updatedUser.getId() == null) {
            log.error("provided wrong ID");
            throw new ValidationException("Id should be indicated");
        }
        if (userStorage.returnIDs().contains(updatedUser.getId())) {
            validateAllParameters(updatedUser);
            log.info("updated user: {}", updatedUser);

            return userStorage.updateUser(updatedUser.getId(), updatedUser);

        } else {
            log.error("provided wrong ID");
            throw new NotFoundException("Indicated wrong id");
        }
    }

    public User returnUserById(long id) {
        if (userStorage.returnIDs().contains(id)) {
            return userStorage.returnUserById(id);
        } else {
            log.error("provided wrong ID");
            throw new NotFoundException("Indicated wrong id");
        }
    }

    public Collection<User> returnFriends(Long userId) {
        if (userStorage.returnIDs().contains(userId)) {
            User currentUser = userStorage.returnUserById(userId);
            return currentUser.getFriends().stream()
                    .map(id -> userStorage.returnUserById(id))
                    .collect(Collectors.toSet());
        } else {
            log.error("provided wrong ID");
            throw new NotFoundException("Indicated wrong id");
        }
    }

    public void addFriend(Long userId, Long newFriendId) {
        if (userStorage.returnIDs().contains(userId) && userStorage.returnIDs().contains(newFriendId)) {
            User currentUser = userStorage.returnUserById(userId);
            User newFriendUser = userStorage.returnUserById(newFriendId);
            currentUser.getFriends().add(newFriendId);
            System.out.println("-- после добавления НД друзья пользователя " + userId + ": " + currentUser.getFriends());
            newFriendUser.getFriends().add(userId);
            System.out.println("-- после добавления НД друзья НД " + newFriendId + ": " + newFriendUser.getFriends());
            log.info("added friends: {}, {}", userId, newFriendId);

        } else {
            log.error("provided wrong IDs");
            throw new NotFoundException("Indicated wrong users' id numbers");
        }
    }

    public void deleteFriend(Long userId, Long formerFriendId) {
        if (userStorage.returnIDs().contains(userId) && userStorage.returnIDs().contains(formerFriendId)) {
            User currentUser = userStorage.returnUsers()
                    .stream()
                    .filter(user -> user.getId() == userId)
                    .findFirst()
                    .get();
            User formerFriendUser = userStorage.returnUsers()
                    .stream()
                    .filter(user -> user.getId() == formerFriendId)
                    .findFirst()
                    .get();
            Set<Long> currentUserFriends = currentUser.getFriends();
            currentUserFriends.remove(formerFriendId);
            Set<Long> formerFriendUserFriends = formerFriendUser.getFriends();
            formerFriendUserFriends.remove(userId);
            log.info("deleted friends: {}, {}", userId, formerFriendId);
        } else {
            log.error("provided wrong IDs");
            throw new NotFoundException("Indicated wrong users' id numbers");
        }
    }

    public Collection<User> mutualFriendsSet(Long userId, Long otherUserId) {
        if (userStorage.returnIDs().contains(userId) && userStorage.returnIDs().contains(otherUserId)) {
            User currentUser = userStorage.returnUserById(userId);
            User otherUser = userStorage.returnUserById(otherUserId);
            Set<Long> userFriends = new HashSet<>(currentUser.getFriends());
            Set<Long> otherFriends = otherUser.getFriends();

            userFriends.retainAll(otherFriends);
            log.info("provided information about mutual friends for users with IDs {} and {}: {}",
                    userId, otherUserId, userFriends);
            return userFriends.stream()
                    .map(id -> userStorage.returnUserById(id))
                    .collect(Collectors.toSet());
        } else {
            log.error("provided wrong IDs");
            throw new NotFoundException("Indicated wrong users' id numbers");
        }
    }

    private long getNextId() {
        long currentMaxId = userStorage.returnIDs()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateAllParameters(User user) {
        if (!emailValidation(user)) {
            log.error("provided wrong email");
            throw new ValidationException("Email should be indicated and have \"@\" symbol");
        }
        if (!loginValidation(user)) {
            log.error("provided wrong login");
            throw new ValidationException("Login cannot be blank or contain spaces");
        }
        if (!birthdayValidation(user)) {
            log.error("provided wrong BD date");
            throw new ValidationException("Birthday date is wrong");
        }
    }

    private boolean emailValidation(User user) {
        return StringUtils.hasText(user.getEmail()) && user.getEmail().contains("@");
    }

    private boolean loginValidation(User user) {
        return StringUtils.hasText(user.getLogin()) && !user.getLogin().contains(" ");
    }

    private boolean birthdayValidation(User user) {
        return user.getBirthday().isBefore(LocalDate.now());
    }
}
