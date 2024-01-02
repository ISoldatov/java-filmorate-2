package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.List;

@Component
public class InDBFilmGenreStorage implements FilmGenreStorage {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public InDBFilmGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int setGenre(int idFilm, int idGenre) {
        String sqlQuery = "INSERT INTO Film_Genre (id_film, id_genre) " +
                "          VALUES (?,?)";
        return jdbcTemplate.update(sqlQuery, idFilm, idGenre);
    }

    public int updateGenre(int idFilm, List<Genre> genres) {
        deleteAllGenreForFilm(idFilm);
        genres.stream().distinct().forEach(g -> setGenre(idFilm, g.getId()));
        return genres.size();
    }

    public int deleteGenre(int idFilm, int idGenre) {
        String sqlQuery = "DELETE FROM Film_Genre " +
                "           WHERE id_film=? " +
                "             AND id_genre=? ";
        return jdbcTemplate.update(sqlQuery, idFilm, idGenre);
    }

    public List<Genre> getGenreFilm(int idFilm) {
        String sqlQuery = "SELECT g.ID ,g.NAME  " +
                "            FROM film_genre fg " +
                "           INNER JOIN genres g ON g.ID =fg.ID_GENRE " +
                "           WHERE fg.ID_FILM= ?" +
                "        ORDER BY g.ID";

        List<Genre> list = (jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name")), idFilm));
        return list;

    }

    public int deleteAllGenreForFilm(int idFilm) {
        String sqlQuery = "DELETE FROM Film_Genre " +
                "           WHERE id_film=? ";
        return jdbcTemplate.update(sqlQuery, idFilm);
    }

}
