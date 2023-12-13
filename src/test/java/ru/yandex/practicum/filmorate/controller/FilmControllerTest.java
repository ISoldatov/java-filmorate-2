package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

class FilmControllerTest {

    private static FilmController filmController;
    private static Film normalFilm;
    private static Film borderReleaseDateFilm;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();

        normalFilm = Film.builder()
                .name("normalFilm")
                .description("description")
                .releaseDate(LocalDate.of(2023, 10, 15))
                .duration(100)
                .build();

        borderReleaseDateFilm = Film.builder()
                .name("normalFilm")
                .description("description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100)
                .build();
    }

    @Test
    void createNormalFilm() {
        Film normalFilmWithID = Film.builder()
                .id(1)
                .name("normalFilm")
                .description("description")
                .releaseDate(LocalDate.of(2023, 10, 15))
                .duration(100)
                .build();
        Film addFilm = filmController.create(normalFilm);
        Assertions.assertEquals(normalFilmWithID, addFilm);
    }

    @Test
    void createBorderReleaseDateFilm() {
        Film borderReleaseDateFilmWithId = Film.builder()
                .id(1)
                .name("normalFilm")
                .description("description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100)
                .build();
        Film addFilm = filmController.create(borderReleaseDateFilm);
        Assertions.assertEquals(borderReleaseDateFilmWithId, addFilm);
    }

    @Test
    void update() {
    }

    @Test
    void getAll() {
        Film normalFilmWithID = Film.builder()
                .id(1)
                .name("normalFilm")
                .description("description")
                .releaseDate(LocalDate.of(2023, 10, 15))
                .duration(100)
                .build();
        Film borderReleaseDateFilmWithId = Film.builder()
                .id(2)
                .name("normalFilm")
                .description("description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100)
                .build();
        filmController.create(normalFilm);
        filmController.create(borderReleaseDateFilm);
        Assertions.assertArrayEquals(new Film[]{normalFilmWithID, borderReleaseDateFilmWithId}, filmController.getAll().toArray());
    }
}