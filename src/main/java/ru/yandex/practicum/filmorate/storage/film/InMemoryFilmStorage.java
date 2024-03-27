package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final LocalDate minRelaseDate = LocalDate.parse("1895-12-28");
    private final Integer maxDescriptionLenngth = 200;
    private Integer filmID = 1;
    private final Map<Integer,Film> films = new HashMap<Integer, Film>();

    private Integer generatedID() {
        log.debug("Generated filmID: " + filmID);
        return filmID++;
    }

    @Override
    public Film create(Film film) {
        film.setId(generatedID());
        films.put(film.getId(), film);
        log.info("Создан фильм: " + film);

        return film;
    }

    @Override
    public Collection<Film> getFilms() {
        log.info("Получить все фильмы");
        return films.values();
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновлен фильм: " + film);
            return film;
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }

    @Override
    public Film getFilmByID(Integer id) {
        if (films.containsKey(id)) {
            log.info("Получен фильм: " + films.get(id));
            return films.get(id);
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }
}
