package com.cdek.egtsintegrationstarter.core.model;

import com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request.CourierStatus;
import com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request.HemisphereEquatorType;
import com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request.HemisphereGreenwichType;
import com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request.TransportType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * Класс, представляющий информацию о трекинге курьера
 */
@Data
@Builder
public class CourierTrackingInfo {

    /**
     * Идентификатор курьера
     */
    private int courierId;

    /**
     * Тип транспорта
     */
    private TransportType transportType;

    /**
     * Статус курьера
     */
    private CourierStatus courierStatus;

    /**
     * Идентификатор транспорта
     */
    private String transportIdentifier;

    /**
     * Номера заказов
     */
    private List<String> orderNumbers;

    /**
     * Номера сумок курьера
     */
    private List<String> bagNumbers;

    /**
     * Время навигации
     */
    private Instant navigationTime;

    /**
     * Широта
     */
    private int latitude;

    /**
     * Долгота
     */
    private int longitude;

    /**
     * Сторона света от экватора
     */
    private HemisphereEquatorType equatorSide;

    /**
     * Тип стороны света относительно Гринвича
     */
    private HemisphereGreenwichType greenwichType;

    /**
     * Флаг, указывающий, движется ли курьер
     */
    private boolean isMoving;

    /**
     * Скорость курьера
     */
    private double speed;

    /**
     * Направление движения курьера
     */
    private int direction;

    /**
     * Пробег Т\С
     */
    private int odometer;
}