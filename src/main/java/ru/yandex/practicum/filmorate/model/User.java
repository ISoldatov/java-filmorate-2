package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractBaseEntity {

    @NotNull(message = "Поле email обязательно.")
    @Email
    private String email;

    @NotNull(message = "Поле Login обязательно.")
    @NotBlank(message = "Login не может быть пустым.")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,20}$", message = "Login должен содержать латинские символы и цифры. Длина от 3 до 20 символов.")
    private String login;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();
}
