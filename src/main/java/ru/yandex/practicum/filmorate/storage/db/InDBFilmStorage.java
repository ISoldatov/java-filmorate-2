package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("inDBFilmStorage")
public class InDBFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private MPAStorage mpaStorage;
    private FilmGenreStorage filmGenreStorage;
    private LikeStorage likeStorage;

    public InDBFilmStorage(JdbcTemplate jdbcTemplate, MPAStorage mpaStorage, FilmGenreStorage filmGenreStorage, LikeStorage likeStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.filmGenreStorage = filmGenreStorage;
        this.likeStorage = likeStorage;
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

        film.getGenres().forEach(genre -> filmGenreStorage.setGenre(film.getId(), genre.getId()));
        film.getLikes().forEach(like -> likeStorage.setLike(film.getId(), like));

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

        filmGenreStorage.updateGenre(film.getId(), film.getGenres());
        likeStorage.updateLikes(film.getId(), film.getLikes());

        return numRow == 0 ? null : get(film.getId());
    }

    @Override
    public boolean delete(int id) {
        String sqlQuery = "DELETE FROM Films WHERE id = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public Film get(int idFilm) {
        String sqlQuery = "SELECT f.id, " +
                "                 f.NAME, " +
                "                 f.DESCRIPTION, " +
                "                 f.RELEASE_DATE, " +
                "                 f.DURATION, " +
                "                 f.mpa " +
                "            FROM FILMS f " +
                "           WHERE f.id=? ";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, idFilm);

        Film film;
        if (films.isEmpty()) {
            return null;
        } else {
            film = films.get(0);
        }

        film.setGenres(filmGenreStorage.getGenreFilm(idFilm));
        film.setLikes(likeStorage.getLikesFilm(idFilm));
        return film;
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT f.id, " +
                "                 f.NAME, " +
                "                 f.DESCRIPTION, " +
                "                 f.RELEASE_DATE, " +
                "                 f.DURATION, " +
                "                 f.mpa " +
                "            FROM FILMS f " +
                "        ORDER BY f.id ASC";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);

        films.forEach(f -> {
            f.setGenres(filmGenreStorage.getGenreFilm(f.getId()));
            f.setLikes(likeStorage.getLikesFilm(f.getId()));
        });
        return films;
    }

    @Override
    public List<Film> getPopFilms(int count) {
        List<Film> films = likeStorage.getPopFilms(count).stream()
                .map(this::get)
                .collect(Collectors.toList());
        return films;
    }

    Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        Film film = Film.builder()
                .id(id)
                .name(rs.getString("name"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpa(mpaStorage.getMpa(rs.getInt("mpa")))
                .genres(filmGenreStorage.getGenreFilm(id))
                .likes(likeStorage.getLikesFilm(id))
                .build();
        System.out.println(film);
        return film;
    }


}
