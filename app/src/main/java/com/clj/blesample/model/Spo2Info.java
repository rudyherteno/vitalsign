package com.clj.blesample.model;

import android.util.Log;

import com.clj.blesample.lib.TransUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Spo2Info {
    public int DBP;
    public int SBP;
    public int SPo2;
    public long begindate;
    public int breathPer;
    public int cvrr;
    public long enddate;
    public int hour;
    public int hrv;
    public int isBreathPerUpload;
    public int isHrvUpload;
    public int isTempUpload;
    public int isUpload;
    public long stime;
    public String stimeFormat;
    public int tempDouble;
    public int tempInteger;
    public String timeStr;

    public void fameDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        this.stimeFormat = simpleDateFormat.format(new Date(this.stime));
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
        simpleDateFormat2.setTimeZone(TimeZone.getDefault());
        this.timeStr = simpleDateFormat2.format(new Date(this.stime));
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("HH");
        simpleDateFormat3.setTimeZone(TimeZone.getDefault());
        this.hour = Integer.parseInt(simpleDateFormat3.format(new Date(this.stime)));
    }

    public void initWithData(byte[] bArr) {
        if (bArr != null && bArr.length >= 15) {
            Log.d("iiiii", bArr.length + "");
            long Bytes2Dec = ((long) TransUtils.Bytes2Dec(new byte[]{bArr[3], bArr[2], bArr[1], bArr[0]})) + 946684800;
            this.stime = Bytes2Dec;
            long j = Bytes2Dec * 1000;
            this.stime = j;
            this.stime = j - TransUtils.getTimeOffset();
            fameDate();
            this.tempDouble = bArr[14] & -1;
            if (this.tempDouble==15)
            this.SPo2 = bArr[9] & -1;

            if (this.SPo2==0 && (this.tempDouble==15))
            this.breathPer = bArr[10] & -1;
            this.cvrr = bArr[12] & -1;
            this.hrv = bArr[11] & -1;
            this.tempInteger = bArr[13];

            Log.d("uuuuu", this.SPo2 + "--" + this.breathPer + "--" + this.cvrr + "--" + this.hrv + "--" + this.tempInteger + "--" + this.tempDouble);
        }
    }

    private String parseData(byte[] bArr) {
        byte[] bArr2 = {bArr[3], bArr[2], bArr[1], bArr[0]};
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            i += (bArr2[i2] & -1) << ((3 - i2) * 8);
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date((i + 946684800) * 1000));
    }


    public int getSPo2() {
        return this.SPo2;
    }

    public void setSPo2(int i) {
        this.SPo2 = i;
    }

    public int getBreathPer() {
        return this.breathPer;
    }

    public void setBreathPer(int i) {
        this.breathPer = i;
    }

    public int getCvrr() {
        return this.cvrr;
    }

    public void setCvrr(int i) {
        this.cvrr = i;
    }

    public int getHrv() {
        return this.hrv;
    }

    public void setHrv(int i) {
        this.hrv = i;
    }

}
