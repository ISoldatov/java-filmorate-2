package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    public static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Добавлен User c id={}", user.getId());
        users.putIfAbsent(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Обновлен User c id={}", user.getId());
        users.computeIfPresent(user.getId(), (i, f) -> user);
        return user;
    }

    @GetMapping
    public List<User> getAll() {
        log.debug("Получен список всех Users");
        return new ArrayList<>(users.values());
    }

}
