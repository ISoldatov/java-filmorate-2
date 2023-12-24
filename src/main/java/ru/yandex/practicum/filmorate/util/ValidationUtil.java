package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.AbstractBaseEntity;
import ru.yandex.practicum.filmorate.util.exception.NotFoundException;
import ru.yandex.practicum.filmorate.util.exception.ValidationException;

@UtilityClass
public class ValidationUtil {
    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new ValidationException(entity.getClass().getSimpleName() + " должна быть new (id=null).");
        }
    }

    public static void checkNotNew(AbstractBaseEntity entity) {
        if (entity.isNew()) {
            throw new ValidationException(entity.getClass().getSimpleName() + " должна быть не new (id!=null).");
        }
    }

    public static void checkParams(int... params) {
        for (int i : params) {
            if (i <= 0) {
                throw new ValidationException("Параметры запроса должны быть положительные.");
            }
        }
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException(String.format("Не найден объект с " + msg));
        }
    }
}
