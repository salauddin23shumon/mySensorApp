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

public class DataRepo {

    private static DataRepo instance;
    private static List<SensorModel> modelList;
    private SensorModel model;
    private static SensorDb sensorDb;

    private static final String TAG = "SensorRepo";

    public static DataRepo getInstance(Application application) {
        modelList = new ArrayList<>();
        sensorDb = SensorDb.getInstance(application);
        if(instance == null) {
            synchronized (DataRepo.class) {
                if(instance == null) {
                    instance = new DataRepo();
                }
            }
        }
        return instance;
    }

    public LiveData<List<SensorModel>> getAllData() {

        return sensorDb.dao().getAllDatAsc();
    }

}
