package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    private Integer id;

    @NotNull(message = "Поле email обязательно.")
    @Email
    private String email;

    @NotNull(message = "Поле Login обязательно.")
    @NotBlank(message = "Login не может быть пустым.")
    private String login;

    private String name;

    @Past(message = "Дата рождения должна не может быть в будущем.")
    private LocalDate birthday;
}
