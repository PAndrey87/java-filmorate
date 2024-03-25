package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private Integer userID = 1;
    private final Map<Integer,User> users = new HashMap<>();

    private Integer generatedID() {
        log.debug("Генерация ID: " + userID);
        return userID++;

    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        String name = user.getName();
        String login = user.getLogin();
        if (name == null || name.isEmpty()) {
            user.setName(login);
            log.debug("имя для отображения может быть пустым — в таком случае будет использован логин");
        }
        validate(user);
        user.setId(generatedID());
        users.put(user.getId(),user);
        log.info("Пользователь ID: {}, Имя: {}, добавлен.",user.getId(),user.getName());
        return user;
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Список пользователей: " + users.values());
        return users.values();
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(),user);
            log.info("Пользователь ID: " + user.getId() + ", Имя: " + user.getName() + ", обновлен.");
            return user;
        } else {
            throw new ValidationException("Такой пользователь не существует");
        }
    }

    private void validate(User user) {
        String email = user.getEmail();
        String login = user.getLogin();
        LocalDate birthday = user.getBirthday();
        if (email == null || email.isEmpty() || !email.contains("@")) {
            log.warn("электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("электронная почта пустая или не содержит символ @");
        } else if (login == null || login.isEmpty() || login.contains(" ")) {
            log.warn("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("логин пустой илт содержит пробелы");
        } else if (birthday.isAfter(LocalDate.now())) {
            log.warn("дата рождения не может быть в будущем");
            throw new ValidationException("дата рождения в будущем");
        }
    }

}
