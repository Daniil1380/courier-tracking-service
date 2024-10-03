package com.cdek.egtsintegrationstarter.core.model;

/**
 * Интерфейс для работы с бинарными данными, которые можно декодировать
 */
public interface DecodableBinaryData extends BinaryData {

    /**
     * Декодирует бинарные данные из массива байтов прямо в вызываемый объект
     *
     * @param content Массив байтов, содержащий данные для декодирования
     */
    void decode(byte[] content);

}
