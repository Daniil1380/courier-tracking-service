package com.cdek.egtsintegrationstarter.core.request.position;

import com.cdek.egtsintegrationstarter.core.model.packagedata.PackageData;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Интерфейс для обработки запросов и ответов, связанных с позиционированием курьеров
 */
public interface PositionService {

    /**
     * Отправляет данные о позиции курьера и проверяет получение подтверждения
     *
     * @param packageData  Объект {@link PackageData}, содержащий данные о позиции.
     * @param inputStream  Входной поток для чтения данных.
     * @param outputStream Выходной поток для отправки данных.
     * @return {@code true}, если операция выполнена успешно, иначе {@code false}.
     */
    boolean sendPosition(PackageData packageData, InputStream inputStream, OutputStream outputStream);
}