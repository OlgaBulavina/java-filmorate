package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmComparator;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Long filmId, Film newFilm) {
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film updateFilm(Long filmId, Film updatedFilm) {

        Film oldFilm = films.get(updatedFilm.getId());

        oldFilm.setName(updatedFilm.getName());
        oldFilm.setDescription(updatedFilm.getDescription());
        oldFilm.setDuration(updatedFilm.getDuration());
        oldFilm.setReleaseDate(updatedFilm.getReleaseDate());
        return oldFilm;
    }

    @Override
    public void deleteFilm(Long filmId) {
        films.remove(filmId);
    }

    @Override
    public Collection<Film> returnFilms() {
        return films.values();
    }

    @Override
    public boolean idExists(Long id) {
        return films.keySet().contains(id);
    }

    @Override
    public Film returnFilmById(Long filmId) {
        return films.get(filmId);
    }

    public Set<Film> returnTopLikedFilms(Long amount) {
        LinkedHashSet<Film> filmsSortedByAmountOfLikes = returnFilms().stream()
                .sorted(new FilmComparator())
                .limit((amount >= returnFilms().size() ? amount : returnFilms().size()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return filmsSortedByAmountOfLikes;
    }
}
