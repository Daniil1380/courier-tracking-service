package com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.response;

import com.cdek.egtsintegrationstarter.core.model.BinaryData;
import com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.SubRecordData;
import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Класс, представляющий ответ на подзапись сервиса.
 */
@Data
public class SubRecordResponse implements SubRecordData {

    /**
     * Номер подтверждённой записи.
     */
    private short confirmedRecordNumber;

    /**
     * Статус записи.
     */
    private byte recordStatus;

    /**
     * Размер ответа
     */
    private final static int SIZE_OF_DATA = 3;

    public SubRecordResponse() {
    }

    public SubRecordResponse(short confirmedRecordNumber, byte recordStatus) {
        this.confirmedRecordNumber = confirmedRecordNumber;
        this.recordStatus = recordStatus;
    }

    @Override
    public BinaryData decode(byte[] content) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        try {
            confirmedRecordNumber = ByteBuffer.wrap(inputStream.readNBytes(2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
            recordStatus = inputStream.readNBytes(1)[0];
        } catch (IOException exception) {
            System.out.println("SrResponse decode error " + exception.getMessage());
            return null;
        }
        return this;
    }

    @Override
    public byte[] encode() {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        try {
            bytesOut.write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(confirmedRecordNumber).array());
            bytesOut.write(recordStatus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytesOut.toByteArray();
    }

    @Override
    public int length() {
        return SIZE_OF_DATA;
    }
}