package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.ValidationUtil;

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
    private final Map<Integer, User> users = new HashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Добавлен User c id={}", user.getId());
        ValidationUtil.checkNew(user);
        user.setId(counter.incrementAndGet());
        checkNameEmpty(user);
        users.putIfAbsent(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновлен User c id={}", user.getId());
        ValidationUtil.checkNotNew(user);
        checkNameEmpty(user);
        return ValidationUtil.checkNotFound(users.computeIfPresent(user.getId(), (i, f) -> user),user.getId());
    }

    @GetMapping
    public List<User> getAll() {
        log.debug("Получен список всех Users");
        return new ArrayList<>(users.values());
    }

    private void checkNameEmpty(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }

}
