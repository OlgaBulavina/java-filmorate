package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;

    @GetMapping
    public Collection get() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}") //new
    public User getUser(@PathVariable Long id) {
        return userService.returnUserById(id);
    }

    @PostMapping
    public User add(@RequestBody User newUser) {
        return userService.addUser(newUser);
    }

    @PutMapping
    public User update(@RequestBody User updatedUser) {
        return userService.updateUser(updatedUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        System.out.println("-- запрошено добавление друзей " + id + " & " + friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> showFriends(@PathVariable Long id) {
        return userService.returnFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> showMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.mutualFriendsSet(id, otherId);
    }
}
