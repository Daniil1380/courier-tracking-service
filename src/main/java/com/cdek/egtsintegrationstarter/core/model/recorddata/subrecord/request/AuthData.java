package com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request;

import com.cdek.egtsintegrationstarter.core.model.BinaryData;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Класс, представляющий данные аутентификации
 */
@Data
public class AuthData implements BinaryData {

    /**
     * Тип диспетчера
     */
    private byte dispatcherType;

    /**
     * Идентификатор диспетчера
     */
    private int dispatcherId;

    /**
     * Байт, равный нулю
     */
    private final static byte ZERO_BYTE = 0;

    /**
     * Размер данных в байтах
     */
    private final static byte SIZE_OF_DATA = 5;

    public AuthData(int dispatcherId) {
        this.dispatcherType = ZERO_BYTE;
        this.dispatcherId = dispatcherId;
    }

    @Override
    public byte[] encode() {
        try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream()) {
            bytesOut.write(dispatcherType);
            bytesOut.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(dispatcherId).array());

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