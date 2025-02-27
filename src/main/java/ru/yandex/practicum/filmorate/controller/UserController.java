package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection get() {
        log.info("provided list of users: " + users.values());
        return users.values();
    }

    @PostMapping
    public User add(@RequestBody User newUser) {
        if (!emailValidation(newUser)) {
            log.error("provided wrong email");
            throw new ValidationException("Email should be indicated and have \"@\" symbol");
        }
        if (!loginValidation(newUser)) {
            log.error("provided wrong login");
            throw new ValidationException("Login cannot be blank or contain spaces");
        }
        if (!birthdayValidation(newUser)) {
            log.error("provided wrong BD date");
            throw new ValidationException("Birthday date is wrong");
        }

        newUser.setId(getNextId());
        newUser.setName(StringUtils.hasText(newUser.getName()) ? newUser.getName() : newUser.getLogin());
        users.put(newUser.getId(), newUser);
        log.info("added new user: {}", newUser);

        return newUser;
    }

    @PutMapping
    public User update(@RequestBody User updatedUser) {
        if (updatedUser.getId() == null) {
            log.error("provided wrong ID");
            throw new ValidationException("Id should be indicated");
        }

        if (users.containsKey(updatedUser.getId())) {
            if (!emailValidation(updatedUser)) {
                log.error("provided wrong email");
                throw new ValidationException("Email should be indicated and have \"@\" symbol");
            }
            if (!loginValidation(updatedUser)) {
                log.error("provided wrong login");
                throw new ValidationException("Login cannot be blank or contain spaces");
            }
            if (!birthdayValidation(updatedUser)) {
                log.error("provided wrong BD date");
                throw new ValidationException("Birthday date is wrong");
            }

            User oldUser = users.get(updatedUser.getId());
            oldUser.setName(updatedUser.getName() == null || updatedUser.getName().isEmpty()
                    || updatedUser.getName().isBlank() ? updatedUser.getLogin() : updatedUser.getName());
            oldUser.setEmail(updatedUser.getEmail());
            oldUser.setLogin(updatedUser.getLogin());
            oldUser.setBirthday(updatedUser.getBirthday());
            log.info("updated user: {}", oldUser);

            return oldUser;

        } else {
            log.error("provided wrong ID");
            throw new ValidationException("Indicated wrong id");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
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
