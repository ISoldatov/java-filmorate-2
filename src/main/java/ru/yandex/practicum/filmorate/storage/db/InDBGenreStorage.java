package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Component
public class InDBGenreStorage implements GenreStorage {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public InDBGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre get(int id) {
        String sqlQuery = "SELECT id, " +
                "                 name " +
                "            FROM Genres " +
                "           WHERE id=?";

        List<Genre> Genres = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name")), id);
        if (Genres.isEmpty()) {
            return null;
        }
        return Genres.get(0);
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT id, " +
                "                name " +
                "           FROM Genres";

        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name")));
    }



}
