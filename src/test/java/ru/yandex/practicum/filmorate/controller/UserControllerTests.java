package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTests {
    private UserController userController;

    @BeforeEach
    public void setUp () {
        userController = new UserController();
    }

    @Test
    public void createUserWithoutError () {
        User user = User.builder()
                .name(getRandomWorld(10))
                .email("user@example.com")
                .birthday(LocalDate.parse("1999-11-21"))
                .login(getRandomWorld(10))
                .build();
        userController.create(user);
        assertEquals(1, userController.getUsers().size());
        assertTrue(userController.getUsers().contains(user));

    }

    @Test
    public void createUserWitnErrorEmail () {
        User user = User.builder()
                .name(getRandomWorld(10))
                .email("     ")
                .birthday(LocalDate.parse("1999-11-11"))
                .login(getRandomWorld(10))
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("электронная почта пустая или не содержит символ @", exception.getMessage());
    }

    @Test
    public void createUserWitnErrorLogin () {
        User user = User.builder()
                .name(getRandomWorld(10))
                .email("user@example.com")
                .birthday(LocalDate.parse("1999-11-11"))
                .login("  ")
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("логин пустой илт содержит пробелы", exception.getMessage());
    }

    @Test
    public void createUserWitnoutName () {
        User user = User.builder()
                .name(null)
                .email("user@example.com")
                .birthday(LocalDate.parse("1999-11-11"))
                .login(getRandomWorld(10))
                .build();

        userController.create(user);
        assertEquals(user.getLogin(), userController.getUsers().iterator().next().getName());
    }

    @Test
    public void createUserWitnErrorDate () {
        User user = User.builder()
                .name(getRandomWorld(10))
                .email("user@example.com")
                .birthday(LocalDate.parse("2222-11-11"))
                .login(getRandomWorld(10))
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("дата рождения в будущем", exception.getMessage());
    }

    public String getRandomWorld (Integer length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random(characters.length());
        String result = "";
        for (int i = 0; i < length; i++) {
            result += characters.charAt(random.nextInt(characters.length()));
        }
        return result;
    }
}
