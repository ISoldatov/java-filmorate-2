package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.util.List;
import java.util.Objects;

@Component("inDBUserStorage")
public class InDBUserStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public InDBUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User save(User user) {
        String sqlQuery = "INSERT INTO Users (email, login, name, birthday) " +
                "VALUES (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        if (keyHolder.getKeys().size() > 1) {
            user.setId((int) keyHolder.getKeys().get("id"));
        } else {
            user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        }
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE Users " +
                "SET email = ?, " +
                "login = ?, " +
                "name = ?, " +
                "birthday = ? " +
                "WHERE id = ?";

        int numRow = jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , Date.valueOf(user.getBirthday())
                , user.getId());

        return numRow == 0 ? null : user;
    }

    @Override
    public boolean delete(int id) {
        String sqlQuery = "DELETE FROM Users WHERE id = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public User get(int id) {
        String sqlQuery = "SELECT id, email, login, name, birthday " +
                "            FROM Users " +
                "           WHERE id = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT id, email, login, name, birthday " +
                "FROM Users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();

    }
}
