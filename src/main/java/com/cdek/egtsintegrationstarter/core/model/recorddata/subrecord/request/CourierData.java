package com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request;

import com.cdek.egtsintegrationstarter.core.model.BinaryData;
import com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.SubRecordData;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Класс, представляющий данные курьера.
 */
@Data
public class CourierData implements SubRecordData {

    /**
     * Тип данных курьера.
     */
    private CourierDataType courierDataType;

    /**
     * Значение данных.
     */
    private int value;

    /**
     * Байт, равный нулю.
     */
    private final static byte ZERO_BYTE = 0;

    /**
     * Размер данных в байтах.
     */
    private final static byte SIZE_OF_DATA = 4;

    public CourierData(CourierDataType courierDataType, int value) {
        this.courierDataType = courierDataType;
        this.value = value;
    }

    @Override
    public BinaryData decode(byte[] content) {
        return null;
    }

    @Override
    public byte[] encode() {
        try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream()) {
            byte[] bytes = ByteBuffer.allocate(4).putInt(value).array();
            bytes[0] = courierDataType.getId();
            bytesOut.write(ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).put(bytes).array());

            return bytesOut.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int length() {
        return SIZE_OF_DATA;
    }
}