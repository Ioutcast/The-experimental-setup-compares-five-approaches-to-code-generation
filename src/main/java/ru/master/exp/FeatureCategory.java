package ru.master.exp;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Категории требований, по которым рассчитывается покрытие.
 */
enum FeatureCategory {
    DOMAIN("Доменная модель"),
    WORKFLOW("Workflow"),
    ROUTING("Маршруты согласования"),
    ACCESS("Права доступа"),
    SLA("SLA"),
    EVENT("События"),
    INTEGRATION("Интеграции"),
    AUDIT("Аудит"),
    TEST("Тесты"),
    EVOLUTION("Эволюция требований");

    private final String title;

    FeatureCategory(String title) {
        this.title = title;
    }

    String title() {
        return title;
    }
}
