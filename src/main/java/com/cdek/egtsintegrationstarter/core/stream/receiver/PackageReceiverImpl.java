package com.cdek.egtsintegrationstarter.core.stream.receiver;

import com.cdek.egtsintegrationstarter.core.model.packagedata.PackageData;
import com.cdek.egtsintegrationstarter.exception.EgtsBadAnswerException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class PackageReceiverImpl implements PackageReceiver {

    private static final int BUFFER_SIZE = 256;

    @Override
    public PackageData receivePackage(InputStream inputStream) throws EgtsBadAnswerException {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            inputStream.read(buffer);
            PackageData response = new PackageData();
            response.decode(buffer);

            log.info("RECEIVED: {}", response);

            return response;
        } catch (IOException e) {
            throw new EgtsBadAnswerException();
        }

    }

}
