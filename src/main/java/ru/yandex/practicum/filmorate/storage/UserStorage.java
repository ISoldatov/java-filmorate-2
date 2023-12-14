package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User save(User user);

    User update(User user);

    boolean delete(int id);

    User get(int id);

    List<User> getAll();

    void addFriend(int userId, int friendId);

}
