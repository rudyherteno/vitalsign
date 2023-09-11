package com.clj.blesample.api;

import com.google.gson.annotations.SerializedName;

public class ModelPasien {
    @SerializedName("id")
    private String id;

    @SerializedName("nik")
    private String nik;

    @SerializedName("name")
    private String name;

    @SerializedName("birth_date")
    private String birth_date;

    @SerializedName("bpjs_number")
    private String bpjs_number;

    @SerializedName("phone_number")
    private String phone_number;

    @SerializedName("address")
    private String address;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("dm_gender_id")
    private String dm_gender_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getBpjs_number() {
        return bpjs_number;
    }

    public void setBpjs_number(String bpjs_number) {
        this.bpjs_number = bpjs_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDm_gender_id() {
        return dm_gender_id;
    }

    public void setDm_gender_id(String dm_gender_id) {
        this.dm_gender_id = dm_gender_id;
    }
}
