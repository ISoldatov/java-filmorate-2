package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Component
public class InDBFriendStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public InDBFriendStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery = "INSERT INTO Friends (id_user, id_friend) " +
                "VALUES (?,?)";

        jdbcTemplate.update(sqlQuery,
                userId,
                friendId);
    }
    @Override
    public void removeFriend(int userId, int friendId) {
        String sqlQuery = "DELETE FROM Friends WHERE id_user = ? AND id_friend= ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }
}
