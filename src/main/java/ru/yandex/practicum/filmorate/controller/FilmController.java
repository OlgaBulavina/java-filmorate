package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final LocalDate CINEMATOGRAPHY_BIRTHDAY_DATE = LocalDate.of(1895, 12, 28);
    private Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection get() {
        log.info("provided list of films: " + films.values());
        return films.values();
    }

    @PostMapping
    public Film add(@RequestBody Film newFilm) {
        if (!nameValidation(newFilm)) {
            log.error("provided wrong film name");
            throw new ValidationException("Film should have a name");
        }
        if (!descriptionLengthValidation(newFilm)) {
            log.error("provided description is too long");
            throw new ValidationException("Description length is more than 200 symbols");
        }
        if (newFilm.getReleaseDate().isBefore(CINEMATOGRAPHY_BIRTHDAY_DATE)) {
            log.error("provided wrong release date");
            throw new ValidationException("Wrong release date");
        }
        if (newFilm.getDuration() < 0) {
            log.error("provided duration is negative");
            throw new ValidationException("Duration cannot be expressed with a negative number");
        }

        newFilm.setId(getNextId());

        films.put(newFilm.getId(), newFilm);
        log.info("added new film: {}", newFilm);

        return newFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film updatedFilm) {

        if (updatedFilm.getId() == null || updatedFilm.getId().toString().isBlank()) {
            log.error("provided wrong ID");
            throw new ValidationException("Id should be indicated");
        }

        if (films.containsKey(updatedFilm.getId())) {

            if (!nameValidation(updatedFilm)) {
                log.error("provided wrong film name");
                throw new ValidationException("Film should have a name");
            }
            if (!descriptionLengthValidation(updatedFilm)) {
                log.error("provided description is too long");
                throw new ValidationException("Description length is more than 200 symbols");
            }
            if (updatedFilm.getReleaseDate().isBefore(CINEMATOGRAPHY_BIRTHDAY_DATE)) {
                log.error("provided wrong release date");
                throw new ValidationException("Wrong release date");
            }
            if (updatedFilm.getDuration() < 0) {
                log.error("provided duration is negative");
                throw new ValidationException("Duration cannot be expressed with a negative number");
            }

            Film oldFilm = films.get(updatedFilm.getId());

            oldFilm.setName(updatedFilm.getName());
            oldFilm.setDescription(updatedFilm.getDescription());
            oldFilm.setDuration(updatedFilm.getDuration());
            oldFilm.setReleaseDate(updatedFilm.getReleaseDate());
            log.info("updated film: {}", oldFilm);

            return oldFilm;

        } else {
            log.error("provided wrong ID");
            throw new ValidationException("Indicated wrong id");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private boolean descriptionLengthValidation(Film film) {
        return film.getDescription().length() <= 200;
    }

    private boolean nameValidation(Film film) {
        return film.getName() != null && !film.getName().isBlank();
    }
}