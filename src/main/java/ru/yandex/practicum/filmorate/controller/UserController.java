package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.ValidationUtil;
import ru.yandex.practicum.filmorate.util.exception.NotFoundException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
        ValidationUtil.checkNew(user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновлен User c id={}", user.getId());
        ValidationUtil.checkNotNew(user);
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User get(@RequestParam int id) {
        log.info("Получен User c id={}", id);
        return userService.get(id);
    }

    @GetMapping
    public List<User> getAll() {
        log.debug("Получен список всех Users");
        return userService.getAll();
    }


}
