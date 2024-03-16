// Model.java
package com.example.dairyfarm;

import androidx.annotation.NonNull;

public class Model {

    private String cid, dname, dr, dt, dis;

    public Model() {
    }

    public Model(String cid, String dname, String dr, String dt, String dis) {
        this.cid = cid;
        this.dname = dname;
        this.dr = dr;
        this.dt = dt;
        this.dis = dis;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDr() {
        return dr;
    }

    public void setDr(String dr) {
        this.dr = dr;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    @Override
    public String toString() {
        return "Model{" +
                "cid='" + cid + '\'' +
                ", dname='" + dname + '\'' +
                ", dr='" + dr + '\'' +
                ", dt='" + dt + '\'' +
                ", dis='" + dis + '\'' +
                '}';
    }
}