package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    @Autowired
    private final FilmStorage filmStorage;
    @Autowired
    private final UserStorage userStorage;
    private LocalDate cinematographyBirthdayDate = LocalDate.of(1895, 12, 28);

    public Collection<Film> getAllFilms() {
        log.info("provided list of films: " + filmStorage.returnFilms());
        return filmStorage.returnFilms();
    }

    public Film updateFilm(Film updatedFilm) {
        if (updatedFilm.getId() == null) {
            log.error("provided wrong film ID");
            throw new NotFoundException("Film Id should be indicated");
        }
        if (filmStorage.returnIDs().contains(updatedFilm.getId())) {

            validateAllParameters(updatedFilm);
            log.info("updated film: {}", updatedFilm);

            return filmStorage.updateFilm(updatedFilm.getId(), updatedFilm);
        } else {
            log.error("provided wrong film ID");
            throw new NotFoundException("Indicated wrong film id");
        }
    }

    public Film getFilm(long id) {
        if (filmStorage.returnIDs().contains(id)) {
            return filmStorage.returnFilmById(id);
        } else {
            log.error("provided wrong film ID");
            throw new NotFoundException("Indicated wrong film id");
        }
    }

    public Film addFilm(Film newFilm) {
        validateAllParameters(newFilm);
        newFilm.setId(getNextId());
        log.info("added new film: {}", newFilm);
        return filmStorage.addFilm(newFilm.getId(), newFilm);
    }

    public void addLike(Long filmId, Long likedUserId) {
        if (filmStorage.returnIDs().contains(filmId)) {
            if (userStorage.returnIDs().contains(likedUserId)) {
                Film currentFilm = filmStorage.returnFilmById(filmId);
                Set<Long> currentFilmLikes = currentFilm.getLikes();
                currentFilmLikes.add(likedUserId);
                log.info("To film with id {} added like from user with id: {}", filmId, likedUserId);
            } else {
                log.error("provided wrong user ID");
                throw new NotFoundException("Indicated wrong user id number");
            }
        } else {
            log.error("provided wrong film ID");
            throw new NotFoundException("Indicated wrong film id number");
        }
    }

    public void deleteLike(Long filmId, Long dislikedUserId) {

        if (filmStorage.returnIDs().contains(filmId)) {
            if (userStorage.returnIDs().contains(dislikedUserId)) {
                Film currentFilm = filmStorage.returnFilms()
                        .stream()
                        .filter(film -> film.getId() == filmId)
                        .findFirst()
                        .get();
                currentFilm.getLikes().remove(dislikedUserId);
                log.info("To film with id {} deleted like from user with id: {}", filmId, dislikedUserId);
            } else {
                log.error("provided wrong user ID");
                throw new NotFoundException("Indicated wrong user id number");
            }
        } else {
            log.error("provided wrong film ID");
            throw new NotFoundException("Indicated wrong film id number");
        }
    }

    public Set<Film> getPopular(Optional<String> count) {
        long defaultAmountOfFilms = 10L;
        if (count.isPresent() && !count.get().isEmpty()) {
            defaultAmountOfFilms = Long.parseLong(count.get());
        }

        LinkedHashSet<Film> filmsSortedByAmountOfLikes = filmStorage.returnFilms().stream()
                .sorted(new FilmComparator())
                .limit((defaultAmountOfFilms >= filmStorage.returnFilms().size() ?
                        defaultAmountOfFilms : filmStorage.returnFilms().size()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return filmsSortedByAmountOfLikes;
    }

    private long getNextId() {
        long currentMaxId = filmStorage.returnIDs()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateAllParameters(Film film) {
        if (!validateName(film)) {
            log.error("provided wrong film name");
            throw new ValidationException("Film should have a name");
        }
        if (!validateDescriptionLength(film)) {
            log.error("provided description is too long");
            throw new ValidationException("Description length is more than 200 symbols");
        }
        if (film.getReleaseDate().isBefore(cinematographyBirthdayDate)) {
            log.error("provided wrong release date");
            throw new ValidationException("Wrong release date");
        }
        if (film.getDuration() < 0) {
            log.error("provided duration is negative");
            throw new ValidationException("Duration cannot be expressed with a negative number");
        }
    }

    private boolean validateDescriptionLength(Film film) {
        return film.getDescription().length() <= 200;
    }

    private boolean validateName(Film film) {
        return StringUtils.hasText(film.getName());
    }
}
