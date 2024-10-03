package com.cdek.egtsintegrationstarter.core.model.recorddata;

import lombok.Getter;

/**
 * Перечисление, представляющее типы записей данных в EGTS-системе
 */
@Getter
public enum RecordType {

    /**
     * Идентификация диспетчера
     */
    EGTS_SR_DISPATCHER_IDENTITY(5),

    /**
     * Данные о позиции
     */
    EGTS_SR_POS_DATA(16),

    /**
     * Ответ на запись
     */
    EGTS_SR_RECORD_RESPONSE(0),

    /**
     * Абсолютные данные аналоговых датчиков
     */
    EGTS_SR_ABS_AN_SENS_DATA(24),

    /**
     * Расширенные данные
     */
    EGTS_SR_EXT_DATA(44),

    /**
     * Код результата
     */
    RESULT_CODE(9),

    /**
     * Расширенные данные о позиции
     */
    EXT_POS_DATA(17);

    /**
     * Идентификатор типа записи
     */
    private final Integer id;

    RecordType(Integer id) {
        this.id = id;
    }

    /**
     * Возвращает тип записи по его идентификатору
     *
     * @param id Идентификатор типа записи
     * @return Тип записи, соответствующий указанному идентификатору, или null, если такого типа не существует
     */
    public static RecordType fromId(Integer id) {
        for (RecordType value : RecordType.values()) {
            if (value.getId().equals(id)) {
                return value;
            }
        }
        return null;
    }
}