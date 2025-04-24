package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Set;

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
    public User getUser(@PathVariable long id) {
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
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<Long> showFriendsIds(@PathVariable long id) {
        return userService.returnUserById(id).getFriends();
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<Long> showMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.mutualFriendsSet(id, otherId);
    }
}
