# Generation summary

Approach: LLM-генерация
Requirement version: v5 Добавление делегирования согласующих

## Coverage
- Доменная модель: 0.73
- Workflow: 0.61
- Маршруты согласования: 0.64
- Права доступа: 0.59
- SLA: 0.51
- События: 0.68
- Интеграции: 0.66
- Аудит: 0.63
- Тесты: 0.76
- Эволюция требований: 0.52

## Notes
Model notes:
Generated layered implementation plan:
approach=LLM
version=v5
requirements:
- Сущности User, Department, Request, RequestType, ApprovalRoute и ApprovalStep
- Конечный автомат DRAFT, SUBMITTED, ROUTING, IN_APPROVAL, APPROVED, REJECTED и CLOSED
- Построение маршрута по сумме заявки и обязательным ролям согласующих
- Проверка автора, руководителя подразделения, согласующего и администратора
- Фиксация изменения статуса и решений согласующих в журнале аудита
suggested layers=DTO, Domain, Repository, Database, Application service, Workflow, Event, Policy, Audit, Test, Integration