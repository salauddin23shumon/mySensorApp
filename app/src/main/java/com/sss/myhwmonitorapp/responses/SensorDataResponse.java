package com.sss.myhwmonitorapp.responses;

import com.sss.myhwmonitorapp.db.SensorModel;

import java.util.List;

public class SensorDataResponse {

    private boolean isProgressLoading;
    private String toastMessage;
    private int status;
    private List<SensorModel > sensorModels;
    private SensorModel sensorData;

    public SensorDataResponse(int status, boolean isProgressLoading, String toastMessage, List<SensorModel> sensorModels) {
        this.isProgressLoading = isProgressLoading;
        this.toastMessage = toastMessage;
        this.status = status;
        this.sensorModels = sensorModels;
    }

    public SensorDataResponse(int status, boolean isProgressLoading, String toastMessage, SensorModel sensorData) {
        this.isProgressLoading = isProgressLoading;
        this.toastMessage = toastMessage;
        this.status = status;
        this.sensorData = sensorData;
    }

    public SensorDataResponse(int status, boolean isProgressLoading, String toastMessage) {
        this.isProgressLoading = isProgressLoading;
        this.toastMessage = toastMessage;
        this.status = status;
    }

    public boolean isProgressLoading() {
        return isProgressLoading;
    }

    public void setProgressLoading(boolean progressLoading) {
        isProgressLoading = progressLoading;
    }

    public String getToastMessage() {
        return toastMessage;
    }

    public void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
    }

    public List<SensorModel> getSensorModels() {
        return sensorModels;
    }

    public void setSensorModels(List<SensorModel> sensorModels) {
        this.sensorModels = sensorModels;
    }

    public SensorModel getSensorData() {
        return sensorData;
    }

    public void setSensorData(SensorModel sensorData) {
        this.sensorData = sensorData;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
