package com.cdek.egtsintegrationstarter.core.stream.sender;

import com.cdek.egtsintegrationstarter.core.model.packagedata.PackageData;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class PackageSenderImpl implements PackageSender {

    @Override
    public void sendPackage(OutputStream outputStream, PackageData packageData) {
        try {
            byte[] encodedRequest = packageData.encode();

            outputStream.write(encodedRequest);
            outputStream.flush();
            log.info("SENT : {}", packageData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
