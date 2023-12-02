package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.TestFilms;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.TestFilms.*;

class FilmControllerTest {

    private static FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void createNormalFilm() {
        filmController.create(normalFilm);
        Assertions.assertArrayEquals(new Film[]{normalFilm}, filmController.getAll().toArray());
    }

    @Test
    void createBorderReleaseDateFilm() {
        filmController.create(borderReleaseDateFilm);
        Assertions.assertArrayEquals(new Film[]{borderReleaseDateFilm}, filmController.getAll().toArray());
    }

    @Test
    void update() {
    }

    @Test
    void getAll() {
        filmController.create(normalFilm);
        filmController.create(borderReleaseDateFilm);
        Assertions.assertArrayEquals(new Film[]{normalFilm,borderReleaseDateFilm}, filmController.getAll().toArray());
    }
}