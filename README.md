# java-explore-with-me

Pull-request: [https://github.com/Azark1n/java-explore-with-me/pull/5](https://github.com/Azark1n/java-explore-with-me/pull/5)

Postman-tests for feature Comments: [https://github.com/Azark1n/java-explore-with-me/blob/feature_comments/postman/feature.json](https://github.com/Azark1n/java-explore-with-me/blob/feature_comments/postman/feature.json)

Swagger-spec for feature Comments and Main-service: [https://github.com/Azark1n/java-explore-with-me/blob/feature_comments/swagger/ewm-comments-feature-spec.json](https://github.com/Azark1n/java-explore-with-me/blob/feature_comments/swagger/ewm-comments-feature-spec.json)

### Feature: Comments

* Добавлять комментарии к событиям могут зарегистрированные пользователи
* Комментарии события доступны списком с пагинацией по публичному эндпоинту
* Редактирование комментария возможно его автором, при следующих условиях:
  * в течение 10 минут с момента создания
  * не добавлен следующий комментарий
  * на комментарий не пожаловались
* При редактировании сохраняется информация о дате последнего изменения
* Удаление
  * доступно только администратору
  * возможно удаление всех комментариев пользователя для определенного события
  * при удалении происходит пометка об удалении и блокировка доступа
  * у администратора есть возможность просмотра комментариев вместе с удаленными
* Пользователь может быть забанен администратором, в таком случае добавление комментариев будет недоступно
* Жалобы
  * на комментарий могут пожаловаться другие пользователи
  * признак наличия жалобы возвращается при получении списка комментариев
  * администраторам жалобы попадают в список на обработку (по всем событиям или по конкретному)
  * администратор может отменить жалобу или удалить комментарий
