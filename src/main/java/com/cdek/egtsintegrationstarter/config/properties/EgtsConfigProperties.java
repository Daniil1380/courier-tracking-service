package com.cdek.egtsintegrationstarter.config.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * Настройки EGTS
 */
@Getter
@Setter
public class EgtsConfigProperties {

    /**
     * Хост, куда необходимо подключаться
     */
    private String host;

    /**
     * Порт, куда необходимо подключаться
     */
    private int port;

    /**
     * Количество сокетов в пуле
     */
    private int poolSize;

    /**
     * Макс. количество простаивающих сокетов в пуле
     */
    private int maxIdle;

    /**
     * Мин. количество простаивающих сокетов в пуле
     */
    private int minIdle;

    /**
     * Макс. время ожидания коннекшна
     */
    private int maxWaitTime;

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