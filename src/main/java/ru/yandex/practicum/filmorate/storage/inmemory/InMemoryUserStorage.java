package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
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
        return storage.computeIfPresent(user.getId(), (i, oldUser) -> user);
    }

    @Override
    public boolean delete(int id) {
        return storage.remove(id) != null;
    }

    @Override
    public User get(int id) {
        return storage.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(storage.values());
    }

}
