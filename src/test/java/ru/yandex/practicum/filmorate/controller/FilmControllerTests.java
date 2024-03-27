package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTests {
    private FilmController filmController;
    private FilmStorage filmStorage = new InMemoryFilmStorage();
    private UserStorage userStorage = new InMemoryUserStorage();
    private FilmService filmService = new FilmService(filmStorage,userStorage);

    @BeforeEach
    public void setUp() {
        filmController = new FilmController(filmService);
    }
    @Test
    public void createFilmWithoutError() {
        Film film = Film.builder()
                .name(getRandomWorld(10))
                .description(getRandomWorld(50))
                .releaseDate(LocalDate.parse("1895-12-29"))
                .duration(100)
                .build();
        filmController.create(film);
        assertEquals(1, filmController.getFilms().size());
        assertTrue(filmController.getFilms().contains(film));

    }

    @Test
    public void createFilmWitnErrorDate() {
        Film film = Film.builder()
                .name(getRandomWorld(10))
                .description(getRandomWorld(50))
                .releaseDate(LocalDate.parse("1890-12-29"))
                .duration(100)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    public void createFilmWitnErrorName() {
        Film film = Film.builder()
                .name(null)
                .description(getRandomWorld(50))
                .releaseDate(LocalDate.parse("1999-11-11"))
                .duration(100)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("название не может быть пустым", exception.getMessage());
    }

    @Test
    public void createFilmWitnErrorDescription() {
        Film film = Film.builder()
                .name(getRandomWorld(10))
                .description(getRandomWorld(201))
                .releaseDate(LocalDate.parse("1999-11-11"))
                .duration(100)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    public void createFilmWitnErrorDuration() {
        Film film = Film.builder()
                .name(getRandomWorld(10))
                .description(getRandomWorld(50))
                .releaseDate(LocalDate.parse("1999-11-11"))
                .duration(-100)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("продолжительность фильма должна быть положительной", exception.getMessage());
    }

    public String getRandomWorld(Integer length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random(characters.length());
        String result = "";
        for (int i = 0; i < length; i++) {
            result += characters.charAt(random.nextInt(characters.length()));
        }
        return result;
    }
}
