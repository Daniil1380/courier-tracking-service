package com.cdek.egtsintegrationstarter.core.request.auth;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;

/**
 * Интерфейс для выполнения аутентификации в EGTS-системе
 */
public interface AuthService {

    /**
     * Выполняет аутентификацию в EGTS-системе
     *
     * @param inputStream  Входной поток для чтения данных
     * @param outputStream Выходной поток для отправки данных
     * @param dispatcherId Идентификатор компании (CDEK или франчайзи)
     * @param now          Текущее время
     * @return {@code true}, если аутентификация выполнена успешно, иначе {@code false}
     */
    boolean authenticate(InputStream inputStream, OutputStream outputStream, int dispatcherId, Instant now);
}