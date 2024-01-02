package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

@Component
public class InDBFriendStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private InDBUserStorage userStorage;

    public InDBFriendStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery = "INSERT INTO Friends (id_user, id_friend) " +
                "          VALUES (?,?)";

        jdbcTemplate.update(sqlQuery,
                userId,
                friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String sqlQuery = "DELETE FROM Friends WHERE id_user = ? AND id_friend= ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getFriends(int userId) {
        String sqlQuery = "SELECT u.* " +
                "            FROM Friends f " +
                "           INNER JOIN Users u ON u.ID =f.ID_FRIEND " +
                "           WHERE f.ID_USER =?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> userStorage.mapRowToUser(rs, rowNum), userId);
    }

    @Override
    public List<User> getCommFriends(int userId, int friendId) {
        String sqlQuery = "SELECT u.* " +
                "            FROM Users u " +
                "           WHERE u.ID IN (SELECT f2.ID_FRIEND " +
                "                            FROM friends f2 " +
                "                           WHERE f2.ID_FRIEND  IN (SELECT f.ID_FRIEND " +
                "                                                   FROM friends f " +
                "                                                  WHERE f.ID_USER =?) " +
                "                           AND f2.ID_USER =?)";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> userStorage.mapRowToUser(rs, rowNum), userId, friendId);
    }
}


