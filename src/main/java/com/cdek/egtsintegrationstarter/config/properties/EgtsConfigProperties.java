package com.cdek.egtsintegrationstarter.config.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * Настройки EGTS.
 */
@Getter
@Setter
public class EgtsConfigProperties {

    /**
     * Время ожидания ответа до падения ошибки
     */
    private int timeout;

    /**
     * Количество попыток до падения ошибки
     */
    private int maxAttempts;

    /**
     * Идентификатор поставщика
     */
    private int vendorId;
}