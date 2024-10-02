package com.cdek.egtsintegrationstarter.core.stream.receiver;

import com.cdek.egtsintegrationstarter.core.model.packagedata.PackageData;
import com.cdek.egtsintegrationstarter.exception.EgtsBadAnswerException;

import java.io.InputStream;

/**
 * Интерфейс для приема пакетов данных из входного потока.
 */
public interface PackageReceiver {

    /**
     * Принимает пакет данных из входного потока.
     *
     * @param inputStream Входной поток, из которого нужно прочитать пакет данных.
     * @return Объект {@link PackageData}, представляющий принятый пакет данных.
     * @throws EgtsBadAnswerException Если получен некорректный ответ.
     */
    PackageData receivePackage(InputStream inputStream) throws EgtsBadAnswerException;
}