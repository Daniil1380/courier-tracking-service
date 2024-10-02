package com.cdek.egtsintegrationstarter.core.stream.sender;

import com.cdek.egtsintegrationstarter.core.model.packagedata.PackageData;

import java.io.OutputStream;

/**
 * Интерфейс для отправки пакетов данных в выходной поток.
 */
public interface PackageSender {

    /**
     * Отправляет пакет данных в выходной поток.
     *
     * @param outputStream Выходной поток, в который нужно отправить пакет данных.
     * @param packageData  Объект {@link PackageData}, представляющий пакет данных для отправки.
     */
    void sendPackage(OutputStream outputStream, PackageData packageData);
}