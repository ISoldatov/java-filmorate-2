package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Component("inDBFilmStorage")
public class InDBFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public InDBFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film save(Film film) {
        String sqlQuery = "INSERT INTO Films (name, description, release_date, duration, mpa) " +
                "VALUES (?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE Films " +
                "             SET name = ?, " +
                "                 description = ?, " +
                "                 release_date = ?, " +
                "                 duration = ?, " +
                "                 mpa = ? " +
                "           WHERE id = ?";

        int numRow = jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , Date.valueOf(film.getReleaseDate())
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());

        return numRow == 0 ? null : film;
    }

    @Override
    public boolean delete(int id) {
        String sqlQuery = "DELETE FROM Films WHERE id = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public Film get(int id) {
        String sqlQuery = "SELECT f.id id, f.NAME name, f.DESCRIPTION desc, f.RELEASE_DATE r_date, f.DURATION dur, f.MPA mpa, m.TITLE mpa_title " +
                "FROM FILMS f " +
                "INNER JOIN mpa m ON m.ID =f.MPA " +
                "WHERE f.id=? ";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, id);
        if (films.isEmpty()) {
            return null;
        }
        return films.get(0);
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT f.id id, f.NAME name, f.DESCRIPTION desc, f.RELEASE_DATE r_date, f.DURATION dur, f.MPA mpa, m.TITLE mpa_title " +
                "            FROM FILMS f " +
                "           INNER JOIN mpa m ON m.ID =f.MPA ";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("desc"))
                .releaseDate(rs.getDate("r_date").toLocalDate())
                .duration(rs.getInt("dur"))
                .mpa(new MPA(rs.getInt("mpa"), rs.getString("mpa_title")))
                .genres(getGenreFilm(rs.getInt("id")))
                .build();
        System.out.println(film);
        return film;

    }

    private Set<Genre> getGenreFilm(int id) {
        String sqlQuery = "SELECT g.ID id, g.NAME name " +
                "          FROM " +
                " film_genre fg " +
                " INNER JOIN genres g ON g.ID =fg.ID_GENRE " +
                " WHERE fg.ID_FILM= ?";

        List<Genre> list = jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name")),id
        );
        return new HashSet<>(list);
    }
}
