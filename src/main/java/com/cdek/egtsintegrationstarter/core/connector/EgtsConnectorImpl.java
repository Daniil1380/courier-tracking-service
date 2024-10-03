package com.cdek.egtsintegrationstarter.core.connector;

import com.cdek.egtsintegrationstarter.config.properties.EgtsConfigProperties;
import com.cdek.egtsintegrationstarter.core.model.CourierTrackingInfo;
import com.cdek.egtsintegrationstarter.core.model.packagedata.PackageData;
import com.cdek.egtsintegrationstarter.core.request.auth.AuthService;
import com.cdek.egtsintegrationstarter.core.request.factory.RequestEgtsFactory;
import com.cdek.egtsintegrationstarter.core.request.position.PositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class EgtsConnectorImpl implements EgtsConnector {

    private final AuthService authService;
    private final PositionService positionService;
    private final EgtsConfigProperties egtsConfigProperties;

    @Override
    public List<OperationResult> sendData(
            Socket socket,
            List<CourierTrackingInfo> courierTrackingInfos,
            int dispatcherId,
            Instant now) {
        List<OperationResult> dataInfo = new ArrayList<>();

        try {
            socket.setSoTimeout(egtsConfigProperties.getTimeout());

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            boolean resultAuth = authService.authenticate(inputStream, outputStream, dispatcherId, now);
            log.debug("RESULT AUTH: {}", resultAuth);

            if (!resultAuth) {
                return new ArrayList<>();
            }

            for (int i = 0; i < courierTrackingInfos.size(); i++) {
                CourierTrackingInfo courierTrackingInfo = courierTrackingInfos.get(i);

                PackageData packageData = RequestEgtsFactory.createPositionRequest(courierTrackingInfo,
                        i + 1, egtsConfigProperties.getVendorId()); //TODO вероятно, это не параметр, а просто значение
                Optional<PackageData> optionalPackageData = positionService.sendPosition(packageData, inputStream, outputStream);

                OperationResult operationResult = OperationResult.builder()
                        .isSuccessful(optionalPackageData
                                .map(PackageData::isAckAndIsOk)
                                .orElse(false))
                        .initialData(courierTrackingInfo)
                        .sentData(packageData)
                        .receivedData(optionalPackageData
                                .orElse(null))
                        .build();

                dataInfo.add(operationResult);

                log.debug("RESULT DATA: {}", operationResult);
            }

            return dataInfo;


        } catch (IOException e) {
            return dataInfo;
        }
    }
}
