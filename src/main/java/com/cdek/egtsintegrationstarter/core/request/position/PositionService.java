package com.cdek.egtsintegrationstarter.core.request.position;

import com.cdek.egtsintegrationstarter.core.model.packagedata.PackageData;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

/**
 * Интерфейс для обработки запросов и ответов, связанных с позиционированием курьеров
 */
public interface PositionService {

    /**
     * Отправляет данные о позиции курьера и проверяет получение подтверждения
     *
     * @param packageData  Объект {@link PackageData}, содержащий данные о позиции
     * @param inputStream  Входной поток для чтения данных
     * @param outputStream Выходной поток для отправки данных
     * @return PackageData c ответом на отправку позиции
     */
    Optional<PackageData> sendPosition(PackageData packageData, InputStream inputStream, OutputStream outputStream);
}