package com.cdek.egtsintegrationstarter.core.request.factory;

import com.cdek.egtsintegrationstarter.core.model.CourierTrackingInfo;
import com.cdek.egtsintegrationstarter.core.model.packagedata.PackageData;
import com.cdek.egtsintegrationstarter.core.model.packagedata.PackageType;
import com.cdek.egtsintegrationstarter.core.model.recorddata.RecordData;
import com.cdek.egtsintegrationstarter.core.model.recorddata.RecordDataSet;
import com.cdek.egtsintegrationstarter.core.model.recorddata.RecordType;
import com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request.AuthData;
import com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request.CourierData;
import com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request.CourierDataType;
import com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request.ExtendedCourierData;
import com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.request.PositionData;
import com.cdek.egtsintegrationstarter.core.model.recorddata.subrecord.response.SubRecordResponse;
import com.cdek.egtsintegrationstarter.core.model.servicedata.framedata.request.ServiceDataSet;
import com.cdek.egtsintegrationstarter.core.model.servicedata.framedata.response.PtResponse;
import com.cdek.egtsintegrationstarter.core.model.servicedata.innerdata.ServiceDataRecord;
import com.cdek.egtsintegrationstarter.core.model.servicedata.innerdata.ServiceType;

import java.time.Instant;
import java.util.List;

import static com.cdek.egtsintegrationstarter.util.ConstantValues.ZERO_BYTE;

/**
 * Фабрика для создания различных типов запросов EGTS
 */
public class RequestEgtsFactory {


    /**
     * Единица для отправки в виде байта
     */
    private static final byte ONE_CODE = 1;

    /**
     * Форматер для строки, в которую подставляются значения курьера для отправки
     */
    private static final String FORMAT_STRING = "kisartid=%d;vplate=%s;bagplates=%s;orders=%s";

    /**
     * Создает пакет данных для аутентификационного запроса
     *
     * @param dispatcherId Идентификатор диспетчера
     * @param now          Текущее время
     * @return Пакет данных для аутентификационного запроса
     */
    public static PackageData createAuthRequest(int dispatcherId, Instant now) {
        AuthData authData = new AuthData(dispatcherId);
        RecordData recordData = new RecordData(authData, RecordType.EGTS_SR_DISPATCHER_IDENTITY);
        RecordDataSet recordDataSet = new RecordDataSet(List.of(recordData));

        ServiceDataRecord serviceDataRecord = new ServiceDataRecord(ZERO_BYTE, ServiceType.EGTS_AUTH_SERVICE, recordDataSet, now);
        ServiceDataSet serviceDataSet = new ServiceDataSet(List.of(serviceDataRecord));

        return new PackageData(ZERO_BYTE, PackageType.EGTS_PT_APPDATA, serviceDataSet);
    }

    /**
     * Создает пакет данных для подтверждения отправки запроса
     *
     * @param submittedPackageId   Идентификатор отправленного пакета
     * @param submittedSubRecordId Идентификатор отправленной записи
     * @param now                  Текущее время
     * @return Пакет данных для подтверждения отправки запроса
     */
    public static PackageData createSubmitRequest(int submittedPackageId, int submittedSubRecordId, Instant now) {
        SubRecordResponse subRecordResponse = new SubRecordResponse((short) submittedSubRecordId, ZERO_BYTE);
        RecordData recordData = new RecordData(subRecordResponse, RecordType.EGTS_SR_RECORD_RESPONSE);
        RecordDataSet recordDataSet = new RecordDataSet(List.of(recordData));
        ServiceDataRecord serviceDataRecord = new ServiceDataRecord(ZERO_BYTE, ServiceType.EGTS_AUTH_SERVICE, recordDataSet, now);
        ServiceDataSet serviceDataSet = new ServiceDataSet(List.of(serviceDataRecord));
        PtResponse ptResponse = new PtResponse(submittedPackageId, ZERO_BYTE, serviceDataSet);
        return new PackageData(ONE_CODE, PackageType.EGTS_PT_RESPONSE, ptResponse);
    }

