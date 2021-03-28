package com.sss.myhwmonitorapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {SensorModel.class}, version = 1, exportSchema = false)
public abstract class SensorDb extends RoomDatabase {
    public static SensorDb sensorDb;
    public static String Database_Name = "sensor_db";

    public static synchronized SensorDb getInstance(Context context) {
        if (sensorDb == null) {
            sensorDb = Room.databaseBuilder(context.getApplicationContext(),
                    SensorDb.class, Database_Name)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return sensorDb;
    }

    public abstract SensorDao dao();      //abstract method of type SensorDao

}
