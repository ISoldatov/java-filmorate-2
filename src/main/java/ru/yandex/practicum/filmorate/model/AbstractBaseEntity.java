package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Positive;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractBaseEntity {
    @Positive
    protected Integer id;
    public boolean isNew() {
        return this.id == null;
    }
}
