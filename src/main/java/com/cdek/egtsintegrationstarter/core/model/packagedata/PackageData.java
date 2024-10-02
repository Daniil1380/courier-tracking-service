package com.cdek.egtsintegrationstarter.core.model.packagedata;

import com.cdek.egtsintegrationstarter.core.model.BinaryData;
import com.cdek.egtsintegrationstarter.core.model.servicedata.framedata.ServiceFrameData;
import com.cdek.egtsintegrationstarter.core.model.servicedata.framedata.request.ServiceDataSet;
import com.cdek.egtsintegrationstarter.core.model.servicedata.framedata.response.PtResponse;
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

import static com.cdek.egtsintegrationstarter.core.model.packagedata.PackageType.EGTS_PT_RESPONSE;
import static com.cdek.egtsintegrationstarter.util.BooleanUtil.getStringFromBool;
import static com.cdek.egtsintegrationstarter.util.CrcUtil.calculateCrc16;
import static com.cdek.egtsintegrationstarter.util.CrcUtil.calculateCrc8;

/**
 * Класс, представляющий данные пакета.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageData implements BinaryData {

    /**
     * Версия протокола.
     */
    private int protocolVersion;

    /**
     * Идентификатор ключа безопасности.
     */
    private int securityKeyId;

    /**
     * Флаг, указывающий, есть ли префикс.
     */
    private boolean prefix;

    /**
     * Флаг, указывающий, есть ли маршрут.
     */
    private boolean route;

    /**
     * Флаг, указывающий, используется ли алгоритм шифрования.
     */
    private boolean encryptionAlg;

    /**
     * Флаг, указывающий, используется ли сжатие.
     */
    private boolean compression;

    /**
     * Флаг, указывающий, есть ли приоритет.
     */
    private boolean priority;

    /**
     * Длина заголовка.
     */
    private int headerLength;

    /**
     * Кодировка заголовка.
     */
    private int headerEncoding;

    /**
     * Длина данных "рамка".
     */
    private short frameDataLength;

    /**
     * Идентификатор пакета.
     */
    private int packageIdentifier;

    /**
     * Тип пакета.
     */
    private PackageType packageType;

    /**
     * Адрес отправителя.
     */
    private int peerAddress;

    /**
     * Адрес получателя.
     */
    private int recipientAddress;

    /**
     * Время жизни пакета.
     */
    private int timeToLive;

    /**
     * Контрольная сумма заголовка.
     */
    private int headerCheckSum;

    /**
     * Данные "рамки" сервисов.
     */
    private ServiceFrameData servicesFrameData;

    /**
     * Контрольная сумма данных "рамки" сервисов.
     */
    private int servicesFrameDataCheckSum;

    /**
     * Версия протокола по умолчанию.
     */
    private static final byte PROTOCOL_VERSION = 1;

    /**
     * Значение, обозначающее отсутствие (ноль).
     */
    private static final byte NONE = 0;

    /**
     * Длина заголовка по умолчанию.
     */
    private static final byte HEADER_LENGTH = 11;

    public PackageData(int packageIdentifier, PackageType packageType, ServiceFrameData servicesFrameData) {
        this.protocolVersion = PROTOCOL_VERSION;
        this.securityKeyId = NONE;
        this.prefix = false;
        this.route = false;
        this.encryptionAlg = false;
        this.compression = false;
        this.priority = false;
        this.headerLength = HEADER_LENGTH;
        this.headerEncoding = NONE;
        this.packageIdentifier = packageIdentifier;
        this.packageType = packageType;
        this.servicesFrameData = servicesFrameData;
    }

    @Override
    public BinaryData decode(byte[] content) {
        var inputStream = new ByteArrayInputStream(content);
        var in = new BufferedInputStream(inputStream);
        try {
            protocolVersion = in.read();
            securityKeyId = in.read();
            decodeFlags(in.read());
            headerLength = in.read();
            headerEncoding = in.read();

            frameDataLength = ByteBuffer.wrap(in.readNBytes(2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
            packageIdentifier = ByteBuffer.wrap(in.readNBytes(2)).order(ByteOrder.LITTLE_ENDIAN).getShort();

            packageType = PackageType.fromId(in.read());

            if (route) {
                peerAddress = ByteBuffer.wrap(in.readNBytes(2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
                recipientAddress = ByteBuffer.wrap(in.readNBytes(2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
                timeToLive = in.read();
            }

            headerCheckSum = in.read();

            var dataFrameBytes = in.readNBytes(frameDataLength);
            decodeService(dataFrameBytes);

            var crcBytes = in.readNBytes(2);
            servicesFrameDataCheckSum = ByteBuffer.wrap(crcBytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    /**
     * Декодирует флаги из байта.
     *
     * @param flag Байт, содержащий флаги.
     */
    private void decodeFlags(int flag) {
        prefix = (flag & 0b11000000) == 0b11000000;
        route = (flag & 0b00100000) == 0b00100000;
        encryptionAlg = (flag & 0b00011000) == 0b00011000;
        compression = (flag & 0b00000100) == 0b00000100;
        priority = (flag & 0b00000011) == 0b00000011;
    }

    /**
     * Декодирует данные "рамки" сервиса.
     *
     * @param dataFrameBytes Массив байтов, содержащий данные кадра сервиса.
     */
    private void decodeService(byte[] dataFrameBytes) {
        switch (packageType) {
            case EGTS_PT_RESPONSE -> servicesFrameData = new PtResponse();
            case EGTS_PT_APPDATA -> servicesFrameData = new ServiceDataSet();
            default -> throw new RuntimeException("Unknown package type: " + packageType);
        }
        servicesFrameData.decode(dataFrameBytes);
    }

    @Override
    public byte[] encode() {
        var bytesOut = new ByteArrayOutputStream();
        try {
            byte[] sfrd = servicesFrameData.encode();

            short frameDataLength = (short) servicesFrameData.length();
            short packageIdentifierShort = (short) packageIdentifier;

            bytesOut.write(protocolVersion);
            bytesOut.write(securityKeyId);
            bytesOut.write(calculateFlags());
            bytesOut.write(headerLength);
            bytesOut.write(headerEncoding);
            bytesOut.write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(frameDataLength).array());
            bytesOut.write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(packageIdentifierShort).array());
            bytesOut.write(packageType.getId());
            bytesOut.write(calculateCrc8(bytesOut.toByteArray()));

            if (frameDataLength > 0) {
                bytesOut.write(sfrd);
                bytesOut.write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short) calculateCrc16(sfrd)).array());
            }

            return bytesOut.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Вычисляет флаги для данных пакета.
     *
     * @return Байт, представляющий флаги.
     */
    private byte calculateFlags() {
        var flagsBits = getStringFromBool(prefix)
                + getStringFromBool(route)
                + getStringFromBool(encryptionAlg)
                + getStringFromBool(compression)
                + getStringFromBool(priority);
        return Byte.parseByte(flagsBits);
    }


    @Override
    public int length() {
        return 0;
    }

    /**
     * Определяет, правильный ли ответ пришел и подтвердил ли он получение пакета
     *
     * @return возвращает true, если есть подтверждение
     */
    public boolean isAckAndIsOk() {
        return packageType == EGTS_PT_RESPONSE && ((PtResponse) servicesFrameData).isAckOk();
    }
}
