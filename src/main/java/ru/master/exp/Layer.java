package ru.master.exp;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Архитектурные слои многослойного приложения.
 */
enum Layer {
    API("API"),
    DTO("DTO"),
    APPLICATION("Application service"),
    DOMAIN("Domain"),
    WORKFLOW("Workflow"),
    POLICY("Policy"),
    REPOSITORY("Repository"),
    INTEGRATION("Integration"),
    EVENT("Event"),
    AUDIT("Audit"),
    DATABASE("Database"),
    TEST("Test");

    private final String title;

    Layer(String title) {
        this.title = title;
    }

    String title() {
        return title;
    }
}
