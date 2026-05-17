# Generation summary

Approach: LLM-генерация
Requirement version: v1 Базовый процесс согласования заявки

## Coverage
- Доменная модель: 0.77
- Workflow: 0.65
- Маршруты согласования: 0.67
- Права доступа: 0.64
- SLA: 0.58
- События: 0.72
- Интеграции: 0.70
- Аудит: 0.67
- Тесты: 0.80
- Эволюция требований: 0.56

## Notes
Model notes:
Generated layered implementation plan:
approach=LLM
version=v1
requirements:
- Сущности User, Department, Request, RequestType, ApprovalRoute и ApprovalStep
- Конечный автомат DRAFT, SUBMITTED, ROUTING, IN_APPROVAL, APPROVED, REJECTED и CLOSED
- Построение маршрута по сумме заявки и обязательным ролям согласующих
- Проверка автора, руководителя подразделения, согласующего и администратора
- Фиксация изменения статуса и решений согласующих в журнале аудита
suggested layers=DTO, Domain, Repository, Database, Application service, Workflow, Event, Policy, Audit, Test