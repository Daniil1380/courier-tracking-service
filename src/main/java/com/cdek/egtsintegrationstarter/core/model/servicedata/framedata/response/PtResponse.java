package com.cdek.egtsintegrationstarter.core.model.servicedata.framedata.response;

import com.cdek.egtsintegrationstarter.core.model.BinaryData;
import com.cdek.egtsintegrationstarter.core.model.servicedata.framedata.ServiceFrameData;
import com.cdek.egtsintegrationstarter.core.model.servicedata.framedata.request.ServiceDataSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Класс, представляющий ответ на пакет данных (PT-ответ) в EGTS-системе.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PtResponse implements ServiceFrameData {

    /**
     * Идентификатор пакета ответа.
     */
    private int responsePacketId;

    /**
     * Результат обработки.
     */
    private int processingResult;

    /**
     * Набор данных сервиса.
     */
    private ServiceDataSet serviceDataSet;

    /**
     * Размер данных в ответе
     */
    private static final int SIZE_OF_DATA = 3;

    @Override
    public BinaryData decode(byte[] content) {
        var inputStream = new ByteArrayInputStream(content);
        var in = new BufferedInputStream(inputStream);
        try {
            responsePacketId = ByteBuffer.wrap(in.readNBytes(2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
            processingResult = in.read();
            serviceDataSet = new ServiceDataSet();
            serviceDataSet.decode(in.readNBytes(in.available()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }


    @Override
    public byte[] encode() {
        var bytesOut = new ByteArrayOutputStream();
        try {
            bytesOut.write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short) responsePacketId).array());
            bytesOut.write(processingResult);
            var sdrBytes = serviceDataSet.encode();
            bytesOut.write(sdrBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytesOut.toByteArray();
    }

    @Override
    public int length() {
        return SIZE_OF_DATA + serviceDataSet.length();
    }

    /**
     * Проверяет, успешен ли результат обработки.
     *
     * @return {@code true}, если результат обработки успешен (processingResult == 0), иначе {@code false}.
     */
    public boolean isAckOk() {
        return processingResult == 0;
    }
}