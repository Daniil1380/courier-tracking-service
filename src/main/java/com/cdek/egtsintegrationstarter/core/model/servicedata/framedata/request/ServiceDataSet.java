package com.cdek.egtsintegrationstarter.core.model.servicedata.framedata.request;

import com.cdek.egtsintegrationstarter.core.model.BinaryData;
import com.cdek.egtsintegrationstarter.core.model.recorddata.RecordDataSet;
import com.cdek.egtsintegrationstarter.core.model.servicedata.framedata.ServiceFrameData;
import com.cdek.egtsintegrationstarter.core.model.servicedata.innerdata.ServiceDataRecord;
import com.cdek.egtsintegrationstarter.core.model.servicedata.innerdata.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.cdek.egtsintegrationstarter.util.ConstantValues.TIMESTAMP_IN_2010;

/**
 * Класс, представляющий набор сервисов
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDataSet implements ServiceFrameData {

    /**
     * Список сервисов
     */
    private List<ServiceDataRecord> serviceDataRecords;


    @Override
    public void decode(byte[] content) {
        serviceDataRecords = new ArrayList<>();
        var inputStream = new ByteArrayInputStream(content);
        var in = new BufferedInputStream(inputStream);
        while (true) {
            try {
                if (!(in.available() > 0)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                var sdr = new ServiceDataRecord();
                var recordLength = ByteBuffer.wrap(in.readNBytes(2))
                        .order(ByteOrder.LITTLE_ENDIAN).getShort();
                sdr.setRecordLength(recordLength);

                var recordNumber = ByteBuffer.wrap(in.readNBytes(2))
                        .order(ByteOrder.LITTLE_ENDIAN).getShort();
                sdr.setRecordNumber(recordNumber);

                var flags = in.read();

                sdr.setSourceServiceOnDevice((flags & 0b10000000) == 0b10000000);
                sdr.setRecipientServiceOnDevice((flags & 0b01000000) == 0b01000000);
                sdr.setGroup((flags & 0b00100000) == 0b00100000);
                sdr.setRecordProcessingPriority((flags & 0b00011000) == 0b00011000);
                sdr.setTimeFieldExists((flags & 0b00000100) == 0b00000100);
                sdr.setEventIdFieldExists((flags & 0b00000010) == 0b00000010);
                sdr.setObjectIdFieldExists((flags & 0b00000001) == 0b00000001);

                if (sdr.isObjectIdFieldExists()) {
                    var objectIdentifier = ByteBuffer.wrap(in.readNBytes(4))
                            .order(ByteOrder.LITTLE_ENDIAN).getInt();
                    sdr.setObjectIdentifier(objectIdentifier);
                }

                if (sdr.isEventIdFieldExists()) {
                    var eventIdentifier = ByteBuffer.wrap(in.readNBytes(4))
                            .order(ByteOrder.LITTLE_ENDIAN).getInt();
                    sdr.setEventIdentifier(eventIdentifier);
                }

                if (sdr.isTimeFieldExists()) {
                    var time = ByteBuffer.wrap(in.readNBytes(4)).order(ByteOrder.LITTLE_ENDIAN).getInt();
                    Instant instant = Instant.ofEpochSecond(time + TIMESTAMP_IN_2010);
                    sdr.setTime(instant);
                }

                sdr.setSourceServiceType(ServiceType.fromId(in.readNBytes(1)[0]));
                sdr.setRecipientServiceType(ServiceType.fromId(in.readNBytes(1)[0]));

                serviceDataRecords.add(sdr);

                if (in.available() != 0) {
                    var rds = new RecordDataSet();
                    var rdsBytes = ByteBuffer.wrap(in.readNBytes(sdr.getRecordLength()))
                            .order(ByteOrder.LITTLE_ENDIAN).array();
                    rds.decode(rdsBytes);
                    sdr.setRecordDataSet(rds);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public byte[] encode() {
        var bytesOut = new ByteArrayOutputStream();
        for (BinaryData sdr : serviceDataRecords) {
            try {
                bytesOut.write(sdr.encode());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return bytesOut.toByteArray();
    }

    @Override
    public int length() {
        return serviceDataRecords.stream()
                .map(BinaryData::length)
                .reduce(Integer::sum)
                .orElse(0);
    }
}