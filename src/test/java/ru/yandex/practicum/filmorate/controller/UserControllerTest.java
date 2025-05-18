package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    User user;
    UserController userController = new UserController(new UserService(new InMemoryUserStorage()));

    @Test
    void addCorrectUser() {
        user = new User();
        user.setName("name");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(1990, 12, 31));
        user.setEmail("email@yandex.ru");

        userController.add(user);
        assertEquals(userController.get().size(), 1, "collection size must equal 1");

    }

    @Test
    void addUserWithIncorrectEmail() {
        user = new User();
        user.setName("name");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(1990, 12, 31));
        user.setEmail("email yandex.ru");

        assertThrows(ValidationException.class, () -> userController.add(user),
                "method should throw an exception");
        assertEquals(userController.get().size(), 0, "collection size must equal 0");
    }

    @Test
    void addUserWithIncorrectLogin() {
        user = new User();
        user.setName("name");
        user.setLogin(" ");
        user.setBirthday(LocalDate.of(1990, 12, 31));
        user.setEmail("email@yandex.ru");

        assertThrows(ValidationException.class, () -> userController.add(user),
                "method should throw an exception");
        assertEquals(userController.get().size(), 0, "collection size must equal 0");
    }

    @Test
    void addUserWithIncorrectBirthdayDate() {
        user = new User();
        user.setName("name");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2100, 12, 31));
        user.setEmail("email@yandex.ru");

        assertThrows(ValidationException.class, () -> userController.add(user),
                "method should throw an exception");
        assertEquals(userController.get().size(), 0, "collection size must equal 0");
    }

    @Test
    void addUserWithEmptyName() {
        user = new User();
        user.setName("");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(1990, 12, 31));
        user.setEmail("email@yandex.ru");
        userController.add(user);
        assertEquals(user.getName(), user.getLogin(), "login should be used instead of blank name");
    }

    @Test
    void getAllUsers() {
        user = new User();
        user.setName("nameOne");
        user.setLogin("loginOne");
        user.setBirthday(LocalDate.of(1990, 12, 31));
        user.setEmail("emailOne@yandex.ru");
        userController.add(user);

        User userTwo = new User();
        userTwo.setName("nameTwo");
        userTwo.setLogin("loginTwo");
        userTwo.setBirthday(LocalDate.of(1980, 1, 1));
        userTwo.setEmail("emailTwo@yandex.ru");
        userController.add(userTwo);

        assertEquals(userController.get().size(), 2, "collection size must equal 2");
    }

    @Test
    void updateCorrectUser() {
        user = new User();
        user.setName("nameOne");
        user.setLogin("loginOne");
        user.setBirthday(LocalDate.of(1990, 12, 31));
        user.setEmail("emailOne@yandex.ru");
        userController.add(user);

        User userTwo = new User();
        userTwo.setId(user.getId());
        userTwo.setName("nameTwo");
        userTwo.setLogin("loginTwo");
        userTwo.setBirthday(LocalDate.of(1980, 1, 1));
        userTwo.setEmail("emailTwo@yandex.ru");
        userController.update(userTwo);

        assertEquals(user.getEmail(), userTwo.getEmail(), "email should be equal");
    }

    @Test
    void updateUserWithIncorrectEmail() {
        user = new User();
        user.setName("nameOne");
        user.setLogin("loginOne");
        user.setBirthday(LocalDate.of(1990, 12, 31));
        user.setEmail("emailOne@yandex.ru");
        userController.add(user);

        User userTwo = new User();
        userTwo.setId(user.getId());
        userTwo.setName("nameTwo");
        userTwo.setLogin("loginTwo");
        userTwo.setBirthday(LocalDate.of(1980, 1, 1));
        userTwo.setEmail("emailTwo yandex.ru");

        assertThrows(ValidationException.class, () -> userController.update(userTwo),
                "method should throw an exception");
        assertEquals(user.getName(), "nameOne", "data should remain unchanged");
    }

    @Test
    void updateUserWithIncorrectLogin() {
        user = new User();
        user.setName("nameOne");
        user.setLogin("loginOne");
        user.setBirthday(LocalDate.of(1990, 12, 31));
        user.setEmail("emailOne@yandex.ru");
        userController.add(user);

        User userTwo = new User();
        userTwo.setId(user.getId());
        userTwo.setName("nameTwo");
        userTwo.setLogin(" ");
        userTwo.setBirthday(LocalDate.of(1980, 1, 1));
        userTwo.setEmail("emailTwo@yandex.ru");

        assertThrows(ValidationException.class, () -> userController.update(userTwo),
                "method should throw an exception");
        assertEquals(user.getName(), "nameOne", "data should remain unchanged");
    }

    @Test
    void updateUserWithIncorrectBirthdayDate() {
        user = new User();
        user.setName("nameOne");
        user.setLogin("loginOne");
        user.setBirthday(LocalDate.of(1990, 12, 31));
        user.setEmail("emailOne@yandex.ru");
        userController.add(user);

        User userTwo = new User();
        userTwo.setId(user.getId());
        userTwo.setName("nameTwo");
        userTwo.setLogin("loginTwo");
        userTwo.setBirthday(LocalDate.of(2080, 1, 1));
        userTwo.setEmail("emailTwo@yandex.ru");

        assertThrows(ValidationException.class, () -> userController.update(userTwo),
                "method should throw an exception");
        assertEquals(user.getName(), "nameOne", "data should remain unchanged");
    }

    @Test
    void updateUserWithEmptyName() {
        user = new User();
        user.setName("nameOne");
        user.setLogin("loginOne");
        user.setBirthday(LocalDate.of(1990, 12, 31));
        user.setEmail("emailOne@yandex.ru");
        userController.add(user);

        User userTwo = new User();
        userTwo.setId(user.getId());
        userTwo.setName(" ");
        userTwo.setLogin("loginTwo");
        userTwo.setBirthday(LocalDate.of(1980, 1, 1));
        userTwo.setEmail("emailTwo@yandex.ru");

        userController.update(userTwo);
        assertEquals(user.getName(), "loginTwo", "login should be used instead name after update");
    }
}