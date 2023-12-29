package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class InDBLikeStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private InDBFilmStorage filmStorage;

    @Autowired
    public InDBLikeStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void setLike(int filmId, int userId) {
        String sqlQuery = "INSERT INTO Likes (id_film, id_user) " +
                "          VALUES (?,?)";

        jdbcTemplate.update(sqlQuery,
                filmId,
                userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String sqlQuery = "DELETE FROM Likes WHERE id_film = ? AND id_user= ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getPopFilms(int count) {
        String sqlQuery = "SELECT f.ID id, " +
                "                 f.NAME naame, " +
                "                 f.DESCRIPTION des , " +
                "                 f.RELEASE_DATE r_date ," +
                "                 f.DURATION dur " +
                "            FROM films f " +
                "           LEFT JOIN likes l ON l.ID_FILM =f.ID " +
                "        GROUP BY f.ID " +
                "        ORDER BY COUNT(f.id) desc " +
                "           LIMIT ?";
        List<Film> list = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> filmStorage.mapRowToFilm(rs, rowNum), count);
        return list;
    }


}
