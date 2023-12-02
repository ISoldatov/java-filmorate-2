package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class AbstractBaseEntity {
    protected Integer id;

    public boolean isNew() {
        return this.id == null;
    }
}
