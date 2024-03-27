package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
@Jacksonized
public class Film {
    private Integer id;
    @NotNull(message = "Должно быть название")
    @NotBlank(message = "Название не должно быть пустое")
    private String name;
    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "продолжительность фильма должна быть положительной")
    private Integer duration;
    @Builder.Default
    private Set<Integer> likes = new HashSet<>();

    public void addLike(Integer id) {
        likes.add(id);
    }

    public void removeLike(Integer id) {
        likes.remove(id);
    }
}
