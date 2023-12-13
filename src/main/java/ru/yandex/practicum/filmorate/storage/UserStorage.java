package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User save(User film);

    User update(User film);

    boolean delete(int id);

    User get(int id);

    List<User> getAll();

}
