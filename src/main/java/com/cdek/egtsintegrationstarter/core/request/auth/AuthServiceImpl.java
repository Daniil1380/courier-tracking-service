package com.cdek.egtsintegrationstarter.core.request.auth;

import com.cdek.egtsintegrationstarter.core.model.packagedata.PackageData;
import com.cdek.egtsintegrationstarter.core.model.servicedata.framedata.request.ServiceDataSet;
import com.cdek.egtsintegrationstarter.core.model.servicedata.innerdata.ServiceDataRecord;
import com.cdek.egtsintegrationstarter.core.request.factory.RequestEgtsFactory;
import com.cdek.egtsintegrationstarter.core.stream.receiver.PackageReceiver;
import com.cdek.egtsintegrationstarter.core.stream.sender.PackageSender;
import com.cdek.egtsintegrationstarter.exception.EgtsBadAnswerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;

@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final PackageReceiver packageReceiver;
    private final PackageSender packageSender;

    @Override
    public boolean authenticate(InputStream inputStream, OutputStream outputStream, int dispatcherId, Instant now) {
        try {
            PackageData authRequest = RequestEgtsFactory.createAuthRequest(dispatcherId, now);
            packageSender.sendPackage(outputStream, authRequest);

            PackageData responseFirst = packageReceiver.receivePackage(inputStream);

            if (responseFirst == null || !responseFirst.isAckAndIsOk()) {
                return false;
            }

            PackageData responseSecond = packageReceiver.receivePackage(inputStream);

            if (responseSecond == null) {
                return false;
            }

            ServiceDataSet serviceDataSet = (ServiceDataSet) responseSecond.getServicesFrameData();
            ServiceDataRecord serviceDataRecord = serviceDataSet.getServiceDataRecords().get(0);

            PackageData submitRequest = RequestEgtsFactory
                    .createSubmitRequest(responseFirst.getPackageIdentifier(), serviceDataRecord.getRecordNumber(), now);
            packageSender.sendPackage(outputStream, submitRequest);

            return true;
        } catch (EgtsBadAnswerException e) {
            log.error("ERROR WHILE WAITING ACK FOR {}", packageReceiver);
            return false;
        }
    }

}
