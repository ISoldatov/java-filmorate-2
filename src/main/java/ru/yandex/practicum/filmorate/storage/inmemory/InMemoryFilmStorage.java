package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> storage = new HashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    public Film save(Film film) {
        film.setId(counter.incrementAndGet());
        storage.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        return storage.computeIfPresent(film.getId(), (i, oldFilm) -> film);
    }

    @Override
    public boolean delete(int id) {
        return storage.remove(id) != null;
    }

    @Override
    public Film get(int id) {
        return storage.get(id);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(storage.values());
    }

    public List<Film> getPopFilms(int count) {
        return null;
    }

}
