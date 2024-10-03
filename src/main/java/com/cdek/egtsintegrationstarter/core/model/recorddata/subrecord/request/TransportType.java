package com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request;

import lombok.Getter;

/**
 * Перечисление, представляющее типы транспорта
 */
@Getter
public enum TransportType {

    /**
     * Пешком
     */
    ON_FOOT(0),

    /**
     * Личное мобильное средство (например, самокат, гироскутер)
     */
    PERSONAL_MOBILITY_VEHICLE(1),

    /**
     * Велосипед
     */
    BIKE(2),

    /**
     * Автомобиль
     */
    CAR(3);

    /**
     * Идентификатор типа транспорта
     */
    private final int id;

    TransportType(int id) {
        this.id = id;
    }
}