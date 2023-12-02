package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.ValidationUtil;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/films")
public class FilmController {
    public static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Добавлен Film c id={}", film.getId());
        ValidationUtil.checkNew(film);
        film.setId(counter.incrementAndGet());
        films.putIfAbsent(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновлен Film c id={}", film.getId());
        ValidationUtil.checkNotNew(film);
        return ValidationUtil.checkNotFound(films.computeIfPresent(film.getId(), (i, f) -> film),film.getId());
    }

    @GetMapping
    public List<Film> getAll() {
        log.debug("Получен список всех Films");
        return new ArrayList<>(films.values());
    }

}
