# Generation summary

Approach: LLM-генерация
Requirement version: v7 Добавление многофилиальной структуры организации

## Coverage
- Доменная модель: 0.71
- Workflow: 0.59
- Маршруты согласования: 0.62
- Права доступа: 0.57
- SLA: 0.49
- События: 0.66
- Интеграции: 0.61
- Аудит: 0.61
- Тесты: 0.74
- Эволюция требований: 0.50

## Notes
Model notes:
Generated layered implementation plan:
approach=LLM
version=v7
requirements:
- Сущности User, Department, Request, RequestType, ApprovalRoute и ApprovalStep
- Конечный автомат DRAFT, SUBMITTED, ROUTING, IN_APPROVAL, APPROVED, REJECTED и CLOSED
- Построение маршрута по сумме заявки и обязательным ролям согласующих
- Проверка автора, руководителя подразделения, согласующего и администратора
- Фиксация изменения статуса и решений согласующих в журнале аудита
suggested layers=DTO, Domain, Repository, Database, Application service, Workflow, Event, Policy, Audit, Test, Integration