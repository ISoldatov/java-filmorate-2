package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface LikeStorage {
    void setLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> getPopFilms(int count);


}
