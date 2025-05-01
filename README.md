# java-filmorate
Template repository for Filmorate project.
## ER-Diagram
![DB_diagramm](https://github.com/user-attachments/assets/6a5cd202-9f1d-4930-93af-6a3c0d56aae8)


## Пояснение к БД:
### user
Cодержит информацию о пользователе приложения (ключевое поле - user_id)
### film
Cодержит информацию о фильме (ключевое поле - film_id)
### likes
Cовокупная таблица, содержащая информацию о film_id фильм и user_id пользователей, оценивших фильм
### friends
Cовокупная таблица, содержащая информацию о user_id и friend_id, а также статусе их дружбы
### film_genres
Cовокупная таблица, содержащая информацию о жанрах genre_id к каждому фильму по его film_id
### friendship_status
Таблица, содержащая перечисление статусов дружбы (ключевое поле - friendship_status_id)
### film_rating
Таблица, содержащая перечисление рейтингов фильмов (ключевое поле - rating_id)
### genre
Таблица, содержащая перечисление жанров фильмов (ключевое поле - genre_id)
