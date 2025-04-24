package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmServiceTest {
    UserStorage userStorage = new InMemoryUserStorage();
    FilmService filmService = new FilmService(new InMemoryFilmStorage(), userStorage);
    UserService userService = new UserService(userStorage);

    @Test
    void getMostPopularFilms() {
        Film filmOne = new Film();
        filmOne.setName("film1");
        filmOne.setDescription("1");
        filmOne.setDuration(100L);
        filmOne.setReleaseDate(LocalDate.of(1995, 11, 18));

        filmService.addFilm(filmOne);

        Film filmTwo = new Film();
        filmTwo.setName("film2");
        filmTwo.setDescription("description2");
        filmTwo.setDuration(100L);
        filmTwo.setReleaseDate(LocalDate.of(1899, 1, 16));

        filmService.addFilm(filmTwo);

        Film filmThree = new Film();
        filmThree.setName("film3");
        filmThree.setDescription("description3");
        filmThree.setDuration(100L);
        filmThree.setReleaseDate(LocalDate.of(1999, 10, 11));

        filmService.addFilm(filmThree);

        User userOne = new User();
        userOne.setName("name1");
        userOne.setLogin("login1");
        userOne.setBirthday(LocalDate.of(1991, 10, 11));
        userOne.setEmail("email1@yandex.ru");

        User userTwo = new User();
        userTwo.setName("name2");
        userTwo.setLogin("login2");
        userTwo.setBirthday(LocalDate.of(1990, 12, 31));
        userTwo.setEmail("email2@yandex.ru");

        User userThree = new User();
        userThree.setName("name3");
        userThree.setLogin("login3");
        userThree.setBirthday(LocalDate.of(1989, 8, 10));
        userThree.setEmail("email3@yandex.ru");

        userService.addUser(userOne);
        userService.addUser(userTwo);
        userService.addUser(userThree);

        filmService.addLike(filmOne.getId(), userOne.getId());
        filmService.addLike(filmTwo.getId(), userOne.getId());
        filmService.addLike(filmThree.getId(), userOne.getId());

        filmService.addLike(filmTwo.getId(), userTwo.getId());
        filmService.addLike(filmThree.getId(), userTwo.getId());

        filmService.addLike(filmThree.getId(), userThree.getId());

        Set<Film> filmRating = filmService.getPopular(Optional.of("100"));
        Set<Film> filmRating2 = filmService.getPopular(Optional.of(""));

        System.out.println(filmRating);
        System.out.println(filmRating2);

        TreeSet<Film> mostPopularFilms = new TreeSet<>(new FilmComparator());
        mostPopularFilms.add(filmOne);
        mostPopularFilms.add(filmTwo);
        mostPopularFilms.add(filmThree);

        assertEquals(filmRating, mostPopularFilms);
        assertEquals(filmRating2, mostPopularFilms);
    }
}
