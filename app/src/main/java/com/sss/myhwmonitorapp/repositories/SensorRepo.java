package com.sss.myhwmonitorapp.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sss.myhwmonitorapp.db.SensorDb;
import com.sss.myhwmonitorapp.db.SensorModel;
import com.sss.myhwmonitorapp.responses.SensorDataResponse;

import java.util.ArrayList;
import java.util.List;

public class SensorRepo {

    private static SensorRepo instance;
    private static List<SensorModel> modelList;
    private SensorModel model;
    private static MutableLiveData<SensorDataResponse> responseMutableLiveData;
    private static SensorDb sensorDb;

    private static final String TAG = "SensorRepo";

    public static SensorRepo getInstance(Application application) {
        modelList = new ArrayList<>();
        responseMutableLiveData = new MutableLiveData<>();
        sensorDb = SensorDb.getInstance(application);
        if(instance == null) {
            synchronized (SensorRepo.class) {
                if(instance == null) {
                    instance = new SensorRepo();
                }
            }
        }
        return instance;
    }

    /*public SensorRepo(Application application) {
        modelList = new ArrayList<>();
        responseMutableLiveData = new MutableLiveData<>();
        sensorDb = SensorDb.getInstance(application);
    }

    public MutableLiveData<SensorDataResponse> getResponseMutableLiveData() {
        if (sensorDb.dao().ifRowExist()) {
            responseMutableLiveData.postValue(new SensorDataResponse(1, false, "data found"));
        } else {
            responseMutableLiveData.postValue(new SensorDataResponse(0, false, "data not found"));
        }
        return responseMutableLiveData;
    }*/

    public LiveData<SensorModel> getSingleLiveData() {
        return sensorDb.dao().lastEntry();
    }

    public void doSomeStuff() {
        new Thread(() -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException ignored) {
                ignored.printStackTrace();
            }finally {

//                if (sensorDb.dao().ifRowExist()) {
//                    model = sensorDb.dao().lastEntry();
//                    responseMutableLiveData.postValue(new SensorDataResponse(1, false, "data found", model));
//                } else {
//                    responseMutableLiveData.postValue(new SensorDataResponse(0, false, "data not found"));
//                }
            }


        }).start();
    }

}
