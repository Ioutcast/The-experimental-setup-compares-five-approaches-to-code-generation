# Generation summary

Approach: LLM-генерация
Requirement version: v6 Добавление внешней финансовой системы

## Coverage
- Доменная модель: 0.72
- Workflow: 0.60
- Маршруты согласования: 0.63
- Права доступа: 0.58
- SLA: 0.50
- События: 0.67
- Интеграции: 0.62
- Аудит: 0.62
- Тесты: 0.75
- Эволюция требований: 0.51

## Notes
Model notes:
Generated layered implementation plan:
approach=LLM
version=v6
requirements:
- Сущности User, Department, Request, RequestType, ApprovalRoute и ApprovalStep
- Конечный автомат DRAFT, SUBMITTED, ROUTING, IN_APPROVAL, APPROVED, REJECTED и CLOSED
- Построение маршрута по сумме заявки и обязательным ролям согласующих
- Проверка автора, руководителя подразделения, согласующего и администратора
- Фиксация изменения статуса и решений согласующих в журнале аудита
suggested layers=DTO, Domain, Repository, Database, Application service, Workflow, Event, Policy, Audit, Test, Integration