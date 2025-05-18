package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {


    Film addFilm(Long filmId, Film newFilm);

    Film updateFilm(Long filmId, Film updatedFilm);

    void deleteFilm(Long filmId);

    Collection<Film> returnFilms();

    boolean idExists(Long id);

    Film returnFilmById(Long filmId);

    Set<Film> returnTopLikedFilms(Long amount);
}
