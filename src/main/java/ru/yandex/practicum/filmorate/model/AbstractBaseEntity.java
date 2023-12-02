package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public abstract class AbstractBaseEntity {
    protected Integer id;

    public boolean isNew() {
        return this.id == null;
    }
}
