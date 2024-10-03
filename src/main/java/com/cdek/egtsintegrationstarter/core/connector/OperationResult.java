package com.cdek.egtsintegrationstarter.core.connector;

import com.cdek.egtsintegrationstarter.core.model.BinaryData;
import com.cdek.egtsintegrationstarter.core.model.CourierTrackingInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperationResult {

    CourierTrackingInfo initialData;

    BinaryData sentData;

    BinaryData receivedData;

    boolean isSuccessful;

}
