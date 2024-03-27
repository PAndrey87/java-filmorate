package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        validate(user);
        return userStorage.create(user);
    }

    public Collection<User> getUsers() {
return userStorage.getUsers();
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    private void validate(User user) {
        String email = user.getEmail();
        String name = user.getName();
        String login = user.getLogin();
        LocalDate birthday = user.getBirthday();
        if (name == null || name.isEmpty()) {
            user.setName(login);
            log.debug("имя для отображения может быть пустым — в таком случае будет использован логин");
        }
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

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public User addFriend(Integer id, Integer friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        return user;
    }

    public User removeFriend(Integer id, Integer friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
        return user;
    }

    public List<User> getFriends(Integer id) {
        return getUserById(id).getFriends()
                .stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        List<User> commonFriends = new ArrayList<>();
        User user = getUserById(id);
        User otherUser = getUserById(otherId);
        for (Integer u : user.getFriends()) {
            if (otherUser.getFriends().contains(u)) {
                User commonFriend = userStorage.getUserById(u);
                commonFriends.add(commonFriend);
            }
        }
        return commonFriends;
    }
}
