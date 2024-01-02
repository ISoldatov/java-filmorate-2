package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class InDBLikeStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

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

    public int updateLikes(int filmId, Set<Integer> likes) {
        removeAllFilmLikes(filmId);
        likes.forEach(l -> setLike(filmId, l));
        return likes.size();
    }

    public Set<Integer> getLikesFilm(int filmId) {
        String sqlQuery = "SELECT l.ID_USER  " +
                "            FROM Likes l " +
                "           WHERE l.ID_FILM =?";

        List<Integer> list = jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> rs.getInt("ID_USER"), filmId);
        return new HashSet<>(list);
    }

    public int removeAllFilmLikes(int filmId) {
        String sqlQuery = "DELETE FROM Likes WHERE id_film = ?";
        return jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public List<Integer> getPopFilms(int count) {
        String sqlQuery = "SELECT f.id " +
                "            FROM films f " +
                "       LEFT JOIN Likes l on f.id =l.id_film " +
                "        GROUP BY f.id " +
                "        ORDER BY count(f.id) DESC " +
                "           LIMIT ? ";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("id"), count);
    }


}
