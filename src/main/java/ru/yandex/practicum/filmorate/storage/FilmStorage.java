package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film save(Film film);

    Film update(Film film);

    boolean delete(int id);

    Film get(int id);

    List<Film> getAll();

    List<Film> getPopFilms(int count);


}
