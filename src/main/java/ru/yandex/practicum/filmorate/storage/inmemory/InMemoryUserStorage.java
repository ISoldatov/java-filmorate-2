package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.ValidationUtil;
import ru.yandex.practicum.filmorate.util.exception.NotFoundException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> storage = new HashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    public User save(User user) {
        user.setId(counter.incrementAndGet());
        storage.put(user.getId(), user);
        return user;
    }

    public User update(User user) {

        return ValidationUtil.checkNotFound(storage.computeIfPresent(user.getId(), (i, f) -> user), user.getId());
    }

    @Override
    public boolean delete(int id) {
        return storage.remove(id) != null;
    }

    @Override
    public User get(int id) {
        if (!storage.containsKey(id)) {
            throw new NotFoundException(String.format("User с id=%d не найден", id));
        }
        return storage.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(storage.values());
    }


}
