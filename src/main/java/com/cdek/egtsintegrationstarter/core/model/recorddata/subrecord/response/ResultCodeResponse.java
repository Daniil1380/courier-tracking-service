package com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.response;

import com.cdek.egtsintegrationstarter.core.model.DecodableBinaryData;
import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Класс, представляющий ответ с кодом результата
 */
@Data
public class ResultCodeResponse implements DecodableBinaryData {

    /**
     * Код результата
     */
    private byte resultCode;

    /**
     * Размер ответа
     */
    private static final int SIZE_OF_DATA = 1;

    /**
     * Конструктор по умолчанию
     */
    public ResultCodeResponse() {
    }


    @Override
    public void decode(byte[] content) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        try {
            resultCode = ByteBuffer.wrap(inputStream.readNBytes(1)).order(ByteOrder.LITTLE_ENDIAN).get();
        } catch (IOException exception) {
            System.out.println("ResultCodeResponse decode error " + exception.getMessage());
        }
    }

    @Override
    public byte[] encode() {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        bytesOut.write(resultCode);
        return bytesOut.toByteArray();
    }

    @Override
    public int length() {
        return SIZE_OF_DATA;
    }
}