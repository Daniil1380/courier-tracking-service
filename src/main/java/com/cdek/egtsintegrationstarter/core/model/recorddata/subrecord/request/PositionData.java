package com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request;

import com.cdek.egtsintegrationstarter.core.model.BinaryData;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;

import static com.cdek.egtsintegrationstarter.util.BooleanUtil.getStringFromBool;
import static com.cdek.egtsintegrationstarter.util.ConstantValues.TIMESTAMP_IN_2010;
import static com.cdek.egtsintegrationstarter.util.ConstantValues.ZERO_BYTE;

/**
 * Класс, представляющий данные о позиции курьера
 */
@Data
public class PositionData implements BinaryData {

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
     * Флаг, указывающий, будет ли учитываться высота как 3ая координата
     */
    private boolean altitudeExists;

    /**
     * Тип полушария по экватору
     */
    private HemisphereEquatorType hemisphereEquatorType;

    /**
     * Тип полушария по Гринвичу
     */
    private HemisphereGreenwichType hemisphereGreenwichType;

    /**
     * Флаг, указывающий, движется ли курьер
     */
    private boolean isMoving;

    /**
     * Флаг, указывающий, получены ли данные из памяти
     */
    private boolean fromMemory;

    /**
     * Флаг, указывающий, используются ли 3D-координаты
     */
    private boolean is3dFix;

    /**
     * Флаг, указывающий, используются ли координаты государственной системы
     */
    private boolean isGovernmentCoordinate;

    /**
     * Флаг, указывающий, являются ли данные валидными
     */
    private boolean isValidData;

    /**
     * Скорость
     */
    private double speed;

    /**
     * Направление
     */
    private int direction;

    /**
     * Пробег
     */
    private int odometer;

    /**
     * Цифровые входы
     */
    private byte digitalInputs;

    /**
     * Источник данных
     */
    private byte source;

    /**
     * Множитель скорости
     */
    private static final int SPEED_MULTIPLIER = 10;

    /**
     * Максимальное значение байта
     */
    private static final int MAX_BYTE = 0xFF;

    /**
     * Максимальное значение широты
     */
    private static final int MAX_LATITUDE = 90;

    /**
     * Максимальное значение долготы
     */
    private static final int MAX_LONGITUDE = 180;

    /**
     * Максимальное значение беззнакового целого числа
     */
    private static final int MAX_VALUE_UNSIGNED_INT = 0xFFFFFFFF;

    /**
     * Высший бит в байте
     */
    private static final int HIGH_BIT_IN_BYTE = 0x8000;

    /**
     * Размер данных в байтах
     */
    private static final int SIZE_OF_DATA = 21;

    public PositionData(Instant navigationTime,
                        int latitude,
                        int longitude,
                        HemisphereEquatorType hemisphereEquatorType,
                        HemisphereGreenwichType hemisphereGreenwichType,
                        boolean isMoving,
                        double speed,
                        int direction,
                        int odometer) {
        this.navigationTime = navigationTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitudeExists = false;
        this.hemisphereEquatorType = hemisphereEquatorType;
        this.hemisphereGreenwichType = hemisphereGreenwichType;
        this.isMoving = isMoving;
        this.fromMemory = false;
        this.is3dFix = false;
        this.isGovernmentCoordinate = false;
        this.isValidData = true;
        this.speed = speed;
        this.direction = direction;
        this.odometer = odometer;
        this.digitalInputs = ZERO_BYTE;
        this.source = ZERO_BYTE;
    }

    @Override
    public byte[] encode() {
        try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream()) {
            int latitudeNormalized = normalizeCoordinate(latitude, MAX_LATITUDE);
            int longitudeNormalized = normalizeCoordinate(longitude, MAX_LONGITUDE);

            bytesOut.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(calculateTime()).array());
            bytesOut.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(latitudeNormalized).array());
            bytesOut.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(longitudeNormalized).array());
            bytesOut.write(calculateFlags());
            bytesOut.write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(calculateSpeed()).array());
            bytesOut.write((byte) direction);

            bytesOut.write(ZERO_BYTE); // TODO: Пока по нулям пробег, возможно, потом добавим что-то
            bytesOut.write(ZERO_BYTE);
            bytesOut.write(ZERO_BYTE);

            bytesOut.write(digitalInputs);
            bytesOut.write(source);

            return bytesOut.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Нормализует координату
     *
     * @param coordinate Значение координаты
     * @param maxValue   Максимальное значение координаты
     * @return Нормализованное значение координаты
     */
    private int normalizeCoordinate(int coordinate, int maxValue) {
        int unsignedMultiplier = Integer.divideUnsigned(MAX_VALUE_UNSIGNED_INT, maxValue);
        return coordinate * unsignedMultiplier;
    }

    /**
     * Вычисляет флаги для данных о позиции
     *
     * @return Значение флагов
     */
    private int calculateFlags() {
        var flagBits =
                getStringFromBool(altitudeExists)
                        + hemisphereEquatorType.getValue()
                        + hemisphereGreenwichType.getValue()
                        + getStringFromBool(isMoving)
                        + getStringFromBool(fromMemory)
                        + getStringFromBool(is3dFix)
                        + getStringFromBool(isGovernmentCoordinate)
                        + getStringFromBool(isValidData);

        return Integer.parseInt(flagBits, 2);
    }

    /**
     * Вычисляет скорость курьера в формате EGTS
     *
     * @return Значение скорости
     */
    private short calculateSpeed() {
        int speedInt = (int) (speed * SPEED_MULTIPLIER);

        if (direction > MAX_BYTE) {
            speedInt = speedInt + HIGH_BIT_IN_BYTE;
        }

        return (short) speedInt;
    }

    /**
     * Вычисляет время с 2010 для EGTS формата
     *
     * @return Значение времени
     */
    private int calculateTime() {
        return (int) (navigationTime.getEpochSecond() - TIMESTAMP_IN_2010);
    }

    @Override
    public int length() {
        return SIZE_OF_DATA;
    }
}