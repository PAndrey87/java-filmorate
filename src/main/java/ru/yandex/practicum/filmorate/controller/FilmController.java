package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    LocalDate minRelaseDate = LocalDate.parse("1895-12-28");
    Integer maxDescriptionLenngth = 200;
    Integer filmID = 1;
    Map<Integer,Film> films = new HashMap<Integer, Film>();

    private Integer generatedID() {
        log.debug("Generated filmID: " + filmID);
        return filmID++;
    }


    @PostMapping
    public Film create(@Valid @RequestBody Film film) {

        validate(film);
        film.setId(generatedID());
        films.put(film.getId(), film);
        log.info("Создан фильм: " + film);

        return film;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Получить все фильмы");
        return films.values();
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновлен фильм: " + film);
            return film;
        } else {
            throw new ValidationException("Фильм не найден");
        }
    }

    public void validate(Film film) {
        String name = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        Integer duration = film.getDuration();
        if (name == null || name.isEmpty()) {
            log.warn("название пустое");
            throw new ValidationException("название не может быть пустым");
        } else if (description.length() > maxDescriptionLenngth) {
            log.warn("описание больше 200");
            throw new ValidationException("максимальная длина описания — 200 символов");
        } else if (releaseDate.isBefore(minRelaseDate)) {
            log.warn("дата релиза —  раньше 28 декабря 1895 года");
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        } else if (duration < 0) {
            log.warn("продолжительность фильма отрицательная");
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
    }


}
