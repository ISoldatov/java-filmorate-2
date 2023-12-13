package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class TestFilms {
    public static Film normalFilm = Film.builder()
            .name("normalFilm")
            .description("description")
            .releaseDate(LocalDate.of(2023, 10, 15))
            .duration(100)
            .build();

    public static Film normalFilmWithID = Film.builder()
            .id(1)
            .name("normalFilm")
            .description("description")
            .releaseDate(LocalDate.of(2023, 10, 15))
            .duration(100)
            .build();

    public static Film borderReleaseDateFilm = Film.builder()
            .name("normalFilm")
            .description("description")
            .releaseDate(LocalDate.of(1895, 12, 28))
            .duration(100)
            .build();

    public static Film borderReleaseDateFilmWithId = Film.builder()
            .id(1)
            .name("normalFilm")
            .description("description")
            .releaseDate(LocalDate.of(1895, 12, 28))
            .duration(100)
            .build();

    public static Film errorReleaseDateFilm = Film.builder()
            .name("normalFilm")
            .description("description")
            .releaseDate(LocalDate.of(1895, 12, 27))
            .duration(100)
            .build();


}
