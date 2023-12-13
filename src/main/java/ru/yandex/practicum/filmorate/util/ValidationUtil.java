package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.AbstractBaseEntity;
import ru.yandex.practicum.filmorate.util.exception.NotFoundException;

@UtilityClass
public class ValidationUtil {
    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " должна быть new (id=null).");
        }
    }

    public static void checkNotNew(AbstractBaseEntity entity) {
        if (entity.isNew()) {
            throw new IllegalArgumentException(entity + " должна быть не new (id!=null).");
        }
    }

    public static <T> T checkNotFound(T object, Integer id) {
        if (object == null) {
            throw new NotFoundException("Не найден объект с Id= " + id);
        }
        return object;
    }
}