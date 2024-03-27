package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private Integer userID = 1;
    private final Map<Integer,User> users = new HashMap<>();

    private Integer generatedID() {
        log.debug("Генерация ID: " + userID);
        return userID++;

    }

    @Override
    public User create(User user) {
        user.setId(generatedID());
        users.put(user.getId(),user);
        log.info("Пользователь ID: {}, Имя: {}, добавлен.",user.getId(),user.getName());
        return user;
    }

    @Override
    public Collection<User> getUsers() {
        log.info("Список пользователей: " + users.values());
        return users.values();
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(),user);
            log.info("Пользователь ID: " + user.getId() + ", Имя: " + user.getName() + ", обновлен.");
            return user;
        } else {
            throw new NotFoundException("Такой пользователь не существует");
        }
    }

    @Override
    public User getUserById(Integer id) {
        if (users.containsKey(id)) {
            User user = users.get(id);
            log.info("Пользователь ID: " + user.getId() + ", Имя: " + user.getName() + ", получен.");
            return user;
        } else {
            throw new NotFoundException("Такой пользователь не существует");
        }
    }
}
