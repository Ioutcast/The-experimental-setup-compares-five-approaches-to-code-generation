# Generation summary

Approach: LLM-генерация
Requirement version: v8 Добавление настраиваемых типов заявок

## Coverage
- Доменная модель: 0.69
- Workflow: 0.58
- Маршруты согласования: 0.61
- Права доступа: 0.56
- SLA: 0.48
- События: 0.65
- Интеграции: 0.60
- Аудит: 0.60
- Тесты: 0.73
- Эволюция требований: 0.49

## Notes
Model notes:
Generated layered implementation plan:
approach=LLM
version=v8
requirements:
- Сущности User, Department, Request, RequestType, ApprovalRoute и ApprovalStep
- Конечный автомат DRAFT, SUBMITTED, ROUTING, IN_APPROVAL, APPROVED, REJECTED и CLOSED
- Построение маршрута по сумме заявки и обязательным ролям согласующих
- Проверка автора, руководителя подразделения, согласующего и администратора
- Фиксация изменения статуса и решений согласующих в журнале аудита
suggested layers=DTO, Domain, Repository, Database, Application service, Workflow, Event, Policy, Audit, Test, Integration, API