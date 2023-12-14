package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.ValidationUtil;

import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        checkNameEmpty(user);
        return userStorage.save(user);
    }

    public User update(User user) {
        checkNameEmpty(user);
        return ValidationUtil.checkNotFound(userStorage.update(user), user.getId());
    }

    public User get(int id) {
        return ValidationUtil.checkNotFound(userStorage.get(id), id);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }


    private void checkNameEmpty(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}