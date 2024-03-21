package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private Integer id;
    @NotNull(message = "электронная почта не может быть пустой")
    @Email(message = "должна содержать символ @")
    private String  email;
    @NotNull(message = "логин не может быть пустым")
    @NotBlank(message = "логин не может быть пустым")
    @Pattern(regexp = "^\\S+$", message = "логин не может содержать пробелы")
    private String  login;
    private String name;
    @Past(message = "дата рождения не может быть в будущем")
    private LocalDate birthday;
}
