# Bruno: интеграционные тесты библиотеки

Коллекция содержит ручной сценарий и автоматический интеграционный прогон для Eureka, BookService, UserService и BorrowService.

## Ручная проверка

1. Запустите Eureka и три микросервиса.
2. В Bruno нажмите **Open Collection** и выберите папку `bruno/library-microservices`.
3. Выберите окружение **Local**.
4. Выполняйте запросы из **01 Manual** по порядку.

После создания сущностей скрипты сохраняют runtime-переменные `bookId`, `userId` и `borrowId`. Их не нужно копировать вручную.

## Автоматическая проверка в Bruno

Выберите папку **02 Automated Flow** и нажмите **Run**. Сценарий проверяет регистрацию сервисов, создаёт уникальные данные, выдаёт книгу, проверяет смену доступности, ожидает `409` при повторной выдаче, возвращает книгу и удаляет тестовые данные.

## Запуск из PowerShell

Установите Bruno CLI один раз:

```powershell
npm install -g @usebruno/cli
```

Из корня проекта выполните:

```powershell
.\bruno\library-microservices\run-automated.ps1
```

Или напрямую:

```powershell
cd .\bruno\library-microservices
bru run ".\02 Automated Flow" --env Local --bail
```

PowerShell-обёртка сначала проверяет доступность всех четырёх приложений. Bruno CLI возвращает ненулевой код при провале тестов, поэтому команду можно использовать в CI.
