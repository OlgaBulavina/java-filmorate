package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    Film film;
    FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),
            new InMemoryUserStorage()));

    @Test
    void addCorrectFilm() {
        film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setDuration(100L);
        film.setReleaseDate(LocalDate.of(1895, 12, 28));

        filmController.add(film);
        assertEquals(filmController.get().size(), 1, "size of collection must equal 1");
    }

    @Test
    void addFilmWithIncorrectName() {
        film = new Film();
        film.setName(" ");
        film.setDescription("description");
        film.setDuration(100L);
        film.setReleaseDate(LocalDate.of(1895, 12, 31));

        assertThrows(ValidationException.class, () -> filmController.add(film),
                "method should throw an exception");
        assertEquals(filmController.get().size(), 0, "size of collection must equal 0");
    }

    @Test
    void addFilmWithIncorrectDescription() {
        film = new Film();
        film.setName("film");
        film.setDescription("description".repeat(300));
        film.setDuration(100L);
        film.setReleaseDate(LocalDate.of(1895, 12, 31));

        assertThrows(ValidationException.class, () -> filmController.add(film),
                "method should throw an exception");
        assertEquals(filmController.get().size(), 0, "size of collection must equal 0");
    }

    @Test
    void addFilmWithIncorrectReleaseDate() {
        film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setDuration(100L);
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        assertThrows(ValidationException.class, () -> filmController.add(film),
                "method should throw an exception");
        assertEquals(filmController.get().size(), 0, "size of collection must equal 0");
    }

    @Test
    void addFilmWithIncorrectDuration() {
        film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setDuration(-100L);
        film.setReleaseDate(LocalDate.of(1895, 12, 31));

        assertThrows(ValidationException.class, () -> filmController.add(film),
                "method should throw an exception");
        assertEquals(filmController.get().size(), 0, "size of collection must equal 0");
    }

    @Test
    void getAllFilms() {
        film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setDuration(100L);
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmController.add(film);

        Film filmTwo = new Film();
        filmTwo.setName("filmTwo");
        filmTwo.setDescription("descriptionTwo");
        filmTwo.setDuration(200L);
        filmTwo.setReleaseDate(LocalDate.of(1995, 11, 1));
        filmController.add(filmTwo);

        assertEquals(filmController.get().size(), 2, "size of collection must equal 2");
    }

    @Test
    void updateCorrectFilm() {
        film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setDuration(100L);
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmController.add(film);

        Film filmTwo = new Film();
        filmTwo.setId(film.getId());
        filmTwo.setName("filmTwo");
        filmTwo.setDescription("descriptionTwo");
        filmTwo.setDuration(200L);
        filmTwo.setReleaseDate(LocalDate.of(1995, 11, 1));
        filmController.update(filmTwo);

        assertEquals(film.getName(), filmTwo.getName(), "Name film should be changed to filmTwo");
    }

    @Test
    void updateFilmWithIncorrectName() {
        film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setDuration(100L);
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmController.add(film);

        Film filmTwo = new Film();
        filmTwo.setId(film.getId());
        filmTwo.setName(" ");
        filmTwo.setDescription("descriptionTwo");
        filmTwo.setDuration(200L);
        filmTwo.setReleaseDate(LocalDate.of(1995, 11, 1));

        assertThrows(ValidationException.class, () -> filmController.update(filmTwo),
                "method should throw an exception");
        assertEquals(film.getName(), "film", "data should remain unchanged");
    }

    @Test
    void updateFilmWithIncorrectDescription() {
        film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setDuration(100L);
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmController.add(film);

        Film filmTwo = new Film();
        filmTwo.setId(film.getId());
        filmTwo.setName("filmTwo");
        filmTwo.setDescription("descriptionTwo".repeat(300));
        filmTwo.setDuration(200L);
        filmTwo.setReleaseDate(LocalDate.of(1995, 11, 1));

        assertThrows(ValidationException.class, () -> filmController.update(filmTwo),
                "method should throw an exception");
        assertEquals(film.getName(), "film", "data should remain unchanged");
    }

    @Test
    void updateFilmWithIncorrectReleaseDate() {
        film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setDuration(100L);
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmController.add(film);

        Film filmTwo = new Film();
        filmTwo.setId(film.getId());
        filmTwo.setName("filmTwo");
        filmTwo.setDescription("descriptionTwo");
        filmTwo.setDuration(200L);
        filmTwo.setReleaseDate(LocalDate.of(1795, 11, 1));

        assertThrows(ValidationException.class, () -> filmController.update(filmTwo),
                "method should throw an exception");
        assertEquals(film.getName(), "film", "data should remain unchanged");
    }

    @Test
    void updateFilmWithIncorrectDuration() {
        film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setDuration(100L);
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmController.add(film);

        Film filmTwo = new Film();
        filmTwo.setId(film.getId());
        filmTwo.setName("filmTwo");
        filmTwo.setDescription("descriptionTwo");
        filmTwo.setDuration(-200L);
        filmTwo.setReleaseDate(LocalDate.of(1995, 11, 1));

        assertThrows(ValidationException.class, () -> filmController.update(filmTwo),
                "method should throw an exception");
        assertEquals(film.getName(), "film", "data should remain unchanged");
    }
}