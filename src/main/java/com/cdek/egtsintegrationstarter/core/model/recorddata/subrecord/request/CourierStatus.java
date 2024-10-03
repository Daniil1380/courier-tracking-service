package com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request;

import lombok.Getter;

/**
 * Перечисление, представляющее статусы курьера
 */
@Getter
public enum CourierStatus {

    /**
     * Курьер свободен
     */
    FREE(0),

    /**
     * Курьер забирает заказ
     */
    PICKING_UP_ORDER(1),

    /**
     * Курьер доставляет заказ
     */
    DELIVERING_ORDER(2),

    /**
     * Курьер выдает заказ
     */
    ISSUING_ORDER(3);

    /**
     * Идентификатор статуса курьера
     */
    private final int id;


    CourierStatus(int id) {
        this.id = id;
    }

}