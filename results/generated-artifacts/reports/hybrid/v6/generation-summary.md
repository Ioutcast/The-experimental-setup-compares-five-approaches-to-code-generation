# Generation summary

Approach: Гибридный подход
Requirement version: v6 Добавление внешней финансовой системы

## Coverage
- Доменная модель: 0.92
- Workflow: 0.88
- Маршруты согласования: 0.85
- Права доступа: 0.80
- SLA: 0.78
- События: 0.85
- Интеграции: 0.78
- Аудит: 0.85
- Тесты: 0.83
- Эволюция требований: 0.89

## Notes
Model notes:
Generated layered implementation plan:
approach=HYBRID
version=v6
requirements:
- Сущности User, Department, Request, RequestType, ApprovalRoute и ApprovalStep
- Конечный автомат DRAFT, SUBMITTED, ROUTING, IN_APPROVAL, APPROVED, REJECTED и CLOSED
- Построение маршрута по сумме заявки и обязательным ролям согласующих
- Проверка автора, руководителя подразделения, согласующего и администратора
- Фиксация изменения статуса и решений согласующих в журнале аудита
suggested layers=domain, workflow, policy, audit, tests