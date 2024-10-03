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
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Socket;

/**
 * АвтоКонфигурация библиотеки работы с EGTS
 */
@Configuration
@ConditionalOnClass(EgtsFacade.class)
public class EgtsAutoconfiguration {

    /**
     * Настройки EGTS
     */
    @Bean
    @ConfigurationProperties("egts")
    @ConditionalOnProperty(value = {"host", "port"})
    public EgtsConfigProperties egtsProperties() {
        return new EgtsConfigProperties();
    }

    /**
     * Приемник пакетов
     */
    @Bean
    @ConditionalOnMissingBean(PackageReceiver.class)
    public PackageReceiver packageReceiver() {
        return new PackageReceiverImpl();
    }

    /**
     * Отправитель пакетов
     */
    @Bean
    @ConditionalOnMissingBean(PackageSender.class)
    public PackageSender packageSender() {
        return new PackageSenderImpl();
    }

    /**
     * Сервис позиционирования курьера
     */
    @Bean
    @ConditionalOnMissingBean(PositionService.class)
    public PositionService positionService(PackageReceiver packageReceiver,
                                           PackageSender packageSender,
                                           EgtsConfigProperties egtsConfigProperties) {
        return new PositionServiceImpl(packageReceiver, packageSender, egtsConfigProperties);
    }

    /**
     * Авторизационный сервис
     */
    @Bean
    @ConditionalOnMissingBean(AuthService.class)
    public AuthService authService(PackageReceiver packageReceiver,
                                   PackageSender packageSender) {
        return new AuthServiceImpl(packageReceiver, packageSender);
    }

    /**
     * Коннектор EGTS
     */
    @Bean
    @ConditionalOnMissingBean(EgtsConnector.class)
    public EgtsConnector egtsConnector(AuthService authService,
                                       PositionService positionService,
                                       EgtsConfigProperties egtsConfigProperties) {
        return new EgtsConnectorImpl(authService, positionService, egtsConfigProperties);
    }

    /**
     * Фабрика сокетов
     */
    @Bean
    @ConditionalOnMissingBean(PooledObjectFactory.class)
    public PooledObjectFactory<Socket> socketFactory(EgtsConfigProperties egtsConfigProperties) {
        return PoolUtils.synchronizedPooledFactory(new EgtsSocketFactory(egtsConfigProperties));

    }

    /**
     * Пул сокетов
     */
    @Bean
    @ConditionalOnMissingBean(GenericObjectPool.class)
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
    @ConditionalOnMissingBean(EgtsFacade.class)
    public EgtsFacade egtsFacade(EgtsConfigProperties egtsConfigProperties,
                                 EgtsConnector egtsConnector,
                                 GenericObjectPool<Socket> pool) {
        return new EgtsFacadeImpl(egtsConnector, pool, egtsConfigProperties);
    }

}