# Generation summary

Approach: Гибридный подход
Requirement version: v1 Базовый процесс согласования заявки

## Coverage
- Доменная модель: 0.93
- Workflow: 0.89
- Маршруты согласования: 0.85
- Права доступа: 0.82
- SLA: 0.82
- События: 0.86
- Интеграции: 0.82
- Аудит: 0.86
- Тесты: 0.84
- Эволюция требований: 0.90

## Notes
Model notes:
Generated layered implementation plan:
approach=HYBRID
version=v1
requirements:
- Сущности User, Department, Request, RequestType, ApprovalRoute и ApprovalStep
- Конечный автомат DRAFT, SUBMITTED, ROUTING, IN_APPROVAL, APPROVED, REJECTED и CLOSED
- Построение маршрута по сумме заявки и обязательным ролям согласующих
- Проверка автора, руководителя подразделения, согласующего и администратора
- Фиксация изменения статуса и решений согласующих в журнале аудита
suggested layers=domain, workflow, policy, audit, tests