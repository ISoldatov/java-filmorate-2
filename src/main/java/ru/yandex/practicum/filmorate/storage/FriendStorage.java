package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.PathVariable;

public interface FriendStorage {

    void addFriend(int userId, int friendId);
    void removeFriend(int userId, int friendId);

}
