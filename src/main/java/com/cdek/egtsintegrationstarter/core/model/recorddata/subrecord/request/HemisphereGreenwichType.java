package com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request;

import lombok.Getter;

/**
 * Перечисление, представляющее типы полушарий по Гринвичу.
 */
@Getter
public enum HemisphereGreenwichType {

    /**
     * Западное полушарие.
     */
    WEST("1"),

    /**
     * Восточное полушарие.
     */
    EAST("0");

    /**
     * Значение типа полушария.
     */
    private final String value;

    HemisphereGreenwichType(String value) {
        this.value = value;
    }

}