package com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request;

import com.cdek.egtsintegrationstarter.core.model.BinaryData;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * Класс, представляющий расширенные данные курьера
 */
@Data
public class ExtendedCourierData implements BinaryData {

    /**
     * Идентификатор вендора
     */
    private int vendorIdentifier;

    /**
     * Тип данных
     */
    private int dataType;

    /**
     * Строковое значение данных
     */
    private String valueString;

    /**
     * Байт, равный нулю
     */
    private final static byte ZERO_BYTE = 0;

    /**
     * Размер данных в байтах (без учета строкового значения)
     */
    private final static byte SIZE_OF_DATA = 4;

    public ExtendedCourierData(int vendorIdentifier,
                               String valueString) {
        this.vendorIdentifier = vendorIdentifier;
        this.dataType = ZERO_BYTE;
        this.valueString = valueString;
    }

    @Override
    public byte[] encode() {
        try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream()) {
            bytesOut.write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short) vendorIdentifier).array());
            bytesOut.write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short) dataType).array());

            byte[] data = valueString.getBytes(StandardCharsets.UTF_8);
            bytesOut.write(data);

            return bytesOut.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int length() {
        return SIZE_OF_DATA + valueString.getBytes(StandardCharsets.UTF_8).length;
    }
}