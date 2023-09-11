package com.clj.blesample.api;

import com.google.gson.annotations.SerializedName;

public class ResponseGlobal {
    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private ModelPasien data;

    @SerializedName("status")
    private boolean status;

    public void setMsg(String msg){
        this.msg = msg;
    }

    public String getMsg(){
        return msg;
    }

    public void setData(ModelPasien data){
        this.data = data;
    }

    public ModelPasien getData(){
        return data;
    }

    public void setStatus(boolean status){
        this.status = status;
    }

    public boolean isStatus(){
        return status;
    }
}
