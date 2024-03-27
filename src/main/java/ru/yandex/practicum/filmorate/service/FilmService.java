package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private final LocalDate minRelaseDate = LocalDate.parse("1895-12-28");
    private final Integer maxDescriptionLenngth = 200;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        validate(film);
        return filmStorage.create(film);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    private void validate(Film film) {
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

    public Film getFilmByID(Integer id) {
        return filmStorage.getFilmByID(id);
    }

    public Film addLike(Integer id, Integer userId) {
        Film film = filmStorage.getFilmByID(id);
        User user = userStorage.getUserById(userId);
        film.addLike(userId);
        update(film);
        return film;
    }

    public Film deleteLike(Integer id, Integer userId) {
        Film film = filmStorage.getFilmByID(id);
        User user = userStorage.getUserById(userId);
        film.removeLike(userId);
        return film;
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getFilms()
                .stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
