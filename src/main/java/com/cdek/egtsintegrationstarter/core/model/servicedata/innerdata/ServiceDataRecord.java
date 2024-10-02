package com.cdek.egtsintegrationstarter.core.model.servicedata.innerdata;

import com.cdek.egtsintegrationstarter.core.model.BinaryData;
import com.cdek.egtsintegrationstarter.core.model.recorddata.RecordDataSet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;

import static com.cdek.egtsintegrationstarter.util.BooleanUtil.getStringFromBool;

/**
 * Сервис - единица передачи данных в EGTS
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDataRecord implements BinaryData {

    /**
     * Длина записи.
     */
    private int recordLength;

    /**
     * Номер записи.
     */
    private int recordNumber;

    /**
     * Флаг, указывающий, находится ли получатель сервиса на устройстве-отправителе.
     */
    private boolean sourceServiceOnDevice;

    /**
     * Флаг, указывающий, находится ли получатель сервиса на устройстве-получателе.
     */
    private boolean recipientServiceOnDevice;

    /**
     * Флаг группы
     */
    private boolean group;

    /**
     * Флаг, указывающий приоритет обработки записи.
     */
    private boolean recordProcessingPriority;

    /**
     * Флаг, указывающий, существует ли поле времени.
     */
    private boolean timeFieldExists;

    /**
     * Флаг, указывающий, существует ли поле идентификатора события.
     */
    private boolean eventIdFieldExists;

    /**
     * Флаг, указывающий, существует ли поле идентификатора объекта.
     */
    private boolean objectIdFieldExists;

    /**
     * Идентификатор объекта.
     */
    private int objectIdentifier;

    /**
     * Идентификатор события.
     */
    private int eventIdentifier;

    /**
     * Время операции
     */
    private Instant time;

    /**
     * Тип сервиса на устройстве отправителя.
     */
    private ServiceType sourceServiceType;

    /**
     * Тип сервиса на устройстве получателя.
     */
    private ServiceType recipientServiceType;

    /**
     * Набор подзаписей сервиса.
     */
    private RecordDataSet recordDataSet;

    private final static Long TIMESTAMP_IN_2010 = 1262304000L;
    private final static int SIZE_OF_DATA = 7;
    private final static int SIZE_OF_ADDITIONAL_DATA = 4;


    public ServiceDataRecord(int recordNumber, ServiceType recipientServiceType, RecordDataSet recordDataSet, Instant now) {
        this.recordNumber = recordNumber;
        this.sourceServiceOnDevice = false;
        this.recipientServiceOnDevice = false;
        this.group = false;
        this.recordProcessingPriority = false;
        this.timeFieldExists = false;
        this.time = now;
        this.eventIdFieldExists = false;
        this.objectIdFieldExists = false;
        this.sourceServiceType = recipientServiceType;
        this.recipientServiceType = recipientServiceType;
        this.recordDataSet = recordDataSet;
    }

    @Override
    public BinaryData decode(byte[] content) {
        return null;
    }

    @Override
    public byte[] encode() {
        var bytesOut = new ByteArrayOutputStream();
        var rd = recordDataSet.encode();

        short recordLength = (short) rd.length;
        short recordNumberShort = (short) recordNumber;

        try {
            bytesOut.write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(recordLength).array());
            bytesOut.write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(recordNumberShort).array());

            // составной байт
            var flagBits = calculateFlags();
            bytesOut.write(flagBits);

            if (objectIdFieldExists) {
                bytesOut.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(objectIdentifier).array());
            }

            if (eventIdFieldExists) {
                bytesOut.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(eventIdentifier).array());
            }

            if (timeFieldExists) {
                bytesOut.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
                        .putInt((int) (time.getEpochSecond() - TIMESTAMP_IN_2010)).array());
            }

            bytesOut.write(sourceServiceType.getId());
            bytesOut.write(recipientServiceType.getId());

            bytesOut.write(rd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytesOut.toByteArray();
    }

    /**
     * Вычисляет флаги для записи.
     *
     * @return Байт, представляющий флаги.
     */
    private byte calculateFlags() {
        var flagsBits = getStringFromBool(sourceServiceOnDevice)
                + getStringFromBool(recipientServiceOnDevice)
                + getStringFromBool(group)
                + getStringFromBool(recordProcessingPriority)
                + getStringFromBool(timeFieldExists)
                + getStringFromBool(objectIdFieldExists);
        return Byte.parseByte(flagsBits);
    }

    @Override
    public int length() {
        int sum = 0;
        if (objectIdFieldExists) {
            sum += SIZE_OF_ADDITIONAL_DATA;
        }

        if (eventIdFieldExists) {
            sum += SIZE_OF_ADDITIONAL_DATA;
        }

        if (timeFieldExists) {
            sum += SIZE_OF_ADDITIONAL_DATA;
        }

        return SIZE_OF_DATA + sum + recordDataSet.length();
    }
}