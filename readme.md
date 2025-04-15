# Car Rental Microservices
## Описание проекта
Проект представляет собой систему аренды автомобилей, 
в которой пользователи могут бронировать автомобили и оплачивать аренду. 
Архитектура построена на основе микросервисов для обеспечения масштабируемости и гибкости.

## Архитектура микросервисов
Проект состоит из следующих микросервисов:

1. **api-gateway** – Маршрутизация запросов между сервисами.

2. **user-service** – Управление пользователями (регистрация водителей и клиентов, авторизация).

3. **car-service** – Управление доступными автомобилями (добавление, удаление, обновление информации).

4. **booking-service** – Оформление аренды автомобилей (бронирование, подтверждение, отмена).

5. **payment-service** – Обработка платежей за аренду автомобилей.

6. **notification-service** – Уведомления пользователей о статусе бронирования и платежах.

## Ключевые особенности
✅ Авторизация водителей и клиентов. 
✅ Использование Kafka для обработки событий бронирования. 
✅ Оптимизация за счет общего модуля с конфигурациями, DTO и событиями.

## Используемый стек технологий
* Backend: Spring Boot (Spring Cloud, Spring Security, Spring Data, Spring Kafka)

* API Gateway: Spring Cloud Gateway

* Message Broker: Apache Kafka

* Database: PostgreSQL / MongoDB (в зависимости от сервиса)

* Authentication: JWT / OAuth 2.0
