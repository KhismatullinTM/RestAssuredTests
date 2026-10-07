# Selenoid API Tests

Набор API-тестов для проверки доступности и состояния Selenoid с использованием Java, JUnit 5 и Rest Assured.

## Технологический стек

- Java
- JUnit 5
- Rest Assured
- JSON Schema Validator
- Gradle

## Проверяемые эндпоинты

### `/ui/status`

Публичный endpoint для получения информации о состоянии сервиса.

**Проверки:**

- ответ `200 OK`;
- соответствие ответа JSON Schema.

### `/wd/hub/status`

Endpoint для получения статуса Selenoid.

**Проверки:**

- успешная авторизация;
- ответ `200 OK`;
- наличие версии Selenoid в поле `message`;
- значение `ready = true`;
- соответствие ответа JSON Schema;
- корректный `Content-Type`.

## Реализованные тесты

| Тест | Описание |
|--------|--------|
| `checkStatusCode()` | Проверка доступности `/ui/status` |
| `checkSelenoidState()` | Проверка состояния Selenoid, версии и готовности сервиса |
| `checkResponseContentType()` | Проверка заголовка `Content-Type` |
| `checkSelenoidStateUnauthorized()` | Проверка доступа без авторизации |
| `checkSelenoidStateWithInvalidCredentials()` | Проверка доступа с неверными учетными данными |
| `shouldReturn404ForUnknownEndpoint()` | Проверка ответа для несуществующего endpoint |
| `checkPostRequestToStatusEndpoint()` | Проверка обработки POST-запроса к endpoint статуса |

## Обнаруженные особенности API

Во время тестирования было выявлено, что endpoint:

```http
POST /wd/hub/status
```

возвращает ответ:

```http
HTTP/1.1 200 OK
```

с тем же содержимым, что и GET-запрос.

С точки зрения REST API для неподдерживаемого HTTP-метода обычно ожидается ответ:

```http
HTTP/1.1 405 Method Not Allowed
```

Данное поведение было зафиксировано как потенциальный дефект или особенность реализации текущей версии Selenoid.

## Запуск тестов

### Запуск всех тестов

```bash
./gradlew test
```

### Запуск конкретного теста

```bash
./gradlew test --tests "tests.SelenoidApiTests.checkSelenoidState"
```