package com.cdek.egtsintegrationstarter.core.model.packagedata;

import lombok.Getter;

/**
 * Перечисление, представляющее типы пакетов в EGTS-системе
 */
@Getter
public enum PackageType {

    /**
     * Пакет ответа
     */
    EGTS_PT_RESPONSE(0),

    /**
     * Пакет данных
     */
    EGTS_PT_APPDATA(1);

    /**
     * Идентификатор типа пакета
     */
    private final int id;

    PackageType(int id) {
        this.id = id;
    }

    /**
     * Возвращает тип пакета по его идентификатору
     *
     * @param id Идентификатор типа пакета
     * @return Тип пакета, соответствующий указанному идентификатору, или null, если такого типа не существует
     */
    public static PackageType fromId(int id) {
        for (PackageType value : PackageType.values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown package type: " + id);
    }
}