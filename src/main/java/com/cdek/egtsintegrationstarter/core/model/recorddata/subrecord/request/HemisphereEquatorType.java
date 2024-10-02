package com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request;

import lombok.Getter;

/**
 * Перечисление, представляющее типы полушарий по экватору.
 */
@Getter
public enum HemisphereEquatorType {

    /**
     * Южное полушарие.
     */
    SOUTH("1"),

    /**
     * Северное полушарие.
     */
    NORTH("0");

    /**
     * Значение типа полушария.
     */
    private final String value;

    /**
     * Конструктор для создания типа полушария по экватору.
     *
     * @param value Значение типа полушария.
     */
    HemisphereEquatorType(String value) {
        this.value = value;
    }
}