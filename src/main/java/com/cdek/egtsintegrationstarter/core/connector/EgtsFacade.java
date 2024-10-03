package com.cdek.egtsintegrationstarter.core.connector;

import com.cdek.egtsintegrationstarter.core.model.CourierTrackingInfo;

import java.time.Instant;
import java.util.List;

/**
 * Главный класс-фасад
 */
public interface EgtsFacade {

    /**
     * Отправляет данные о трекинге курьеров на EGTS-сервер
     *
     * @param courierTrackingInfos Список информации о трекинге курьеров
     * @param dispatcherId         Идентификатор диспетчера
     * @param now                  Текущее время
     * @return Список информации о трекинге курьеров, отправленных на сервер и успешно принятых
     */
    List<OperationResult> sendData(List<CourierTrackingInfo> courierTrackingInfos,
                                   int dispatcherId,
                                   Instant now);

}
