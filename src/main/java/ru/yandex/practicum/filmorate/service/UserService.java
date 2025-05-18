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
import java.util.Optional;

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
        if (!userStorage.idExists(updatedUser.getId())) {
            log.error("provided wrong ID");
            throw new NotFoundException("Indicated wrong id");
        } else {
            validateAllParameters(updatedUser);
            log.info("updated user: {}", updatedUser);

            return userStorage.updateUser(updatedUser.getId(), updatedUser);
        }
    }

    public User returnUserById(long id) {
        Optional<User> currentUserOptional = Optional.of(userStorage.returnUserById(id));
        if (!currentUserOptional.isPresent()) {
            log.error("provided wrong ID");
            throw new NotFoundException("Indicated wrong id");
        } else {
            return currentUserOptional.get();
        }
    }

    public Collection<User> returnFriends(Long userId) {
        if (!userStorage.idExists(userId)) {
            log.error("provided wrong ID");
            throw new NotFoundException("Indicated wrong id");
        } else {
            return userStorage.returnFriends(userId);
        }
    }

    public void addFriend(Long userId, Long newFriendId) {
        if (!userStorage.idExists(userId) || !userStorage.idExists(newFriendId)) {
            log.error("provided wrong IDs");
            throw new NotFoundException("Indicated wrong users' id numbers");
        } else {
            User currentUser = userStorage.returnUserById(userId);
            User newFriendUser = userStorage.returnUserById(newFriendId);

            currentUser.getFriends().add(newFriendId);
            newFriendUser.getFriends().add(userId);
            log.info("added friends: {}, {}", userId, newFriendId);
        }
    }

    public void deleteFriend(Long userId, Long formerFriendId) {
        if (!userStorage.idExists(userId) || !userStorage.idExists(formerFriendId)) {
            log.error("provided wrong IDs");
            throw new NotFoundException("Indicated wrong users' id numbers");
        } else {
            User currentUser = userStorage.returnUserById(userId);
            User formerFriendUser = userStorage.returnUserById(formerFriendId);

            currentUser.getFriends().remove(formerFriendId);
            formerFriendUser.getFriends().remove(userId);
            log.info("deleted friends: {}, {}", userId, formerFriendId);
        }
    }

    public Collection<User> mutualFriendsSet(Long userId, Long otherUserId) {
        if (!userStorage.idExists(userId) || !userStorage.idExists(otherUserId)) {
            log.error("provided wrong IDs");
            throw new NotFoundException("Indicated wrong users' id numbers");
        } else {
            Collection<User> commonFriends = userStorage.mutualFriends(userId, otherUserId);
            log.info("provided information about mutual friends for users with IDs {} and {}: {}",
                    userId, otherUserId, commonFriends);
            return commonFriends;
        }
    }

    private long getNextId() {
        long currentMaxId = userStorage.returnUsers()
                .stream()
                .mapToLong(user -> user.getId())
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
