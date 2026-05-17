package ru.master.exp;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Фабрика входной спецификации эксперимента.
 *
 * Здесь задаются десять версий требований и набор бизнес-сценариев,
 * по которым сравниваются подходы генерации.
 */
final class ExperimentSpecFactory {
    private ExperimentSpecFactory() {
    }

    static ExperimentSpec create() {
        List<RequirementVersion> versions = new ArrayList<>();
        List<RequirementFeature> features = new ArrayList<>();

        features.add(feature("REQ-DOMAIN-BASE", FeatureCategory.DOMAIN, "Сущности User, Department, Request, RequestType, ApprovalRoute и ApprovalStep", EnumSet.of(Layer.DOMAIN, Layer.DTO, Layer.REPOSITORY, Layer.DATABASE), 2.0));
        features.add(feature("REQ-WORKFLOW-BASE", FeatureCategory.WORKFLOW, "Конечный автомат DRAFT, SUBMITTED, ROUTING, IN_APPROVAL, APPROVED, REJECTED и CLOSED", EnumSet.of(Layer.WORKFLOW, Layer.APPLICATION, Layer.EVENT), 3.2));
        features.add(feature("REQ-ROUTING-AMOUNT", FeatureCategory.ROUTING, "Построение маршрута по сумме заявки и обязательным ролям согласующих", EnumSet.of(Layer.WORKFLOW, Layer.POLICY, Layer.DOMAIN), 2.8));
        features.add(feature("REQ-ACCESS-BASE", FeatureCategory.ACCESS, "Проверка автора, руководителя подразделения, согласующего и администратора", EnumSet.of(Layer.POLICY, Layer.APPLICATION, Layer.AUDIT), 2.6));
        features.add(feature("REQ-AUDIT-STATUS", FeatureCategory.AUDIT, "Фиксация изменения статуса и решений согласующих в журнале аудита", EnumSet.of(Layer.AUDIT, Layer.EVENT, Layer.REPOSITORY), 1.8));
        features.add(feature("REQ-TEST-BASE", FeatureCategory.TEST, "Тесты доменных правил, workflow-переходов и запретов доступа", EnumSet.of(Layer.TEST), 2.2));
        versions.add(version("v1", "Базовый процесс согласования заявки", "Базовый workflow и роли", features));

        features.add(feature("REQ-SLA-ESCALATION", FeatureCategory.SLA, "Контроль сроков согласования, уведомления и эскалация при нарушении SLA", EnumSet.of(Layer.WORKFLOW, Layer.EVENT, Layer.INTEGRATION, Layer.TEST), 3.0));
        versions.add(version("v2", "Добавление SLA и эскалаций", "SLA и фоновые проверки", features));
        features.add(feature("REQ-SECURITY-ROUTE", FeatureCategory.ROUTING, "Добавление согласования службы безопасности для заявок с персональными данными", EnumSet.of(Layer.POLICY, Layer.WORKFLOW, Layer.AUDIT), 2.5));
        versions.add(version("v3", "Добавление службы безопасности", "Безопасность в маршруте", features));
        features.add(feature("REQ-LEGAL-ROUTE", FeatureCategory.ROUTING, "Добавление юридического согласования для договорных заявок", EnumSet.of(Layer.WORKFLOW, Layer.POLICY, Layer.DOMAIN), 2.3));
        versions.add(version("v4", "Добавление юридического согласования", "Юридический шаг маршрута", features));
        features.add(feature("REQ-DELEGATION", FeatureCategory.ACCESS, "Делегирование согласования заместителю при отсутствии основного исполнителя", EnumSet.of(Layer.POLICY, Layer.WORKFLOW, Layer.AUDIT), 3.1));
        versions.add(version("v5", "Добавление делегирования согласующих", "Делегирование и аудит", features));
        features.add(feature("REQ-FINANCE-INTEGRATION", FeatureCategory.INTEGRATION, "Передача утвержденной заявки во внешнюю финансовую систему с retry-обработкой", EnumSet.of(Layer.INTEGRATION, Layer.EVENT, Layer.APPLICATION, Layer.TEST), 3.4));
        versions.add(version("v6", "Добавление внешней финансовой системы", "Интеграционный адаптер", features));
        features.add(feature("REQ-MULTIBRANCH", FeatureCategory.DOMAIN, "Многофилиальная структура организации и маршруты по филиалам", EnumSet.of(Layer.DOMAIN, Layer.POLICY, Layer.REPOSITORY), 2.9));
        versions.add(version("v7", "Добавление многофилиальной структуры организации", "Филиалы и подразделения", features));
        features.add(feature("REQ-CONFIGURABLE-TYPES", FeatureCategory.DOMAIN, "Настраиваемые типы заявок и обязательные поля без изменения кода контроллеров", EnumSet.of(Layer.DOMAIN, Layer.APPLICATION, Layer.DATABASE, Layer.API), 3.3));
        versions.add(version("v8", "Добавление настраиваемых типов заявок", "Конфигурация типов", features));
        features.add(feature("REQ-SECURITY-AUDIT", FeatureCategory.AUDIT, "Аудит запрещенных действий и событий безопасности", EnumSet.of(Layer.AUDIT, Layer.POLICY, Layer.EVENT), 2.4));
        versions.add(version("v9", "Добавление аудита запрещенных действий", "Security audit", features));
        features.add(feature("REQ-REGENERATION", FeatureCategory.EVOLUTION, "Повторная генерация без потери ручных изменений и с контролем регрессий", EnumSet.of(Layer.TEST, Layer.APPLICATION, Layer.DATABASE), 3.6));
        versions.add(version("v10", "Добавление повторной генерации без потери ручных изменений", "Повторная генерация", features));

        List<Scenario> scenarios = new ArrayList<>();
        scenarios.add(new Scenario("SC-DOM-01", FeatureCategory.DOMAIN, "Создание заявки с обязательными полями", 4));
        scenarios.add(new Scenario("SC-DOM-02", FeatureCategory.DOMAIN, "Запрет изменения закрытой заявки", 3));
        scenarios.add(new Scenario("SC-WF-01", FeatureCategory.WORKFLOW, "Допустимый переход DRAFT -> SUBMITTED", 3));
        scenarios.add(new Scenario("SC-WF-02", FeatureCategory.WORKFLOW, "Запрет недопустимого перехода DRAFT -> APPROVED", 3));
        scenarios.add(new Scenario("SC-ROUTE-01", FeatureCategory.ROUTING, "Маршрут по сумме до 50 000", 3));
        scenarios.add(new Scenario("SC-ROUTE-02", FeatureCategory.ROUTING, "Маршрут с безопасностью и юридическим отделом", 4));
        scenarios.add(new Scenario("SC-ACCESS-01", FeatureCategory.ACCESS, "Автор видит собственные заявки", 2));
        scenarios.add(new Scenario("SC-ACCESS-02", FeatureCategory.ACCESS, "Запрет согласования собственной заявки", 3));
        scenarios.add(new Scenario("SC-SLA-01", FeatureCategory.SLA, "Эскалация просроченного шага", 4));
        scenarios.add(new Scenario("SC-EVENT-01", FeatureCategory.EVENT, "Запись события в outbox", 3));
        scenarios.add(new Scenario("SC-INT-01", FeatureCategory.INTEGRATION, "Повтор вызова внешней системы после таймаута", 4));
        scenarios.add(new Scenario("SC-AUDIT-01", FeatureCategory.AUDIT, "Аудит запрещенной попытки действия", 3));
        return new ExperimentSpec(new ArrayList<>(versions), scenarios);
    }

    private static RequirementFeature feature(String code, FeatureCategory category, String description, Set<Layer> layers, double complexity) {
        return new RequirementFeature(code, category, description, layers, complexity);
    }

    private static RequirementVersion version(String code, String title, String change, List<RequirementFeature> features) {
        return new RequirementVersion(code, title, change, new ArrayList<>(features));
    }
}
