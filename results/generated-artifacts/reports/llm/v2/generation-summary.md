# Generation summary

Approach: LLM-генерация
Requirement version: v2 Добавление SLA и эскалаций

## Coverage
- Доменная модель: 0.76
- Workflow: 0.64
- Маршруты согласования: 0.66
- Права доступа: 0.63
- SLA: 0.54
- События: 0.71
- Интеграции: 0.69
- Аудит: 0.66
- Тесты: 0.79
- Эволюция требований: 0.55

## Notes
Model notes:
Generated layered implementation plan:
approach=LLM
version=v2
requirements:
- Сущности User, Department, Request, RequestType, ApprovalRoute и ApprovalStep
- Конечный автомат DRAFT, SUBMITTED, ROUTING, IN_APPROVAL, APPROVED, REJECTED и CLOSED
- Построение маршрута по сумме заявки и обязательным ролям согласующих
- Проверка автора, руководителя подразделения, согласующего и администратора
- Фиксация изменения статуса и решений согласующих в журнале аудита
suggested layers=DTO, Domain, Repository, Database, Application service, Workflow, Event, Policy, Audit, Test, Integration