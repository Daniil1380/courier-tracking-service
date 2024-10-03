package com.cdek.egtsintegrationstarter.core.request.position;

import com.cdek.egtsintegrationstarter.config.properties.EgtsConfigProperties;
import com.cdek.egtsintegrationstarter.core.model.packagedata.PackageData;
import com.cdek.egtsintegrationstarter.core.stream.receiver.PackageReceiver;
import com.cdek.egtsintegrationstarter.core.stream.sender.PackageSender;
import com.cdek.egtsintegrationstarter.exception.EgtsBadAnswerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class PositionServiceImpl implements PositionService {

    private final PackageReceiver packageReceiver;
    private final PackageSender packageSender;
    private final EgtsConfigProperties egtsConfigProperties;

    public Optional<PackageData> sendPosition(PackageData packageData, InputStream inputStream, OutputStream outputStream) {
        for (int i = 0; i < egtsConfigProperties.getMaxAttempts(); i++) {
            packageSender.sendPackage(outputStream, packageData);

            try {
                PackageData responsePosition = packageReceiver.receivePackage(inputStream);

                return Optional.ofNullable(responsePosition);
            } catch (EgtsBadAnswerException e) {
                log.error("ERROR WHILE WAITING ACK FOR {}", packageData);
            }
        }

        return Optional.empty();
    }

}
