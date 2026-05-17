package ru.master.exp;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Подходы к генерации кода, сравниваемые в эксперименте.
 */
enum Approach {
    MANUAL_BASELINE("Ручной эталон"),
    TEMPLATE("Шаблонная генерация"),
    DSL("DSL/model-driven"),
    LLM("LLM-генерация"),
    HYBRID("Гибридный подход");

    private final String title;

    Approach(String title) {
        this.title = title;
    }

    String title() {
        return title;
    }
}
