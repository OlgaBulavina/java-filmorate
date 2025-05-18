package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {
    UserService userService = new UserService(new InMemoryUserStorage());

    @Test
    void addFriends() {
        User userOne = new User();
        userOne.setName("name1");
        userOne.setLogin("login1");
        userOne.setBirthday(LocalDate.of(1991, 10, 11));
        userOne.setEmail("email1@yandex.ru");

        User userTwo = new User();
        userTwo.setName("name2");
        userTwo.setLogin("login2");
        userTwo.setBirthday(LocalDate.of(1990, 12, 31));
        userTwo.setEmail("email2@yandex.ru");

        userService.addUser(userOne);
        userService.addUser(userTwo);

        assertEquals(userService.getAllUsers().size(), 2);

        userService.addFriend(userOne.getId(), userTwo.getId());

        assertEquals(userOne.getFriends().size(), 1);
        assertEquals(userTwo.getFriends().size(), 1);

        assertEquals(userOne.getFriends().stream().findFirst().get(), userTwo.getId());
        assertEquals(userTwo.getFriends().stream().findFirst().get(), userOne.getId());
    }

    @Test
    void mutualFriends() {
        User userOne = new User();
        userOne.setName("name1");
        userOne.setLogin("login1");
        userOne.setBirthday(LocalDate.of(1991, 10, 11));
        userOne.setEmail("email1@yandex.ru");

        User userTwo = new User();
        userTwo.setName("name2");
        userTwo.setLogin("login2");
        userTwo.setBirthday(LocalDate.of(1990, 12, 31));
        userTwo.setEmail("email2@yandex.ru");

        User userThree = new User();
        userThree.setName("name3");
        userThree.setLogin("login3");
        userThree.setBirthday(LocalDate.of(1989, 8, 10));
        userThree.setEmail("email3@yandex.ru");

        User userFour = new User();
        userFour.setName("name4");
        userFour.setLogin("login4");
        userFour.setBirthday(LocalDate.of(1988, 3, 23));
        userFour.setEmail("email4@yandex.ru");

        userService.addUser(userOne);
        userService.addUser(userTwo);
        userService.addUser(userThree);
        userService.addUser(userFour);

        userService.addFriend(userOne.getId(), userTwo.getId());
        userService.addFriend(userThree.getId(), userTwo.getId());
        userService.addFriend(userOne.getId(), userFour.getId());
        userService.addFriend(userThree.getId(), userFour.getId());

        Collection<User> mutualFriends = userService.mutualFriendsSet(userOne.getId(), userThree.getId());

        assertEquals(mutualFriends, Set.of(userTwo, userFour));

        User userFive = new User();
        userFive.setName("name5");
        userFive.setLogin("login5");
        userFive.setBirthday(LocalDate.of(1985, 5, 11));
        userFive.setEmail("email5@yandex.ru");
        userService.addUser(userFive);

        assertEquals(userService.mutualFriendsSet(userOne.getId(), userFive.getId()).size(), 0);

        userService.addFriend(userThree.getId(), userFive.getId());

        assertEquals(userService.mutualFriendsSet(userOne.getId(), userFive.getId()).size(), 0);
        assertEquals(userService.mutualFriendsSet(userTwo.getId(), userFive.getId()).size(), 1);
    }

}
