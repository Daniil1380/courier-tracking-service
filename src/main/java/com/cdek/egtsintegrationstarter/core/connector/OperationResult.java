package com.cdek.egtsintegrationstarter.core.connector;

import com.cdek.egtsintegrationstarter.core.model.BinaryData;
import com.cdek.egtsintegrationstarter.core.model.CourierTrackingInfo;
import lombok.Builder;
import lombok.Data;

/**
 * Результат работы EGTS
 */
@Data
@Builder
public class OperationResult {

    /**
     * Изначальные данные
     */
    private CourierTrackingInfo initialData;

    /**
     * Что послали
     */
    private BinaryData sentData;

    /**
     * Что получили
     */
    private BinaryData receivedData;

    /**
     * Успешно ли
     */
    private boolean isSuccessful;

}
