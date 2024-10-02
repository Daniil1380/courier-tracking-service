package com.cdek.egtsintegrationstarter.core.model.recorddata;

import com.cdek.egtsintegrationstarter.core.model.BinaryData;
import com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.SubRecordData;
import com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.response.ResultCodeResponse;
import com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.response.SubRecordResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий набор подзаписей сервиса
 */
@Data
@AllArgsConstructor
public class RecordDataSet implements BinaryData {

    /**
     * Список подзаписей сервиса
     */
    private List<RecordData> recordDataList;


    public RecordDataSet() {
        this.recordDataList = new ArrayList<>();
    }

    @Override
    public BinaryData decode(byte[] recDS) {
        var inputStream = new ByteArrayInputStream(recDS);
        var in = new BufferedInputStream(inputStream);
        while (true) {
            try {
                if (!(in.available() > 0)) break;

                var subrecordType = RecordType.fromId(in.read());
                var subrecordLength = ByteBuffer.wrap(in.readNBytes(2))
                        .order(ByteOrder.LITTLE_ENDIAN).getShort();
                var subrecordBytes = in.readNBytes(subrecordLength);

                SubRecordData data = subrecordType == RecordType.RESULT_CODE ? new ResultCodeResponse() : new SubRecordResponse();
                data.decode(subrecordBytes);

                var rd = new RecordData(data, subrecordType);
                recordDataList.add(rd);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }

    @Override
    public byte[] encode() {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        try {
            for (BinaryData rd : recordDataList) {
                bytesOut.write(rd.encode());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytesOut.toByteArray();
    }

    @Override
    public int length() {
        return recordDataList.stream()
                .map(BinaryData::length)
                .reduce(Integer::sum)
                .orElse(0);
    }
}