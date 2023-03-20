package com.clj.blesample.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.clj.blesample.MainActivity;
import com.clj.blesample.R;
import com.clj.blesample.lib.Config;
import com.clj.blesample.model.BloodInfo;
import com.clj.blesample.model.HeartInfo;
import com.clj.blesample.model.Spo2Info;
import com.clj.blesample.operation.OperationActivity;
import com.clj.fastble.BleManager;
import com.clj.fastble.bluetooth.SplitWriter;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String KEY_DATA = "key_data";
    private BleDevice connectedDevice;
    private BluetoothGattService bluetoothGattService;
    private BluetoothGattCharacteristic characteristic;
    UUID serviceUUID,char1UUID,char2UUID;
    ImageView imHeart, imSpo2, imRespiratory, imBP, imTemp;
    Button mTest;
    TextView mSpo2, mBpm, mHeart, mBP, mTemp;
    Button mGet;
    int HR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initData();
        initView();
        open_service();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.imHeart:
//                write(connectedDevice,"0580070001d27a");
//                break;
//            case R.id.imSpo2:
//                write(connectedDevice,"05090700019d54");
//                break;
//            case R.id.imRespiratory:
//                //write(connectedDevice,"05090700019d54");
//                write(connectedDevice,"020e06000fd8"); //oke
//                break;
            case R.id.btnGet:
               // write(connectedDevice,"05060700017380"); //oke temp

                mGet.setText("Loading...");
                mSpo2.setText("Loading...");
                mBpm.setText("Loading...");
                mHeart.setText("Loading...");
                mBP.setText("Loading...");
                mTemp.setText("Loading...");
                for (int a = 0; a<4 ;a++) {
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            write_temp(connectedDevice,"020e0800220758df");
                        }
                    }, 1000);
                }
                mGet.setText("GET");
                mHeart.setText(String.valueOf(HR));

                //write(connectedDevice,"05060700017380");
                //write(connectedDevice,"05080700012922");
                //write_dua(connectedDevice,"05090700019d54");
                //write_dua(connectedDevice,"05080700012922"); // berfungsi
                //write(connectedDevice,"03090900000001f3d9");
                //write(connectedDevice,"0200080047436fec");
                //write(connectedDevice,"0502070001824a");
                //write(connectedDevice,"05040700011b6d");
                //write(connectedDevice,"05060700017380");
//                write(connectedDevice,"05090700019d54");
//                write(connectedDevice,"0502070001824a");
//                write(connectedDevice,"05080700012922");


