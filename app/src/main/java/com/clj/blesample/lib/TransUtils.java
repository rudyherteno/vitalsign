package com.clj.blesample.lib;

import java.util.TimeZone;

public class TransUtils {
    public static int Bytes2Dec(byte[] bArr) {
        int i = 0;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            i += (bArr[i2] & -1) << ((3 - i2) * 8);
        }
        return i;
    }

    public static int getTimeOffset() {
        return TimeZone.getDefault().getOffset(System.currentTimeMillis());
    }
}
