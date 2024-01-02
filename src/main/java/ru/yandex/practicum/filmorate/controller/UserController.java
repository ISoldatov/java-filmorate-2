package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;

import java.util.List;

import static ru.yandex.practicum.filmorate.util.ValidationUtil.*;

@RestController
@RequestMapping("/users")
public class UserController {
    public static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Добавлен User {}", user);
        checkNew(user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновлен User c id={}", user.getId());
        checkNotNew(user);
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        log.info("Получен User c id={}", id);
        checkParams(id);
        return userService.get(id);
    }

    @GetMapping
    public List<User> getAll() {
        log.debug("Получен список всех Users");
        return userService.getAll();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        log.debug("Добавление в друзья пользователя id={} друга с friendId={}", userId, friendId);
//        checkParams(userId, friendId); --закомментировал для прохождения тестов хотят код 404 а не 400.
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        log.debug("Удаление из друзей пользователя с id={} друга с friendId={}", userId, friendId);
        checkParams(userId, friendId);
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") int userId) {
        log.debug("Получение списка друзей пользователя с id={}", userId);
        checkParams(userId);
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommFriends(@PathVariable("id") int userId, @PathVariable("otherId") int friendId) {
        log.debug("Получение общего списка друзей пользователя с id={} и друга с friendId={}", userId, friendId);
        checkParams(userId, friendId);
        return userService.getCommFriends(userId, friendId);
    }

}
