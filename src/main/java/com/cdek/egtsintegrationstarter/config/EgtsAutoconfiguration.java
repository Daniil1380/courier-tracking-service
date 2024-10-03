package com.cdek.egtsintegrationstarter.config;

import com.cdek.egtsintegrationstarter.config.properties.EgtsConfigProperties;
import com.cdek.egtsintegrationstarter.core.connector.EgtsConnector;
import com.cdek.egtsintegrationstarter.core.connector.EgtsConnectorImpl;
import com.cdek.egtsintegrationstarter.core.connector.EgtsFacade;
import com.cdek.egtsintegrationstarter.core.connector.EgtsFacadeImpl;
import com.cdek.egtsintegrationstarter.core.request.auth.AuthService;
import com.cdek.egtsintegrationstarter.core.request.auth.AuthServiceImpl;
import com.cdek.egtsintegrationstarter.core.request.position.PositionService;
import com.cdek.egtsintegrationstarter.core.request.position.PositionServiceImpl;
import com.cdek.egtsintegrationstarter.core.stream.receiver.PackageReceiver;
import com.cdek.egtsintegrationstarter.core.stream.receiver.PackageReceiverImpl;
import com.cdek.egtsintegrationstarter.core.stream.sender.PackageSender;
import com.cdek.egtsintegrationstarter.core.stream.sender.PackageSenderImpl;
import org.apache.commons.pool2.PoolUtils;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Socket;

/**
 * АвтоКонфигурация библиотеки работы с EGTS
 */
@Configuration
public class EgtsAutoconfiguration {

    /**
     * Настройки EGTS
     */
    @Bean
    @ConfigurationProperties("egts")
    public EgtsConfigProperties egtsProperties() {
        return new EgtsConfigProperties();
    }

    /**
     * Приемник пакетов
     */
    @Bean
    public PackageReceiver packageReceiver() {
        return new PackageReceiverImpl();
    }

    /**
     * Отправитель пакетов
     */
    @Bean
    public PackageSender packageSender() {
        return new PackageSenderImpl();
    }

    /**
     * Сервис позиционирования курьера
     */
    @Bean
    public PositionService positionService(PackageReceiver packageReceiver,
                                           PackageSender packageSender,
                                           EgtsConfigProperties egtsConfigProperties) {
        return new PositionServiceImpl(packageReceiver, packageSender, egtsConfigProperties);
    }

    /**
     * Авторизационный сервис
     */
    @Bean
    public AuthService authService(PackageReceiver packageReceiver,
                                   PackageSender packageSender) {
        return new AuthServiceImpl(packageReceiver, packageSender);
    }

    /**
     * Коннектор EGTS
     */
    @Bean
    public EgtsConnector egtsConnector(AuthService authService,
                                       PositionService positionService,
                                       EgtsConfigProperties egtsConfigProperties) {
        return new EgtsConnectorImpl(authService, positionService, egtsConfigProperties);
    }

    /**
     * Фабрика сокетов
     */
    @Bean
    public PooledObjectFactory<Socket> socketFactory(EgtsConfigProperties egtsConfigProperties) {
        return PoolUtils.synchronizedPooledFactory(new EgtsSocketFactory(egtsConfigProperties));

    }

    /**
     * Пул сокетов
     */
    @Bean
    public GenericObjectPool<Socket> tcpPool(EgtsConfigProperties egtsConfigProperties,
                                             PooledObjectFactory<Socket> socketPooledObjectFactory) {
        GenericObjectPoolConfig<Socket> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(egtsConfigProperties.getPoolSize());
        poolConfig.setMaxIdle(egtsConfigProperties.getMaxIdle());
        poolConfig.setMinIdle(egtsConfigProperties.getMinIdle());
        poolConfig.setTestOnBorrow(true);
        poolConfig.setJmxEnabled(false);
        return new GenericObjectPool<>(socketPooledObjectFactory, poolConfig);
    }

    /**
     * Фасад для работы с библиотекой
     */
    @Bean
    public EgtsFacade egtsFacade(EgtsConfigProperties egtsConfigProperties,
                                 EgtsConnector egtsConnector,
                                 GenericObjectPool<Socket> pool) {
        return new EgtsFacadeImpl(egtsConnector, pool, egtsConfigProperties);
    }

}