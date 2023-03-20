package com.clj.blesample.model;

import android.util.Log;

import com.clj.blesample.lib.TransUtils;
import com.clj.fastble.utils.HexUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class BloodInfo {
    public int DBP;
    public int SBP;
    public int isInflated;
    public int isUpload;
    public long rtime;
    public String rtimeFormat;
    public String timeStr;

    public void fameDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        this.rtimeFormat = simpleDateFormat.format(new Date(this.rtime));
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
        simpleDateFormat2.setTimeZone(TimeZone.getDefault());
        this.timeStr = simpleDateFormat2.format(new Date(this.rtime));
    }



    public void initWithData(byte[] bArr) {
        if (bArr.length == 8) {
            String[] separated = HexUtil.formatHexString(bArr, true).split(" ");
            long Bytes2Dec = TransUtils.Bytes2Dec(new byte[]{bArr[3], bArr[2], bArr[1], bArr[0]}) + 946684800;
            this.rtime = Bytes2Dec;
            long j = Bytes2Dec * 1000;
            this.rtime = j;
            this.rtime = j - TransUtils.getTimeOffset();
            this.SBP = TransUtils.Bytes2Dec(new byte[]{0, 0, 0, bArr[6]});

            this.isInflated = bArr[4] & -1;
            fameDate();
            Log.d("indicate_data_blood_sbp", String.valueOf(this.SBP));
            Log.d("indicate_data_blood_dbp", String.valueOf(this.DBP ));
            int decimal=Integer.parseInt(String.valueOf(separated[5]),16);
            Log.d("indicate_data_blood_dbp", String.valueOf(decimal));
            this.DBP = decimal;
            Log.d("uuuu-notify", String.valueOf(decimal));
            return;
        }
    }


}
