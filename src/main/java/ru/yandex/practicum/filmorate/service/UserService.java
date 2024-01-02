package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.ValidationUtil;

import java.util.List;

@Service
public class UserService {

    @Autowired
    @Qualifier("inDBUserStorage")
    private UserStorage userStorage;

    @Autowired
    private FriendStorage friendStorage;

    public User create(User user) {
        checkNameEmpty(user);
        return userStorage.save(user);
    }

    public User update(User user) {
        checkNameEmpty(user);
        return ValidationUtil.checkNotFoundWithId(userStorage.update(user), user.getId());
    }

    public User get(int id) {
        return ValidationUtil.checkNotFoundWithId(userStorage.get(id), id);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public void addFriend(int userId, int friendId) {
        User user = ValidationUtil.checkNotFoundWithId(userStorage.get(userId), userId);
        User friend = ValidationUtil.checkNotFoundWithId(userStorage.get(friendId), friendId);
        friendStorage.addFriend(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        User user = ValidationUtil.checkNotFoundWithId(userStorage.get(userId), userId);
        User friend = ValidationUtil.checkNotFoundWithId(userStorage.get(friendId), friendId);
        friendStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(int userId) {
        return friendStorage.getFriends(userId);
//        User user = ValidationUtil.checkNotFoundWithId(userStorage.get(userId), userId);
//        return user.getFriends().stream().map(userStorage::get).collect(Collectors.toList());
    }

    public List<User> getCommFriends(int id, int otherId) {
        return friendStorage.getCommFriends(id, otherId);
//        List<Integer> allFriendsBothUsers = Stream.of(userStorage.get(id).getFriends(), userStorage.get(otherId).getFriends())
//                .flatMap(java.util.Collection::stream)
//                .collect(Collectors.toList());
//        return allFriendsBothUsers.stream()
//                .filter((i -> Collections.frequency(allFriendsBothUsers, i) > 1))
//                .map(userStorage::get)
//                .distinct()
//                .collect(Collectors.toList());
    }

    private void checkNameEmpty(User user) {
        String name = user.getName();
        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
