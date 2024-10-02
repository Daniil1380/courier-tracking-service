package com.cdek.egtsintegrationstarter.util;

/**
 * Класс, предоставляющий вспомогательные методы для работы с булевыми значениями.
 */
public class BooleanUtil {

    /**
     * Преобразует булевое значение в строку "1" или "0".
     *
     * @param value Булевое значение, которое нужно преобразовать.
     * @return Строка "1", если значение true, и "0", если значение false.
     */
    public static String getStringFromBool(boolean value) {
        return value ? "1" : "0";
    }
}