package com.sss.myhwmonitorapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "sensor_tbl")
public class SensorModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    protected int id;

    @ColumnInfo(name = "xAclmtr")
    protected double xAclmtr;

    @ColumnInfo(name = "yAclmtr")
    protected double yAclmtr;

    @ColumnInfo(name = "zAclmtr")
    protected double zAclmtr;

    @ColumnInfo(name = "xGyro")
    protected double xGyro;

    @ColumnInfo(name = "yGyro")
    protected double yGyro;

    @ColumnInfo(name = "zGyro")
    protected double zGyro;

    @ColumnInfo(name = "prxmty_dis")
    protected double prxmty_dis;

    @ColumnInfo(name = "lux")
    protected double lux;

    @ColumnInfo(name = "time")
    protected String time;

    @Ignore
    public SensorModel() {

    }

    public SensorModel(double xAclmtr, double yAclmtr, double zAclmtr, double xGyro, double yGyro, double zGyro, double prxmty_dis, double lux, String time) {
        this.xAclmtr = xAclmtr;
        this.yAclmtr = yAclmtr;
        this.zAclmtr = zAclmtr;
        this.xGyro = xGyro;
        this.yGyro = yGyro;
        this.zGyro = zGyro;
        this.prxmty_dis = prxmty_dis;
        this.lux = lux;
        this.time = time;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getxAclmtr() {
        return xAclmtr;
    }

    public void setxAclmtr(double xAclmtr) {
        this.xAclmtr = xAclmtr;
    }

    public double getyAclmtr() {
        return yAclmtr;
    }

    public void setyAclmtr(double yAclmtr) {
        this.yAclmtr = yAclmtr;
    }

    public double getzAclmtr() {
        return zAclmtr;
    }

    public void setzAclmtr(double zAclmtr) {
        this.zAclmtr = zAclmtr;
    }

    public double getxGyro() {
        return xGyro;
    }

    public void setxGyro(double xGyro) {
        this.xGyro = xGyro;
    }

    public double getyGyro() {
        return yGyro;
    }

    public void setyGyro(double yGyro) {
        this.yGyro = yGyro;
    }

    public double getzGyro() {
        return zGyro;
    }

    public void setzGyro(double zGyro) {
        this.zGyro = zGyro;
    }

    public double getPrxmty_dis() {
        return prxmty_dis;
    }

    public void setPrxmty_dis(double prxmty_dis) {
        this.prxmty_dis = prxmty_dis;
    }

    public double getLux() {
        return lux;
    }

    public void setLux(double lux) {
        this.lux = lux;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
