package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Set;

public interface LikeStorage {
    void setLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    int updateLikes(int filmId, Set<Integer> likes);

    Set<Integer> getLikesFilm(int filmId);

   List<Integer> getPopFilms(int count);


}
