package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreStorage {

    int setGenre(int idFilm, int idGenre);

    int updateGenre(int idFilm, List<Genre> genres);

    int deleteGenre(int idFilm, int idGenre);

    List<Genre> getGenreFilm(int idFilm);

}
