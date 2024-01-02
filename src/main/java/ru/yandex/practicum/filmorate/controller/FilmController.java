package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.util.ValidationUtil.*;

@RestController
//@RequestMapping("/films")
public class FilmController {
    public static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("Добавлен Film c id={}", film.getId());
        checkNew(film);
        return filmService.create(film);
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновлен Film c id={}", film.getId());
        checkNotNew(film);
        return filmService.update(film);
    }

    @GetMapping(value = "/films/{id}")
    public Film get(@PathVariable int id) {
        log.info("Получен Film c id={}", id);
        checkParams(id);
        return filmService.get(id);
    }

    @GetMapping(value = "/films")
    public List<Film> getAll() {
        log.debug("Получен список всех Films");
        return filmService.getAll();
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void setLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        log.debug("Фильму с id={} ставит like User c id={}", filmId, userId);
        checkParams(filmId, userId);
        filmService.setLike(filmId, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        log.debug("Фильму id={} удаляет like User c id={}", filmId, userId);
//        checkParams(filmId, userId); закомментировал для тестов, хотят код 404 а не 400. Я же ловлю уже здесь отрицательные параметры
        filmService.removeLike(filmId, userId);
    }

    @GetMapping(value = {"/films/popular", "/films/popular?count={count}"})
    public List<Film> getPopFilms(@RequestParam(defaultValue = "10") int count) {
        log.debug("Получение {} самых популярных фильмов", count);
        checkParams(count);
        return filmService.getPopFilms(count);
    }

    @GetMapping(value = "/mpa/{id}")
    public MPA getMpa(@PathVariable int id) {
        return filmService.getMpa(id);
    }

    @GetMapping(value = "/mpa")
    public List<MPA> getAllMpa() {
        log.debug("Получение всех типов рейтингов фильмов");
        return filmService.getAllMpa();
    }

    @GetMapping(value = "/genres/{id}")
    public Genre getGenre(@PathVariable int id) {
        return filmService.getGenre(id);
    }

    @GetMapping(value = "/genres")
    public List<Genre> getAllGenres() {
        log.debug("Получение всех жанров фильмов");
        return filmService.getAllGenres();

    }



}
