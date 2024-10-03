package com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request;

import com.cdek.egtsintegrationstarter.core.model.BinaryData;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static com.cdek.egtsintegrationstarter.util.ConstantValues.ZERO_BYTE;

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
     * Размер данных в байтах
     */
    private static final byte SIZE_OF_DATA = 5;

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