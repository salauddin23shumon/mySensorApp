package com.sss.myhwmonitorapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface SensorDao {

    @Insert(onConflict = REPLACE)
    void insert(SensorModel data);

    @Delete
    void delete(SensorModel data);

    @Delete
    void reset(List<SensorModel> dataList);

//    @Query("UPDATE sensor_tbl SET xAclmtr = :nData WHERE id = :nId ")
//    void update(int nId, double nData);

    @Query("SELECT * FROM sensor_tbl ")
    LiveData<List<SensorModel>> getAllData();

    @Query("SELECT * FROM sensor_tbl ORDER BY id ASC")
    LiveData<List<SensorModel>> getAllDatAsc();

    @Query("SELECT EXISTS (SELECT * FROM sensor_tbl )")
    boolean ifRowExist();

//    @Query("SELECT * FROM sensor_tbl ORDER BY time DESC LIMIT 1")
//    SensorModel lastEntry();

    @Query("SELECT * FROM sensor_tbl WHERE id IN ( SELECT max( id ) FROM sensor_tbl )")
    LiveData<SensorModel> lastEntry();
}
