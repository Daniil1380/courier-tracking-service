package com.cdek.egtsintegrationstarter.core.connector;

import com.cdek.egtsintegrationstarter.config.properties.EgtsConfigProperties;
import com.cdek.egtsintegrationstarter.core.model.CourierTrackingInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.net.Socket;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class EgtsFacadeImpl implements EgtsFacade {

    private final EgtsConnector egtsConnector;
    private final GenericObjectPool<Socket> tcpPool;
    private final EgtsConfigProperties egtsConfigProperties;

    @Override
    public List<OperationResult> sendData(List<CourierTrackingInfo> courierTrackingInfos, int dispatcherId, Instant now) {
        Socket socket = null;

        try {
            socket = tcpPool.borrowObject(egtsConfigProperties.getMaxWaitTime());
            log.info(socket.toString());
            return egtsConnector.sendData(socket, courierTrackingInfos, dispatcherId, now);
        } catch (Exception e) {
            log.error("ERROR WHILE SENDING", e);
            return List.of();
        }
        finally {
            tcpPool.returnObject(socket);
        }
    }
}
