package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.util.ValidationUtil;
import ru.yandex.practicum.filmorate.util.exception.NotFoundException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film create(Film film) {
        return filmStorage.save(film);
    }

    public Film update(Film film) {
        return ValidationUtil.checkNotFound(filmStorage.update(film), film.getId());
    }

    public Film get(int id) {
        return ValidationUtil.checkNotFound(filmStorage.get(id), id);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public void setLike(int filmId, int userId) {
        filmStorage.get(filmId).getLikes().add(userId);
    }

    public void removeLike(int filmId, int userId) {
        filmStorage.get(filmId).getLikes().remove(userId);
    }

    public List<Film> getPopFilms(int count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparing(Film::getCountLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
