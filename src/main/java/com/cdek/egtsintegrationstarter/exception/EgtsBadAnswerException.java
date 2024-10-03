package com.cdek.egtsintegrationstarter.exception;

/**
 * Исключение, выбрасываемое при получении некорректного ответа в EGTS
 */
public class EgtsBadAnswerException extends Exception {

    public EgtsBadAnswerException() {
        super();
    }

    public EgtsBadAnswerException(String message) {
        super(message);
    }

    public EgtsBadAnswerException(String message, Throwable cause) {
        super(message, cause);
    }

    public EgtsBadAnswerException(Throwable cause) {
        super(cause);
    }
}