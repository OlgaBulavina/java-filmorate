package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {


    Film addFilm(long filmId, Film newFilm);

    Film updateFilm(long filmId, Film updatedFilm);

    void deleteFilm(long filmId);

    Collection<Film> returnFilms();

    Collection<Long> returnIDs();

    Film returnFilmById(long filmId);

}
