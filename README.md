# Онлайн магазин (Backend)
Данный проект предсьтавляет собой backend часть интренет-магазина, frontend можно увидеть по ссылке ***https://github.com/Vladislav3421730/OnlineStoreMicroservicesFrontend***
Пользователь может видеть, фильтровать товары, складывать их в корзину, формировать заказ. Менеджер может выполнять CRUD операции с товарами и заказами. Админ может банить, удалять пользователей.
Когда пользователь сохраняет товар, посылается запрос на сервис image для сохранение картинок там на MongoDb,  filename каждой картинки храниться в БД PostgreSQL, и если пользователь хочет получить картинку по filename, то запрос летит на сервис image. Также опишу работу access и refresh токенов. Когда пользователь входит в систему, ему выдаётся access и refresh токены. Если access токен истекает, то через axios interceptors на фронте летит запрос для обновления токена по refresh token. Refresh token храниться в Redis.
Далее будет описана инструкция по запуску, реквизиты для входа и используемые технологии. 
- [Используемые технологии](#используемые-технологии-в-проекте)
- [Инструкция по запуску](#инструкция-по-запуску)
- [Документация](#документация)
- [Метрики](#метрики)
- [Реквизиты для входа](#реквизиты-для-входа)
## Используемые технологии в проекте
| Раздел       | Технологии                                                             |
|--------------|------------------------------------------------------------------------|
| Backend      | Spring Boot, Spring Data JPA, Criteria, Spring Data Validation, GridFS |
| Безопасность | Spring Security, JWT (access/refresh)                                  |
| Микросервисы | RestClient, Kafka, Spring Boot Admin                                   |
| Тестирование | Spring Boot Tests, Test Containers, JUnit, Mockito                     |
| Базы данных  | PostgreSQL, MongoDB, Redis                                             |
| Метрики      | Prometheus, Grafana, Actuator                                          |
| Прочее       | ResourceBundle, MapStruct, Page, PageRequest, Swagger, Docker          |
## Инструкция по запуску
Для начала проект нужно скопировать себе локально на компьютер
```bash
git clone https://github.com/Vladislav3421730/OnlineStoreMicroservicesBackend
```
Затем перейти в корневую папку проекта 
```bash
cd OnlineStoreMicroservicesBackend
```
Потом Запустить команду Docker 
```bash
docker compose up
```
после этого можно перейти в Spring boot Admin по ***http://localhost:9000*** для того, чтобы убедиться что все сервисы запускаются корректно
Для перехода на сервис market нужно перейти по ***http://localhost:8081***, для image на ***http://localhost:8082***
## Документация 
Для перехода на Swagger-документацию можно воспользоваться следующими ссылками. Для перехода на документацию сервиса market
```bash
http://localhost:8081/swagger-ui/index.html
```
Для перехода на документацию сервиса image
```bash
http://localhost:8081/swagger-ui/index.html
```
## Метрики
Чтобы метрики стали доступны нужно из запустить через Docker, если вы находитесь в папке ***OnlineStoreMicroservicesBackend***, то вам нужно перейти в папку admin
```bash
cd admin
```
Затем запустить команду docker
```bash
docker compose up 
```
Чтобы перейти на prometheus нужно воспользоваться ***http://localhost:9090***, для перехода на Grafana I***http://localhost:9091***
Затем нужно нажать ***Add your first datasource***. После этого нажать на Prometheus, в Connection ввести
```bash
http://host.docker.internal:9090
```
Прогартните вниз и нажмите на save and Test, если появилось зелёное окошко, то выбирайте create dashboard, затем выберите import dashboard
В папке ***/admin/config/grafana*** содержиться файл с .json расширением, потом нажмите на три точки и edit и нажмите run query для обнолвения каждой панели
После этого dashboard со всеми метриками станет доступна
## Реквизиты для входа
1. Если хотите войти с ролями USER, MANAGER, ADMIN
      + Логин:***`vlad@gmail.com`***, Пароль: ***q1w2e3***
2. Если хотите войти с ролью ADMIN
      + Логин:***`admin@gmail.com`***, Пароль: ***q1w2e3***
3. Если хотите войти с ролью MANAGER
      + Логин:***`manager@gmail.com`***, Пароль: ***q1w2e3***
4. Если хотите войти с ролю USER
      + Логин:***`user@gmail.com`***, Пароль: ***q1w2e3***





