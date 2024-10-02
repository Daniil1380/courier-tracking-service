package com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request;

import lombok.Getter;

/**
 * Перечисление, представляющее типы данных курьера.
 */
@Getter
public enum CourierDataType {

    /**
     * Идентификатор курьера.
     */
    COURIER_IDENTIFIER((byte) 0xC1),

    /**
     * Текущий статус курьера.
     */
    CURRENT_STATUS((byte) 0xC5),

    /**
     * Тип транспорта.
     */
    TRANSPORT((byte) 0xC2);

    /**
     * Идентификатор типа данных курьера.
     */
    private final byte id;

    CourierDataType(byte id) {
        this.id = id;
    }

}