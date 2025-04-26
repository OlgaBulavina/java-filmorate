package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {


    Film addFilm(Long filmId, Film newFilm);

    Film updateFilm(Long filmId, Film updatedFilm);

    void deleteFilm(Long filmId);

    Collection<Film> returnFilms();

    Collection<Long> returnIDs();

    Film returnFilmById(Long filmId);

}
