package com.sss.myhwmonitorapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sss.myhwmonitorapp.db.SensorModel;
import com.sss.myhwmonitorapp.repositories.DataRepo;
import com.sss.myhwmonitorapp.repositories.SensorRepo;
import com.sss.myhwmonitorapp.responses.SensorDataResponse;

import java.util.List;

public class DataViewModel extends AndroidViewModel {

    private DataRepo dataRepo;


    public DataViewModel(@NonNull Application application) {
        super(application);
        dataRepo = DataRepo.getInstance(application);
    }


    public LiveData<List<SensorModel>> getAllSensorData() {
        return dataRepo.getAllData();
    }
}
