package com.cdek.egtsintegrationstarter.util;

/**
 * Класс для вычисления контрольных сумм CRC8 и CRC16
 */
public class CrcUtil {

    /**
     * Вычисляет 8-битную контрольную сумму (CRC8) для массива байтов
     *
     * @param data Массив байтов, для которого нужно вычислить CRC8
     * @return 8-битная контрольная сумма (CRC8)
     */
    public static int calculateCrc8(byte[] data) {
        byte crc = (byte) 0xFF;
        for (byte b : data) {
            crc ^= b;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x80) != 0) {
                    crc = (byte) ((crc << 1) ^ 0x31);
                } else {
                    crc = (byte) (crc << 1);
                }
            }
        }
        return crc;
    }

    /**
     * Вычисляет 16-битную контрольную сумму (CRC16) для массива байтов
     *
     * @param data Массив байтов, для которого нужно вычислить CRC16
     * @return 16-битная контрольная сумма (CRC16)
     */
    public static int calculateCrc16(byte[] data) {
        int crc = 0xFFFF;
        int polynomial = 0x1021; // 0001 0000 0010 0001  (0, 5, 12)
        for (byte b : data) {
            for (int i = 0; i < 8; i++) {
                var bit = ((b >> (7 - i) & 1) == 1);
                var c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
        }
        return (crc & 0xffff);
    }
}