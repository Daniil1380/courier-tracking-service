package com.cdek.egtsintegrationstarter.core.model.recorddata;

import com.cdek.egtsintegrationstarter.core.model.BinaryData;
import com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.SubRecordData;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Класс, представляющий данные подзаписи.
 */
@Data
public class RecordData implements BinaryData {

    /**
     * Тип подзаписи.
     */
    private RecordType subrecordType;

    /**
     * Длина подзаписи.
     */
    private short subrecordLength;

    /**
     * Данные подзаписи.
     */
    private SubRecordData subrecordData;

    /**
     * Размер заголовка подзаписи.
     */
    private static final int SIZE_OF_DATA = 3;


    public RecordData(SubRecordData subrecordData, RecordType subrecordType) {
        this.subrecordData = subrecordData;
        this.subrecordType = subrecordType;
        this.subrecordLength = calculateSubRecordLength();
    }


    @Override
    public BinaryData decode(byte[] content) {
        // Реализация в RecordDataSet
        return null;
    }

    @Override
    public byte[] encode() {
        try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream()) {
            bytesOut.write(subrecordType.getId());
            bytesOut.write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(calculateSubRecordLength()).array());
            bytesOut.write(subrecordData.encode());

            return bytesOut.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private short calculateSubRecordLength() {
        return (short) subrecordData.length();
    }

    @Override
    public int length() {
        return SIZE_OF_DATA + subrecordData.length();
    }
}