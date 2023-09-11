package com.clj.blesample.api;

import com.google.gson.annotations.SerializedName;

public class ModelMonitoring {
    @SerializedName("home_care_patient_id")
    private String home_care_patient_id;

    @SerializedName("time_acquired")
    private String time_acquired;

    @SerializedName("respiratory_rate")
    private String respiratory_rate;

    @SerializedName("oxygen_saturation")
    private String oxygen_saturation;

    @SerializedName("temperature")
    private String temperature;

    @SerializedName("systolic_blood_pressure")
    private String systolic_blood_pressure;

    @SerializedName("diastolic_blood_pressure")
    private String diastolic_blood_pressure;

    @SerializedName("heart_rate")
    private String heart_rate;

    @SerializedName("additional_oxygen")
    private String additional_oxygen;

    @SerializedName("dm_level_of_consciousness_id")
    private String dm_level_of_consciousness_id;

    @SerializedName("news_score")
    private String news_score;

    @SerializedName("risk")
    private String risk;

    public String getHome_care_patient_id() {
        return home_care_patient_id;
    }

    public void setHome_care_patient_id(String home_care_patient_id) {
        this.home_care_patient_id = home_care_patient_id;
    }

    public String getTime_acquired() {
        return time_acquired;
    }

    public void setTime_acquired(String time_acquired) {
        this.time_acquired = time_acquired;
    }

    public String getRespiratory_rate() {
        return respiratory_rate;
    }

    public void setRespiratory_rate(String respiratory_rate) {
        this.respiratory_rate = respiratory_rate;
    }

    public String getOxygen_saturation() {
        return oxygen_saturation;
    }

    public void setOxygen_saturation(String oxygen_saturation) {
        this.oxygen_saturation = oxygen_saturation;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getSystolic_blood_pressure() {
        return systolic_blood_pressure;
    }

    public void setSystolic_blood_pressure(String systolic_blood_pressure) {
        this.systolic_blood_pressure = systolic_blood_pressure;
    }

    public String getDiastolic_blood_pressure() {
        return diastolic_blood_pressure;
    }

    public void setDiastolic_blood_pressure(String diastolic_blood_pressure) {
        this.diastolic_blood_pressure = diastolic_blood_pressure;
    }

    public String getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(String heart_rate) {
        this.heart_rate = heart_rate;
    }

    public String getAdditional_oxygen() {
        return additional_oxygen;
    }

    public void setAdditional_oxygen(String additional_oxygen) {
        this.additional_oxygen = additional_oxygen;
    }

    public String getDm_level_of_consciousness_id() {
        return dm_level_of_consciousness_id;
    }

    public void setDm_level_of_consciousness_id(String dm_level_of_consciousness_id) {
        this.dm_level_of_consciousness_id = dm_level_of_consciousness_id;
    }

    public String getNews_score() {
        return news_score;
    }

    public void setNews_score(String news_score) {
        this.news_score = news_score;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }
}
