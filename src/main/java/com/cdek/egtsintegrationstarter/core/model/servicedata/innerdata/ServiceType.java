package com.cdek.egtsintegrationstarter.core.model.servicedata.innerdata;

import lombok.Getter;

/**
 * Перечисление, представляющее типы сервисов в EGTS-системе.
 */
@Getter
public enum ServiceType {

    /**
     * Сервис аутентификации.
     */
    EGTS_AUTH_SERVICE(1),

    /**
     * Сервис телематических данных.
     */
    EGTS_TELEDATA_SERVICE(2);

    /**
     * Идентификатор типа сервиса.
     */
    private final int id;

    ServiceType(int id) {
        this.id = id;
    }

    /**
     * Возвращает тип сервиса по его идентификатору.
     *
     * @param id Идентификатор типа сервиса.
     * @return Тип сервиса, соответствующий указанному идентификатору, или null, если такого типа не существует.
     */
    public static ServiceType fromId(int id) {
        for (ServiceType value : ServiceType.values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        return null;
    }
}