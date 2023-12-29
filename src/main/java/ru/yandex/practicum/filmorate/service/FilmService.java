package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.util.ValidationUtil.checkNotFoundWithId;

@Service
public class FilmService {

    @Autowired
    @Qualifier("inDBFilmStorage")
    private FilmStorage filmStorage;

    @Autowired
    private LikeStorage likeStorage;

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

    public List<Film> getPopFilms(int count) {
        return likeStorage.getPopFilms(count);
    }
}
