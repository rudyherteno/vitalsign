package com.clj.blesample.model;

import android.util.Log;

import com.clj.blesample.lib.TransUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class HeartInfo {
    public int heartTimes;
    public int hour;
    public long rtime;
    public String rtimeFormat;
    public String timeStr;

    public void sqlinster() {
    }

    public void fameDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        this.rtimeFormat = simpleDateFormat.format(new Date(this.rtime));
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
        simpleDateFormat2.setTimeZone(TimeZone.getDefault());
        this.timeStr = simpleDateFormat2.format(new Date(this.rtime));
        System.out.print(this.timeStr);
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("HH");
        simpleDateFormat3.setTimeZone(TimeZone.getDefault());
        this.hour = Integer.parseInt(simpleDateFormat3.format(new Date(this.rtime)));
    }

    public void initWithData(byte[] bArr) {
        long Bytes2Dec = TransUtils.Bytes2Dec(new byte[]{bArr[3], bArr[2], bArr[1], bArr[0]}) + 946684800;
        this.rtime = Bytes2Dec;
        long j = Bytes2Dec * 1000;
        this.rtime = j;
        this.rtime = j - TransUtils.getTimeOffset();
        this.heartTimes = TransUtils.Bytes2Dec(new byte[]{0, 0, 0, bArr[5]});
        fameDate();
        Log.d("uuuuu-heart", String.valueOf(this.heartTimes));

    }



}