//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        write(connectedDevice,"05090700019d54");
//                    }
//                }, 5000);

                //write(connectedDevice,"0312190000020032330102003331020200333104010004ec79");
                //write(connectedDevice,"03090900010001c3ee");
                break;

        }
    }

    private void initView() {
        mGet = (Button)findViewById(R.id.btnGet);
        mGet.setOnClickListener(this);


        mSpo2 = (TextView)findViewById(R.id.tvSpo2);
        mBpm = (TextView)findViewById(R.id.tvBpm);
        mHeart = (TextView)findViewById(R.id.tvHeart);
        mBP = (TextView)findViewById(R.id.tvBloodPressure);
        mTemp = (TextView)findViewById(R.id.tvTemp);
    }

    private void initData() {
        connectedDevice = getIntent().getParcelableExtra(KEY_DATA);
        if (connectedDevice == null)
            finish();
    }

    public void open_service(){
        if (BleManager.getInstance().isConnected(connectedDevice)) {
            //Intent intent = new Intent(MainActivity.this, OperationActivity.class);
            //intent.putExtra(OperationActivity.KEY_DATA, bleDevice);
            //startActivity(intent);
            BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(connectedDevice);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                for (BluetoothGattService services : gatt.getServices()) {
                    Log.d("gat service",services.getUuid().toString());
                    if (services.getUuid().toString().equals(Config.char0.toLowerCase())) {
                        serviceUUID = services.getUuid();
                        for (BluetoothGattCharacteristic characteristic : services.getCharacteristics()) {
                            if (characteristic.getUuid().toString().equals(Config.char1.toLowerCase())) {
                                char1UUID = characteristic.getUuid();
                                open_indicate(connectedDevice, characteristic);
                            } else if (characteristic.getUuid().toString().equals(Config.char2.toLowerCase())) {
                                char2UUID = characteristic.getUuid();
                            } else if (characteristic.getUuid().toString().equals(Config.char3.toLowerCase())) {
                                open_indicate(connectedDevice, characteristic);
                            }
                        }
                    } else if (services.getUuid().toString().equals(Config.char_heart.toLowerCase())) {
                        serviceUUID = services.getUuid();
                        for (BluetoothGattCharacteristic characteristic : services.getCharacteristics()) {
                            open_notify(connectedDevice, characteristic);
                        }

                    }

                }
            }
        }
    }

    public void open_indicate(final BleDevice bleDevice, final BluetoothGattCharacteristic characteristic){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final BluetoothGattCharacteristic chr = characteristic;
            //BleManager.getInstance().stopIndicate(bleDevice, chr.getService().getUuid().toString(), chr.getUuid().toString());
            Log.d("open_indicate",chr.getService().getUuid().toString()+" "+chr.getUuid().toString());

            BleManager.getInstance().indicate(
                    bleDevice,
                    chr.getService().getUuid().toString(),
                    chr.getUuid().toString(),
                    new BleIndicateCallback() {

                        @Override
                        public void onIndicateSuccess() {
                            Log.d("indicate_success", chr.getUuid().toString());

                        }

                        @Override
                        public void onIndicateFailure(final BleException exception) {
                            Log.d("indicate_fail", chr.getUuid().toString()+ exception);
                            open_indicate(bleDevice,characteristic);
                        }

                        @Override
                        public void onCharacteristicChanged(final byte[] data) {
//                            final byte[] onchar = data;
//                            String str = null;
//                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                                str = new String(data, StandardCharsets.UTF_8);
//                            }
//                            final String finalStr = str;
//                            Log.d(
//                                    "teno indicate","string="+ finalStr +" formatHexString=" +HexUtil.formatHexString(
//                                            data,
//                                            true
//                                    ) + " ori=" + data
//                            );
//                            if (data[0] == 5 && data[1] == 24) {
//                                Log.d("indicate 05 24",makeSendMsg(data,20).toString());
//                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int i = 0;
                                    if (data[0] == 5 && data[1] == 24) {

                                        int length = data.length - 6;
                                        byte[] bArr2 = new byte[length];
                                        while (i < length) {
                                            bArr2[i] = data[i + 4];
                                            i++;
                                        }
                                        List<byte[]> dataSpo2 = prepareSpo2Data(bArr2,20);
                                        perSpo2(dataSpo2);
                                    }
                                    else if (data[0] == 5 && data[1] == 23) {
                                        int length = data.length - 6;
                                        byte[] bArr2 = new byte[length];
                                        while (i < length) {
                                            bArr2[i] = data[i + 4];
                                            i++;
                                        }
                                        perBlood(bArr2);
                                    }
                                    else if (data[0] == 5 && data[1] == 21) {
                                        int length = data.length - 6;
                                        byte[] bArr2 = new byte[length];
                                        while (i < length) {
                                            bArr2[i] = data[i + 4];
                                            i++;
                                        }
                                        perHeart(bArr2);
                                    }
                                    else if (data[0] == 2 && data[1] == 14) {
                                        String str = HexUtil.formatHexString(data,true);
                                        Log.d("indicate_temp_raw", HexUtil.formatHexString(data,true));
                                        String[] separated = str.split(" ");
                                        int temp = Integer.parseInt(separated[4], 16);
                                        int decimal = Integer.parseInt(separated[5], 16);
                                        mTemp.setText(temp+"."+decimal);
                                    }
                                    Log.d("indicate_data", HexUtil.formatHexString(data,true));
                                    //perBlood(data);
                                    //Log.d("indicate_data_blood", HexUtil.formatHexString(data,true));
                                    //Log.d("indicate_data_uuid", chr.getService().getUuid().toString());
                                    //Log.d("indicate_data_chr_str", String.valueOf(chr.getValue()));
                                    //Log.d("indicate_data_chr_hex", HexUtil.formatHexString(chr.getValue()));
//                                    Log.d("indicate_data_str", String.valueOf(onchar));
//                                    Log.d("indicate_data_hex", HexUtil.formatHexString(onchar));
//                                    Log.d(
//                                            "teno indicate","string="+ finalStr +" formatHexString=" +HexUtil.formatHexString(
//                                                    data,
//                                                    true
//                                            ) + " ori=" + data
//                                    );
                                }
                            });
                        }
                    });
        }
    }
    public void open_notify(final BleDevice bleDevice, final BluetoothGattCharacteristic characteristic){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final BluetoothGattCharacteristic chr = characteristic;
            //BleManager.getInstance().stopIndicate(bleDevice, chr.getService().getUuid().toString(), chr.getUuid().toString());
            Log.d("open_notify",chr.getService().getUuid().toString()+" "+chr.getUuid().toString());

            BleManager.getInstance().notify(
                    bleDevice,
                    chr.getService().getUuid().toString(),
                    chr.getUuid().toString(),
                    new BleNotifyCallback() {

                        @Override
                        public void onNotifySuccess() {
                            Log.d("open_notify_success_ht", chr.getUuid().toString());
                            open_notify_temp(bleDevice);
                        }

                        @Override
                        public void onNotifyFailure(BleException exception) {
                            Log.d("open_notify_fail_ht", chr.getUuid().toString());
                        }

                        @Override
                        public void onCharacteristicChanged(final byte[] data) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                        Log.d("uuuu-notify-heart", HexUtil.formatHexString(data, true));
                                        String data_string = HexUtil.formatHexString(data, true);
                                        String[] separated = data_string.split(" ");
                                        if (!separated[1].equals("00")) {
                                            int decimal = Integer.parseInt(separated[1], 16);
                                            Log.d("uuuu-notify", String.valueOf(decimal));
                                            HR = decimal;

                                        }

                                    //perBlood(data);
                                    //Log.d("indicate_data_blood", HexUtil.formatHexString(data,true));
                                    //Log.d("indicate_data_uuid", chr.getService().getUuid().toString());
                                    //Log.d("indicate_data_chr_str", String.valueOf(chr.getValue()));
                                    //Log.d("indicate_data_chr_hex", HexUtil.formatHexString(chr.getValue()));
//                                    Log.d("indicate_data_str", String.valueOf(onchar));
//                                    Log.d("indicate_data_hex", HexUtil.formatHexString(onchar));
//                                    Log.d(
//                                            "teno indicate","string="+ finalStr +" formatHexString=" +HexUtil.formatHexString(
//                                                    data,
//                                                    true
//                                            ) + " ori=" + data
//                                    );
                                }
                            });
                        }
                    });
        }
    }
    public void open_notify_temp(final BleDevice bleDevice){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final BluetoothGattCharacteristic chr = characteristic;
            //BleManager.getInstance().stopIndicate(bleDevice, chr.getService().getUuid().toString(), chr.getUuid().toString());
//            Log.d("open_notify_temp",chr.getService().getUuid().toString()+" "+chr.getUuid().toString());

            BleManager.getInstance().notify(
                    bleDevice,
                    "6e400001-b5a3-f393-e0a9-e50e24dcca9e",
                    "6e400002-b5a3-f393-e0a9-e50e24dcca9e",
                    new BleNotifyCallback() {

                        @Override
                        public void onNotifySuccess() {
                            //Log.d("open_notify_success_tmp", chr.getUuid().toString());

                        }

                        @Override
                        public void onNotifyFailure(BleException exception) {
                            //Log.d("open_notify_fail_tmp", chr.getUuid().toString());
                        }

                        @Override
                        public void onCharacteristicChanged(final byte[] data) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                        Log.d("uuuu-notify-temp", HexUtil.formatHexString(data, true));
                                        String data_string = HexUtil.formatHexString(data, true);
                                        String[] separated = data_string.split(" ");
//                                        if (!separated[1].equals("00")) {
//                                            int decimal = Integer.parseInt(separated[1], 16);
//                                            Log.d("uuuu-notify", String.valueOf(decimal));
//                                            mTemp.setText(String.valueOf(decimal));
//                                        }

                                    //perBlood(data);
                                    //Log.d("indicate_data_blood", HexUtil.formatHexString(data,true));
                                    //Log.d("indicate_data_uuid", chr.getService().getUuid().toString());
                                    //Log.d("indicate_data_chr_str", String.valueOf(chr.getValue()));
                                    //Log.d("indicate_data_chr_hex", HexUtil.formatHexString(chr.getValue()));
//                                    Log.d("indicate_data_str", String.valueOf(onchar));
//                                    Log.d("indicate_data_hex", HexUtil.formatHexString(onchar));
//                                    Log.d(
//                                            "teno indicate","string="+ finalStr +" formatHexString=" +HexUtil.formatHexString(
//                                                    data,
//                                                    true
//                                            ) + " ori=" + data
//                                    );
                                }
                            });
                        }
                    });
        }
    }

    public static byte[] byteMerger(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    public void perBlood(byte[] bArr) {
        Log.d("indicate_perBlood", HexUtil.formatHexString(bArr, true));
        int length = bArr.length;
        byte[] bArr2 = new byte[length];
        System.arraycopy(bArr, 0, bArr2, 0, length);
        Log.d("indicate_arr2_length", String.valueOf(bArr2.length));
        ArrayList<BloodInfo> arrayList = new ArrayList();
        for (int i = 0; i < length; i += 8) {

            BloodInfo bloodInfo = new BloodInfo();
            byte[] bArr3 = new byte[8];
            Log.d("indicate_arr2_length_"+i, String.valueOf(bArr3.length));

            try {
                System.arraycopy(bArr2, i, bArr3, 0, 8);
                Log.d("indicate_arr2_length_"+i+"_data", HexUtil.formatHexString(bArr3, true));
                bloodInfo.initWithData(bArr3);
                arrayList.add(bloodInfo);
                if (bloodInfo.DBP>0)
                    mBP.setText(bloodInfo.DBP+"/"+bloodInfo.SBP);
            } catch (ArrayIndexOutOfBoundsException e){
                Log.d("indicate_arr2_err_"+i, String.valueOf(bArr3.length));
            }
            //bloodInfo.initWithData(bArr3);
            //arrayList.add(bloodInfo);
        }


        for (BloodInfo bloodInfo2 : arrayList) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            bloodInfo2.rtimeFormat = simpleDateFormat.format(new Date(bloodInfo2.rtime));
        }

        for (int i2 = 0; i2 < arrayList.size(); i2++) {

            String rtime = String.valueOf(Long.valueOf(((BloodInfo) arrayList.get(i2)).rtime));
            String rtimeFormat = (((BloodInfo) arrayList.get(i2)).rtimeFormat);
            String SBP = String.valueOf(Integer.valueOf(((BloodInfo) arrayList.get(i2)).SBP));
            String DBP = String.valueOf(Integer.valueOf(((BloodInfo) arrayList.get(i2)).DBP));
            Log.d("uuuuu", "rtime="+String.valueOf(rtime) +
                    " rtimeFormat=" +String.valueOf(rtimeFormat)+
                    " SBP=" +String.valueOf(SBP)+
                    " DBP=" +String.valueOf(DBP));

        }
    }

    private void perSpo2(List<byte[]> data) {
        Log.d("indicate_spo2", String.valueOf(data));
        for (byte[] bArr3 : data) {
            if (bArr3.length >= 15) {
                Spo2Info spo2Info = new Spo2Info();
                spo2Info.initWithData(bArr3);
                if (spo2Info.getSPo2()>0)
                mSpo2.setText(spo2Info.getSPo2()+"%");
                if (spo2Info.getBreathPer()>0)
                mBpm.setText(String.valueOf(spo2Info.getBreathPer()));
            }
        }
    }

    public void perHeart(byte[] bArr) {

        int length = bArr.length;
        byte[] bArr2 = new byte[length];
        System.arraycopy(bArr, 0, bArr2, 0, length);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < length; i += 6) {
            HeartInfo heartInfo = new HeartInfo();
            byte[] bArr3 = new byte[6];
            System.arraycopy(bArr2, i, bArr3, 0, 6);
            heartInfo.initWithData(bArr3);
            arrayList.add(heartInfo);
            if (heartInfo.heartTimes>0)
                mSpo2.setText(heartInfo.heartTimes);
        }

    }


    public void sendRealMsg(byte[] bArr, int i) {
        if (bArr != null) {
            Log.d("indicate_sendRealMsg","success "+ bArr);

            //write(connectedDevice,"05090700019d54");  //spo2 oke
            //write(bleDev,"05080700012922"); blood oke
            //write(bleDev,"0580070001d27a");

            BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(connectedDevice);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                for (BluetoothGattService services : gatt.getServices()) {
                    for (BluetoothGattCharacteristic characteristic : services.getCharacteristics()) {
                        int charaProp = characteristic.getProperties();
                        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                            //write(bleDev, characteristic, bArr);
                        }
                    }
                }
            }

        }
    }

    public void write(BleDevice bleDevice, final String string){
        //final BluetoothGattCharacteristic chr = characteristic;
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BleManager.getInstance().write(
                    bleDevice,
                    "be940000-7333-be46-b7ae-689e71722bd5",
//                    "be940001-7333-be46-b7ae-689e71722bd5",
                    "be940001-7333-be46-b7ae-689e71722bd5",
                    HexUtil.hexStringToBytes(string),
                    new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {
                            Log.d("indicate_write","success "+HexUtil.formatHexString(justWrite));
                            write_tiga(connectedDevice,"020e0800220758df");
                        }

                        @Override
                        public void onWriteFailure(BleException exception) {
                            Log.d("indicate_write","gagal "+string+ " "+ exception);
                        }
                    });
        }
    }
    public void write_tiga(BleDevice bleDevice, final String string){
        //final BluetoothGattCharacteristic chr = characteristic;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BleManager.getInstance().write(
                    bleDevice,
                    "be940000-7333-be46-b7ae-689e71722bd5",
//                    "be940001-7333-be46-b7ae-689e71722bd5",
                    "be940001-7333-be46-b7ae-689e71722bd5",
                    HexUtil.hexStringToBytes(string),
                    new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {
                            Log.d("indicate_write","success "+HexUtil.formatHexString(justWrite));
                        }

                        @Override
                        public void onWriteFailure(BleException exception) {
                            Log.d("indicate_write","gagal "+string+ " "+ exception);
                        }
                    });
        }
    }
    public void write_dua(BleDevice bleDevice, String string){
        //final BluetoothGattCharacteristic chr = characteristic;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BleManager.getInstance().write(
                    bleDevice,
                    "be940000-7333-be46-b7ae-689e71722bd5",
                    "be940001-7333-be46-b7ae-689e71722bd5",
                    HexUtil.hexStringToBytes(string),
                    true,
                    true,
                    2,
                    new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {
                            Log.d("indicate_write","success "+HexUtil.formatHexString(justWrite));
                            write(connectedDevice,"05090700019d54");
                        }

                        @Override
                        public void onWriteFailure(BleException exception) {

                        }
                    }
                    );

        }
    }

    public void write_temp(BleDevice bleDevice, String string){
        //final BluetoothGattCharacteristic chr = characteristic;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BleManager.getInstance().write(
                    bleDevice,
                    "be940000-7333-be46-b7ae-689e71722bd5",
                    "be940001-7333-be46-b7ae-689e71722bd5",
                    HexUtil.hexStringToBytes(string),
                    true,
                    true,
                    3,
                    new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {
                            Log.d("indicate_write","success "+HexUtil.formatHexString(justWrite));
                            write_bloodpress(connectedDevice,"05080700012922");
                        }

                        @Override
                        public void onWriteFailure(BleException exception) {

                        }
                    }
            );

        }
    }

    public void write_bloodpress(BleDevice bleDevice, String string){
        //final BluetoothGattCharacteristic chr = characteristic;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BleManager.getInstance().write(
                    bleDevice,
                    "be940000-7333-be46-b7ae-689e71722bd5",
                    "be940001-7333-be46-b7ae-689e71722bd5",
                    HexUtil.hexStringToBytes(string),
                    true,
                    true,
                    3,
                    new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {
                            Log.d("indicate_write","success "+HexUtil.formatHexString(justWrite));
                            write_spo2(connectedDevice,"05090700019d54");
                        }

                        @Override
                        public void onWriteFailure(BleException exception) {

                        }
                    }
            );

        }
    }
    public void write_spo2(BleDevice bleDevice, String string){
        //final BluetoothGattCharacteristic chr = characteristic;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BleManager.getInstance().write(
                    bleDevice,
                    "be940000-7333-be46-b7ae-689e71722bd5",
                    "be940001-7333-be46-b7ae-689e71722bd5",
                    HexUtil.hexStringToBytes(string),
                    true,
                    true,
                    3,
                    new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {
                            Log.d("indicate_write","success "+HexUtil.formatHexString(justWrite));
                        }

                        @Override
                        public void onWriteFailure(BleException exception) {

                        }
                    }
            );

        }
    }

    public static List<byte[]> prepareSpo2Data(byte[] bArr, int i) {
        ArrayList arrayList = new ArrayList();
        int length = bArr.length / i;
        if (bArr.length % i > 0) {
            length++;
        }
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i2 * i;
            int length2 = i3 + i > bArr.length ? bArr.length - i3 : i;
            byte[] bArr2 = new byte[length2];
            System.arraycopy(bArr, i3, bArr2, 0, length2);
            arrayList.add(bArr2);
            Log.d("indicate 05 24 detil", HexUtil.formatHexString(bArr2, true));
        }
        return arrayList;
    }



}