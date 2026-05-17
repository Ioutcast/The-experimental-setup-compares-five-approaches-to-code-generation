# Generation summary

Approach: LLM-генерация
Requirement version: v9 Добавление аудита запрещенных действий

## Coverage
- Доменная модель: 0.68
- Workflow: 0.57
- Маршруты согласования: 0.60
- Права доступа: 0.55
- SLA: 0.47
- События: 0.64
- Интеграции: 0.59
- Аудит: 0.58
- Тесты: 0.72
- Эволюция требований: 0.48

## Notes
Model notes:
Generated layered implementation plan:
approach=LLM
version=v9
requirements:
- Сущности User, Department, Request, RequestType, ApprovalRoute и ApprovalStep
- Конечный автомат DRAFT, SUBMITTED, ROUTING, IN_APPROVAL, APPROVED, REJECTED и CLOSED
- Построение маршрута по сумме заявки и обязательным ролям согласующих
- Проверка автора, руководителя подразделения, согласующего и администратора
- Фиксация изменения статуса и решений согласующих в журнале аудита
suggested layers=DTO, Domain, Repository, Database, Application service, Workflow, Event, Policy, Audit, Test, Integration, API