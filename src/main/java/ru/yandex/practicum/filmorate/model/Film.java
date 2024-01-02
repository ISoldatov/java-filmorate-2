package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.util.annotation.MinimumDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Film extends AbstractBaseEntity {

    @NotNull(message = "Название фильма обязательно.")
    @NotBlank(message = "Название не может быть пустым.")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания - 200 символов.")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @MinimumDate
    @Past(message = "Дата релиза не может быть раньше 28 декабря 1895 года.")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом.")
    private int duration;

    @NotNull(message = "Рейтинг фильма обязателен")
    private MPA mpa;

    private List<Genre> genres = new ArrayList<>();

    private Set<Integer> likes = new HashSet<>();

    public int getCountLikes() {
        return likes.size();
    }
}
