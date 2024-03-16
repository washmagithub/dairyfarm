package com.example.dairyfarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainModel implements Serializable {
    private String cowid;
    private List<Model> info = null;

    public MainModel() {
    }

    public String getCowid() {
        return cowid;
    }

    public void setCowid(String cowid) {
        this.cowid = cowid;
    }

    public List<Model> getInfo() {
        return info;
    }

    public void setInfo(List<Model> info) {
        this.info = info;
    }
}
