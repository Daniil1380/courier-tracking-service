package com.cdek.egtsintegrationstarter.core.connector;

import com.cdek.egtsintegrationstarter.core.model.CourierTrackingInfo;

import java.net.Socket;
import java.time.Instant;
import java.util.List;

/**
 * Интерфейс для взаимодействия с EGTS-сервером
 */
public interface EgtsConnector {

    /**
     * Отправляет данные о трекинге курьеров на EGTS-сервер
     *
     * @param socket               Сокет для соединения с сервером
     * @param courierTrackingInfos Список информации о трекинге курьеров
     * @param dispatcherId         Идентификатор диспетчера
     * @param now                  Текущее время
     * @return Список информации о трекинге курьеров, отправленных на сервер и успешно принятых
     */
    List<OperationResult> sendData(Socket socket,
                                   List<CourierTrackingInfo> courierTrackingInfos,
                                   int dispatcherId,
                                   Instant now);
}