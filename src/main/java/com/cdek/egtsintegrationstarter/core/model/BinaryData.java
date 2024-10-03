package com.cdek.egtsintegrationstarter.core.model;

/**
 * Интерфейс для работы с бинарными данными, включая их кодирование
 */
public interface BinaryData {

    /**
     * Кодирует данные в бинарный формат
     *
     * @return Массив байтов, представляющий закодированные данные
     */
    byte[] encode();

    /**
     * Возвращает длину бинарных данных
     *
     * @return Длина бинарных данных
     */
    int length();
}