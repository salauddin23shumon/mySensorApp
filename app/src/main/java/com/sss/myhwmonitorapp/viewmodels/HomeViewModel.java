package com.sss.myhwmonitorapp.viewmodels;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sss.myhwmonitorapp.db.SensorModel;
import com.sss.myhwmonitorapp.repositories.SensorRepo;
import com.sss.myhwmonitorapp.responses.SensorDataResponse;

public class HomeViewModel extends AndroidViewModel {

    private SensorRepo sensorRepo;
    private MutableLiveData<SensorDataResponse> responseMutableLiveData;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        sensorRepo =  SensorRepo.getInstance(application);
//        sensorRepo.doSomeStuff();
    }

//    public MutableLiveData<SensorDataResponse> getSensorData() {
//        return sensorRepo.getResponseMutableLiveData();
//    }


    public LiveData<SensorModel> getSingleSensorData() {
        return sensorRepo.getSingleLiveData();
    }
}
