package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    public static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Добавлен Film c id={}", film.getId());
        films.putIfAbsent(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(Film film) {
        log.info("Обновлен Film c id={}", film.getId());
        films.computeIfPresent(film.getId(), (i, f) -> film);
        return film;
    }

}