    /**
     * Создает пакет данных для отправки позиции курьера и доп. данных
     *
     * @param courierTrackingInfo Информация о трекинге курьера
     * @param packageId           Идентификатор пакета
     * @param vendorIdentifier    Идентификатор вендора
     * @return Пакет данных для запроса позиции курьера
     */
    public static PackageData createPositionRequest(CourierTrackingInfo courierTrackingInfo, int packageId, int vendorIdentifier) {
        PositionData positionData = new PositionData(
                courierTrackingInfo.getNavigationTime(),
                courierTrackingInfo.getLatitude(),
                courierTrackingInfo.getLongitude(),
                courierTrackingInfo.getEquatorSide(),
                courierTrackingInfo.getGreenwichType(),
                courierTrackingInfo.isMoving(),
                courierTrackingInfo.getSpeed(),
                courierTrackingInfo.getDirection(),
                courierTrackingInfo.getOdometer());

        CourierData courierData = new CourierData(
                CourierDataType.COURIER_IDENTIFIER, courierTrackingInfo.getCourierId());
        CourierData transportData = new CourierData(
                CourierDataType.TRANSPORT, courierTrackingInfo.getTransportType().getId());
        CourierData statusData = new CourierData(
                CourierDataType.CURRENT_STATUS, courierTrackingInfo.getCourierStatus().getId());

        String valuesString = formatDataToString(
                courierTrackingInfo.getCourierId(),
                courierTrackingInfo.getTransportIdentifier(),
                courierTrackingInfo.getOrderNumbers(),
                courierTrackingInfo.getBagNumbers());
        ExtendedCourierData extendedCourierData = new ExtendedCourierData(vendorIdentifier, valuesString);

        RecordData recordDataPosition = new RecordData(positionData, RecordType.EGTS_SR_POS_DATA);
        RecordData recordDataCourier = new RecordData(courierData, RecordType.EGTS_SR_ABS_AN_SENS_DATA);
        RecordData recordDataTransport = new RecordData(transportData, RecordType.EGTS_SR_ABS_AN_SENS_DATA);
        RecordData recordDataStatus = new RecordData(statusData, RecordType.EGTS_SR_ABS_AN_SENS_DATA);
        RecordData recordDataExternalData = new RecordData(extendedCourierData, RecordType.EGTS_SR_EXT_DATA);

        List<RecordData> recordDataSet = List.of(recordDataPosition, recordDataCourier, recordDataTransport,
                recordDataStatus, recordDataExternalData);

        RecordDataSet recordDataSetPosition = new RecordDataSet(recordDataSet);
        ServiceDataRecord serviceDataRecordPosition = new ServiceDataRecord(ONE_CODE, ServiceType.EGTS_TELEDATA_SERVICE,
                recordDataSetPosition, courierTrackingInfo.getNavigationTime());
        ServiceDataSet serviceDataSetPosition = new ServiceDataSet(List.of(serviceDataRecordPosition));

        return new PackageData(packageId, PackageType.EGTS_PT_APPDATA, serviceDataSetPosition);
    }

    /**
     * Форматирует данные о курьере в строку
     *
     * @param courierIdentifier   Идентификатор курьера
     * @param transportIdentifier Идентификатор транспорта
     * @param orderNumbers        Номера заказов
     * @param bagNumbers          Номера мешков
     * @return Отформатированная строка данных
     */
    private static String formatDataToString(int courierIdentifier,
                                             String transportIdentifier,
                                             List<String> orderNumbers,
                                             List<String> bagNumbers) {
        String basNumbersSeparated = String.join(",", bagNumbers);
        String orderNumbersSeparated = String.join(",", orderNumbers);

        return String.format(FORMAT_STRING, courierIdentifier, transportIdentifier,
                basNumbersSeparated, orderNumbersSeparated);
    }
}