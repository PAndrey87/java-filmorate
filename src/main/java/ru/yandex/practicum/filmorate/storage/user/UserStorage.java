package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public User create(User user);

    public Collection<User> getUsers();

    public User update(User user);

    User getUserById(Integer id);
}
