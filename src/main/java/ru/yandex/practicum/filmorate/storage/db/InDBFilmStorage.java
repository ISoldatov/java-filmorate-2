package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

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

        if (keyHolder.getKeys().size() > 1) {
            film.setId((int) keyHolder.getKeys().get("id"));
        } else {
            film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        }
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
        String sqlQuery = "SELECT f.id id, " +
                "                 f.NAME naame, " +
                "                 f.DESCRIPTION des, " +
                "                 f.RELEASE_DATE r_date, " +
                "                 f.DURATION dur " +
                "            FROM FILMS f " +
                "           INNER JOIN mpa m ON m.ID =f.MPA " +
                "           WHERE f.id=? ";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, id);
        if (films.isEmpty()) {
            return null;
        }
        return films.get(0);
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT f.id id, " +
                "                 f.NAME naame, " +
                "                 f.DESCRIPTION des, " +
                "                 f.RELEASE_DATE r_date, " +
                "                 f.DURATION dur " +
                "            FROM FILMS f " +
                "           INNER JOIN mpa m ON m.ID =f.MPA ";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        Film film = Film.builder()
                .id(id)
                .name(rs.getString("naame"))
                .description(rs.getString("des"))
                .releaseDate(rs.getDate("r_date").toLocalDate())
                .duration(rs.getInt("dur"))
//                .mpa(new MPA(rs.getInt("mpa"), rs.getString("mpa_title")))
                .mpa(getMPA(id))
                .genres(getGenreFilm(id))
                .likes(getLikesFilm(id))
                .build();
        System.out.println(film);
        return film;
    }

    private MPA getMPA(int id) {
        String sqlQuery = "SELECT m.id ," +
                "                 m.title " +
                "            FROM mpa m " +
                "           INNER JOIN Films f ON f.mpa = m.id AND f.id =?";
//        MPA mpa = jdbcTemplate.queryForObject(sqlQuery, new BeanPropertyRowMapper<MPA>(MPA.class), id);

        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);

        // обрабатываем результат выполнения запроса
        if(mpaRows.next()) {
            MPA mpa = new MPA(
                    mpaRows.getInt("id"),
                    mpaRows.getString("title"));


//            log.info("Найден пользователь: {} {}", user.getId(), user.getNickname());

            return mpa;
        } else {
//            log.info("Пользователь с идентификатором {} не найден.", id);
            return null;
        }


//        return mpa;
//        return jdbcTemplate.queryForObject(sqlQuery, new BeanPropertyRowMapper<MPA>(MPA.class), id);
    }

    private Set<Genre> getGenreFilm(int id) {
        String sqlQuery = "SELECT g.ID ,g.NAME  " +
                "            FROM film_genre fg " +
                "           INNER JOIN genres g ON g.ID =fg.ID_GENRE " +
                "           WHERE fg.ID_FILM= ?";

        List<Genre> list = jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name")), id
        );
        return new HashSet<>(list);
    }

    public Set<Integer> getLikesFilm(int id) {
        String sqlQuery = "SELECT l.ID_USER  " +
                "            FROM likes l " +
                "           WHERE l.ID_FILM =?";

        List<Integer> list = jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> rs.getInt("id"), id);
        return new HashSet<>(list);
    }

}
