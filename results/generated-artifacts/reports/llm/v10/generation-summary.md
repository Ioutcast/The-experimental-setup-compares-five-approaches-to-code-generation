# Generation summary

Approach: LLM-генерация
Requirement version: v10 Добавление повторной генерации без потери ручных изменений

## Coverage
- Доменная модель: 0.67
- Workflow: 0.56
- Маршруты согласования: 0.59
- Права доступа: 0.54
- SLA: 0.46
- События: 0.63
- Интеграции: 0.58
- Аудит: 0.57
- Тесты: 0.71
- Эволюция требований: 0.43

## Notes
Model notes:
Generated layered implementation plan:
approach=LLM
version=v10
requirements:
- Сущности User, Department, Request, RequestType, ApprovalRoute и ApprovalStep
- Конечный автомат DRAFT, SUBMITTED, ROUTING, IN_APPROVAL, APPROVED, REJECTED и CLOSED
- Построение маршрута по сумме заявки и обязательным ролям согласующих
- Проверка автора, руководителя подразделения, согласующего и администратора
- Фиксация изменения статуса и решений согласующих в журнале аудита
suggested layers=DTO, Domain, Repository, Database, Application service, Workflow, Event, Policy, Audit, Test, Integration, API