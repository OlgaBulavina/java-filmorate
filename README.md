# java-filmorate
Template repository for Filmorate project.
## ER-Diagram



## Пояснение к схеме БД:

### users
Cодержит информацию о пользователе приложения (ключевое поле - user_id)
#### Запрос создания таблицы:
```
CREATE TABLE  IF NOT EXISTS users (
user_id INTEGER PRIMARY KEY,
login VARCHAR,
email VARCHAR,
name VARCHAR,
birthday DATE
);
```
### films
Cодержит информацию о фильме (ключевое поле - film_id)
#### Запрос создания таблицы:
```
CREATE TABLE IF NOT EXISTS films (
film_id INTEGER PRIMARY KEY,
name VARCHAR,
description VARCHAR(200),
release_date DATE,
duration INTEGER,
rating_id INTEGER
);
```
### likes
Cовокупная таблица, содержащая информацию о film_id фильм и user_id пользователей, оценивших фильм
#### Запрос создания таблицы:
```
CREATE TABLE IF NOT EXISTS likes (
film_id INTEGER,
user_id INTEGER
);
```
### friendship_requests
Cовокупная таблица, содержащая информацию о запросах дружбы и их подтверждениях в формате user_id и friend_id
#### Запрос создания таблицы:
```
CREATE TABLE IF NOT EXISTS friendship_requests (
user_id INTEGER,
friend_id INTEGER
);
```
### film_genres
Cовокупная таблица, содержащая информацию о жанрах genre_id к каждому фильму по его film_id
#### Запрос создания таблицы:
```
CREATE TABLE IF NOT EXISTS film_genres (
film_id INTEGER,
genre_id INTEGER
);
```
### film_ratings
Таблица, содержащая перечисление рейтингов фильмов (ключевое поле - rating_id)
#### Запрос создания таблицы:
```
CREATE TABLE IF NOT EXISTS film_ratings 
rating_id INTEGER PRIMARY KEY,
rating_type VARCHAR
);
```
### genres
Таблица, содержащая перечисление жанров фильмов (ключевое поле - genre_id)
#### Запрос создания таблицы:
```
CREATE TABLE IF NOT EXISTS genres (
genre_id INTEGER PRIMARY KEY,
genre_type VARCHAR
);
```


## Запросы для выполнения методов программы:


### класс FilmService

#### запросы к таблице films:

##### Метод Collection<Film> getAllFilms()
```
SELECT *
FROM films;
```
##### Метод Film getFilm(long id)
```
SELECT *
FROM films
WHERE film_id = {id};
```

##### Метод Film updateFilm(Film updatedFilm)
```
UPDATE films
SET name = '{updatedFilm.getName()}',
description = '{updatedFilm.getDescription()},'
release_date = '{updatedFilm.getReleaseDate()}',
duration = {updatedFilm.getDuration()},
rating_id = {updatedFilm.getRating()}
WHERE film_id = {updatedFilm.getId()};
```

##### Метод Film addFilm(Film newFilm)
```
INSERT INTO films (film_id, name, description, release_date, duration, rating_id)
VALUES ('{newFilm.getId()}', '{newFilm.getName()}', '{newFilm.getDescription()}', '{newFilm.getReleaseDate()}', '{newFilm.getDuration}', '{newFilm.getRating()}');
```

#### запросы к таблице likes:

##### Метод void addLike(Long filmId, Long likedUserId)
```
INSERT INTO likes (film_id, user_id)
VALUES ('{filmId}', '{likedUserId}');
```

##### Метод deleteLike(Long filmId, Long dislikedUserId)
```
DELETE FROM likes
WHERE film_id = {filmId}
AND user_id = {dislikedUserId};
```

##### Метод Set<Film> getPopular(Optional<String> count)
```
SELECT f.*
FROM films AS f
LEFT JOIN likes AS l ON f.film_id = l.film_id
GROUP BY film_id
ORDER BY COUNT(l.user_id) DESC
LIMIT {count.get()};
```

### класс UserService

#### запросы к таблице users:

##### Метод Collection<User> getAllUsers()
```
SELECT *
FROM users;
```

##### Метод User returnUserById(long id)
```
SELECT *
FROM users
WHERE user_id = {id};
```

##### Метод User addUser(User user)
```
INSERT INTO users (user_id, login, email, name, birthday)
VALUES ('{user.getId()}', '{user.getLogin()}', '{user.getEmail()}', '{user.getName()}', '{user.getBirthday()}');
```

##### Метод User updateUser(User updatedUser)
```
UPDATE users
SET login= '{updatedUser.getLogin()}',
email= '{updatedUser.getEmail()}',
name = '{updatedUser.getName()}',
birthday= '{updatedUser.getBirthday()}'
WHERE user_id = {updatedUser.getId()};
```

#### запросы к таблице friendship_requests:

##### Метод void addFriend(Long userId, Long newFriendId)
```
INSERT INTO friendship_requests
VALUES ('{userId}', '{newFriendId}');
```

##### Метод void deleteFriend(Long userId, Long formerFriendId)
```
DELETE FROM friendship_requests
WHERE user_id = {userId}
AND friend_id = {formerFriendId};
```

##### Метод Collection<User> returnFriends(Long userId)
```
SELECT friend_id
FROM friendship_requests
WHERE user_id = {userId}
AND friend_id IN (SELECT user_id FROM friendship_requests WHERE friend_id = {userId});
```

##### Метод Collection<User> mutualFriendsSet(Long userId, Long otherUserId)
```
SELECT friend_id
FROM friendship_requests
WHERE user_id = {userId}
AND friend_id IN (SELECT user_id FROM friendship_requests WHERE friend_id = {userId});
SELECT friend_id
FROM friendship_requests
WHERE user_id = {otherUserId}
AND friend_id IN (SELECT user_id FROM friendship_requests WHERE friend_id = {otherUserId});
```
