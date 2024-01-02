package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MPAStorage;
import ru.yandex.practicum.filmorate.util.ValidationUtil;

import java.util.List;

import static ru.yandex.practicum.filmorate.util.ValidationUtil.checkNotFoundWithId;

@Service
public class FilmService {

    @Autowired
    @Qualifier("inDBFilmStorage")
    private FilmStorage filmStorage;

    @Autowired
    private LikeStorage likeStorage;
    @Autowired
    private MPAStorage mpaStorage;
    @Autowired
    private GenreStorage genreStorage;

    public Film create(Film film) {
        return filmStorage.save(film);
    }

    public Film update(Film film) {
        return checkNotFoundWithId(filmStorage.update(film), film.getId());
    }

    public Film get(int id) {
        return checkNotFoundWithId(filmStorage.get(id), id);

    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public void setLike(int filmId, int userId) {
        Film film = checkNotFoundWithId(filmStorage.get(filmId), filmId);
        checkNotFoundWithId(film.getLikes().add(userId), userId);
        likeStorage.setLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = checkNotFoundWithId(filmStorage.get(filmId), filmId);
        checkNotFoundWithId(film.getLikes().remove(userId), userId);
        likeStorage.removeLike(filmId, userId);
    }

    public MPA getMpa(int id) {
        return ValidationUtil.checkNotFoundWithId(mpaStorage.getMpa(id), id);
    }

    public List<MPA> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    public Genre getGenre(int id) {
        return ValidationUtil.checkNotFoundWithId(genreStorage.get(id), id);
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAll();
    }

    public List<Film> getPopFilms(int count) {
        return filmStorage.getPopFilms(count);
    }


}
