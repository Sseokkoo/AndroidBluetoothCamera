package com.example.copyhomet.Main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.copyhomet.Bluetooth.Bluetooth1.BLE.BleProfileService;
import com.example.copyhomet.Bluetooth.Bluetooth1.UART.UARTManager;
import com.example.copyhomet.Bluetooth.Bluetooth2.BLE.BleProfileService_2;
import com.example.copyhomet.Bluetooth.Bluetooth2.UART.UARTManager2;
import com.example.copyhomet.Bluetooth.Bluetooth3.BLE.BleProfileService_3;
import com.example.copyhomet.Bluetooth.Bluetooth3.UART.UARTManager3;
import com.example.copyhomet.Bluetooth.Bluetooth4.BLE.BleProfileService_4;
import com.example.copyhomet.Bluetooth.Bluetooth4.UART.UARTManager4;
import com.example.copyhomet.Bluetooth.Bluetooth5.BLE.BleProfileService_5;
import com.example.copyhomet.Bluetooth.Bluetooth5.UART.UARTManager5;
import com.example.copyhomet.DialogExTimeSet;
import com.example.copyhomet.DialogSpeach;
import com.example.copyhomet.ModeDialog;
import com.example.copyhomet.R;
import com.example.copyhomet.TotalActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.speech.tts.TextToSpeech.ERROR;
import static android.view.View.GONE;


public class MainTestActivity extends AppCompatActivity {

    TextView moreFunction;
    private DrawerLayout drawerLayout;
    private View drawerView;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Button btnInit;
    Button ExMode;
    TextView ExTime;
    Button play;
    Button Datas;
    String startTime;
    Timer timer,ExTimer;
    String sec,min;

    String DayExTimer;


    ConstraintLayout device1,device2,device3,device4,device5;
    TextView count1,count2,count3,count4,count5;
    Button plus1,plus2,plus3,plus4,plus5,plusAll;
    Button minus1,minus2,minus3,minus4,minus5,minusAll;

    TextView tvEmptyDevice;
    int i =0;
    ImageView btnMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_constraint_test);

        getSupportActionBar().hide();
        SetDrawer();

        SetSST();

        pref = getApplication().getSharedPreferences("info",MODE_PRIVATE);
        editor = pref.edit();

        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(n->{
            drawerLayout.openDrawer(drawerView);
        });

        DayExTimer = "00:00";
        init();

        SharedPreferences pref = getApplication().getSharedPreferences("info",MODE_PRIVATE);
        if(pref.getString("mode","M").equals("M")) {
            ExMode.setText("근육");
        }else if(pref.getString("mode","M").equals("S")){
            ExMode.setText("다이어트");
        }else{
            ExMode.setText("운동 모드");
        }

        moreFunction = findViewById(R.id.moreFunction);
        moreFunction.setOnClickListener(n->{
            Intent intent = new Intent(MainTestActivity.this,DialogMoreFunction.class);
            startActivity(intent);
        });


        btnInit = findViewById(R.id.btnInitAll);
        btnInit.setOnClickListener(n->{

            /**강제 종료 테스트**/
//            Button aa = null;
//            aa.setOnClickListener(na->{
//                Toast.makeText(mainActivity, "hi", Toast.LENGTH_SHORT).show();
//            });
            Intent intent = new Intent(MainTestActivity.this,DialogReset.class);
            startActivityForResult(intent,103);
        });
    }


    public void SetDrawer(){

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);

        TextView tvProfile = findViewById(R.id.tvProfile);
        tvProfile.setOnClickListener(n->{
            Intent intent = new Intent(MainTestActivity.this,MyPageActivity.class);
            startActivity(intent);
        });

    }
    public void init(){

        tvEmptyDevice = findViewById(R.id.tvEmptyDevice);
        ExMode = findViewById(R.id.ExMode);
        ExMode.setOnClickListener(n->{
            Intent intent = new Intent(MainTestActivity.this, ModeDialog.class);
            startActivityForResult(intent,101);
        });
        ExTime = findViewById(R.id.ExTime);
        ExTime.setText("21:00");
        //오늘 하루 지나면 서버에 전송
        SharedPreferences pref = getApplication().getSharedPreferences("info",MODE_PRIVATE);




        setBle();



        count1 = findViewById(R.id.edtCount1);
        count2= findViewById(R.id.edtCount2);
        count4 = findViewById(R.id.edtCount4);

        plus1 =findViewById(R.id.plus1);
        plus2 =findViewById(R.id.plus2);
        plus4 = findViewById(R.id.plus4);
        plusAll =findViewById(R.id.plusAll);

        minus1 =findViewById(R.id.minus1);
        minus2 = findViewById(R.id.minus2);
        minus4 = findViewById(R.id.minus4);
        minusAll = findViewById(R.id.minusAll);

        play = findViewById(R.id.play);
        Datas = findViewById(R.id.Datas);
        ButtonClick();
    }


    public void setBle(){
        device1 =findViewById(R.id.Constraint1);
        if(BleProfileService.getBleProfile()!=null) {
            if (BleProfileService.getBleProfile().getConnectionState() != 2) {
                device1.setVisibility(GONE);
            }
        }else{
            device1.setVisibility(GONE);

        }

        device2 = findViewById(R.id.Constraint2);
        if(BleProfileService_2.getBleProfile()!=null) {
            if (BleProfileService_2.getBleProfile().getConnectionState() != 2) {
                device2.setVisibility(GONE);
            }
        }else{
            if(BleProfileService_3.getBleProfile()!=null){
                if (BleProfileService_3.getBleProfile().getConnectionState() != 2) {
                    device2.setVisibility(GONE);
                }
            }else{
                device2.setVisibility(GONE);
            }
        }
//        device3 = findViewById(R.id.Device3Layout);
//        if(BleProfileService_3.getBleProfile()!=null) {
//            if (BleProfileService_3.getBleProfile().getConnectionState() != 2) {
//                device3.setVisibility(View.GONE);
//
//            }
//        }else{
//            device3.setVisibility(View.GONE);
//
//        }
        device4 = findViewById(R.id.Constraint4);
        if(BleProfileService_4.getBleProfile()!=null) {
            if (BleProfileService_4.getBleProfile().getConnectionState() != 2) {
                device4.setVisibility(GONE);

            }
        }else{
            if(BleProfileService_5.getBleProfile()!=null){
                if (BleProfileService_5.getBleProfile().getConnectionState() != 2) {
                    device4.setVisibility(GONE);
                }

            }else{
                device4.setVisibility(GONE);
            }
        }

        if(BleProfileService.getBleProfile()==null && BleProfileService_2.getBleProfile()==null && BleProfileService_3.getBleProfile()==null
                && BleProfileService_4.getBleProfile()==null && BleProfileService_5.getBleProfile()==null){

            tvEmptyDevice.setVisibility(View.VISIBLE);
        }
    }
    public void ButtonClick(){
        //팔 + 클릭시 팔 1,2 증가 ??
        //다리 + 클릭시 다리 1,2 증가 ??

        plus1.setOnClickListener(n->{
            if(i==1) {
                String count = count1.getText().toString();
                int aCount = Integer.parseInt(count);
                if (aCount < 30) {

                    count1.setText(String.valueOf(aCount + 1));
                    editor.putString("Device1Setting", String.valueOf(aCount + 1));
                    editor.commit();
                    UARTManager.getManager().send("FF060001FE");
                }
            }else{
                Toast.makeText(MainTestActivity.this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
            }
        });
        plus2.setOnClickListener(n->{
            if(i==1) {
                String count = count2.getText().toString();
                int aCount = Integer.parseInt(count);
                if (aCount < 30) {

                    count2.setText(String.valueOf(aCount + 1));
                    editor.putString("Device2Setting", String.valueOf(aCount + 1));
                    editor.commit();
                    if (UARTManager2.getManager() != null) {
                        UARTManager2.getManager().send("FF030001FE");
                    }
                    if (UARTManager3.getManager() != null) {
                        UARTManager3.getManager().send("FF030001FE");
                    }
                }
            }else{
                Toast.makeText(MainTestActivity.this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
            }
        });
//        plus3.setOnClickListener(n->{
//            String count = count3.getText().toString();
//            int aCount = Integer.parseInt(count);
//            if(aCount<100) {
//                count3.setText(String.valueOf(aCount + 5));
//                UARTManager3.getManager().send("FF030001FE");
//            }
//        });
        plus4.setOnClickListener(n->{
            if(i==1) {
                String count = count4.getText().toString();
                int aCount = Integer.parseInt(count);
                if (aCount < 30) {
                    count4.setText(String.valueOf(aCount + 1));
                    editor.putString("Device4Setting", String.valueOf(aCount + 1));
                    editor.commit();
                    if (UARTManager4.getManager() != null) {
                        UARTManager4.getManager().send("FF090001FE");
                    }
                    if (UARTManager5.getManager() != null) {
                        UARTManager5.getManager().send("FF090001FE");
                    }
                }
            }else{
                Toast.makeText(MainTestActivity.this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
            }
        });
//        plus5.setOnClickListener(n->{
//            String count = count5.getText().toString();
//            int aCount = Integer.parseInt(count);
//            if(aCount<100) {
//                count5.setText(String.valueOf(aCount + 5));
//                UARTManager5.getManager().send("FF090001FE");
//            }
//        });

        minus1.setOnClickListener(n->{
            if(i==1) {
                String count = count1.getText().toString();
                int aCount = Integer.parseInt(count);
                if (aCount > 0) {
                    count1.setText(String.valueOf(aCount - 1));
                    editor.putString("Device1Setting", String.valueOf(aCount - 1));
                    editor.commit();
                    UARTManager.getManager().send("FF070001FE");
                }
            }else{
                Toast.makeText(MainTestActivity.this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
            }
        });
        minus2.setOnClickListener(n->{
            if(i==1) {
                String count = count2.getText().toString();
                int aCount = Integer.parseInt(count);
                if (aCount > 0) {
                    count2.setText(String.valueOf(aCount - 1));
                    editor.putString("Device2Setting", String.valueOf(aCount - 1));
                    editor.commit();
                    if (UARTManager2.getManager() != null) {
                        UARTManager2.getManager().send("FF040001FE");
                    }
                    if (UARTManager3.getManager() != null) {
                        UARTManager3.getManager().send("FF040001FE");
                    }
                }
            }else{
                Toast.makeText(MainTestActivity.this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
            }
        });
//        minus3.setOnClickListener(n->{
//            String count = count3.getText().toString();
//            int aCount = Integer.parseInt(count);
//            if(aCount>0) {
//                count3.setText(String.valueOf(aCount - 5));
//                UARTManager3.getManager().send("FF040001FE");
//            }
//        });
        minus4.setOnClickListener(n->{
            if(i==1) {
                String count = count4.getText().toString();
                int aCount = Integer.parseInt(count);
                if (aCount > 0) {

                    count4.setText(String.valueOf(aCount - 1));
                    editor.putString("Device4Setting", String.valueOf(aCount - 1));
                    editor.commit();
                    if (UARTManager4.getManager() != null) {
                        UARTManager4.getManager().send("FF0A0001FE");
                    }
                    if (UARTManager5.getManager() != null) {
                        UARTManager5.getManager().send("FF0A0001FE");
                    }
                }
            }else{
                Toast.makeText(MainTestActivity.this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
            }
        });
//        minus5.setOnClickListener(n->{
//            String count = count5.getText().toString();
//            int aCount = Integer.parseInt(count);
//            if(aCount>0) {
//                count5.setText(String.valueOf(aCount - 5));
//                UARTManager5.getManager().send("FF0A0001FE");
//            }
//        });

        plusAll.setOnClickListener(n->{
            if(i==1) {
                PlusClick();
            }else{
                Toast.makeText(MainTestActivity.this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
            }
        });
        minusAll.setOnClickListener(n->{
            if(i==1) {
                MinusClick();
            }else{
                Toast.makeText(MainTestActivity.this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
            }

        });


        play.setOnClickListener(n->{
            if(BleProfileService.getBleProfile()!=null){
                if(BleProfileService.getBleProfile().getConnectionState()==2){
                    PlayClick();
                }
            }else if(BleProfileService_2.getBleProfile()!=null){
                if(BleProfileService_2.getBleProfile().getConnectionState()==2){
                    PlayClick();
                }
            }else if(BleProfileService_3.getBleProfile()!=null){
                if(BleProfileService_3.getBleProfile().getConnectionState()==2){
                    PlayClick();
                }
            }else if(BleProfileService_4.getBleProfile()!=null){
                if(BleProfileService_4.getBleProfile().getConnectionState()==2){
                    PlayClick();
                }
            }else if(BleProfileService_5.getBleProfile()!=null){
                if(BleProfileService_5.getBleProfile().getConnectionState()==2){
                    PlayClick();
                }
            } else{
                Toast.makeText(MainTestActivity.this, "연결된 기기가 없습니다.", Toast.LENGTH_SHORT).show();
            }

        });
        Datas.setOnClickListener(n->{
            Intent intent = new Intent(MainTestActivity.this, TotalActivity.class);
            startActivity(intent);
        });
    }


    public void MinusClick(){
        if(UARTManager.getManager()!=null) {
            String count = count1.getText().toString();
            int aCount = Integer.parseInt(count);
            if(aCount>0) {
                count1.setText(String.valueOf(aCount - 1));
                editor.putString("Device1Setting", String.valueOf(aCount-1));
                editor.commit();
                UARTManager.getManager().send("FF0C0001FE");
//                    UARTManager.getManager().send("FF0C0001FE");
            }
        }
        if(UARTManager2.getManager()!=null || UARTManager3.getManager()!=null)  {
            String count = count2.getText().toString();
            int aCount = Integer.parseInt(count);
            if(aCount>0) {
                count2.setText(String.valueOf(aCount - 1));
                editor.putString("Device2Setting", String.valueOf(aCount-1));
                editor.commit();
                if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF040001FE");
                }
                if(UARTManager3.getManager()!=null){
                    UARTManager3.getManager().send("FF040001FE");
                }
            }
        }

        if(UARTManager4.getManager()!=null || UARTManager5.getManager()!=null) {
            String count = count4.getText().toString();
            int aCount = Integer.parseInt(count);
            if(aCount>0) {
                count4.setText(String.valueOf(aCount - 1));
                editor.putString("Device4Setting", String.valueOf(aCount-1));
                editor.commit();
                if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF0A0001FE");
                }
                if(UARTManager5.getManager()!=null){
                    UARTManager5.getManager().send("FF0A0001FE");

                }

            }
        }

    }
    public void PlusClick(){
        if(UARTManager.getManager()!=null) {
            String count = count1.getText().toString();
            int aCount = Integer.parseInt(count);
            if(aCount<30) {
                editor.putString("Device1Setting", String.valueOf(aCount+1));
                editor.commit();
                count1.setText(String.valueOf(aCount + 1));
                UARTManager.getManager().send("FF0D0001FE");

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNOW = new SimpleDateFormat("HH:mm:ss.SSS");
                String DATE = sdfNOW.format(date);

                Log.d("복근 +",DATE);
            }
        }
        if(UARTManager2.getManager()!=null ||UARTManager3.getManager()!=null) {
            String count = count2.getText().toString();
            int aCount = Integer.parseInt(count);
            if(aCount<30) {
                count2.setText(String.valueOf(aCount + 1));
                editor.putString("Device2Setting", String.valueOf(aCount+1));
                editor.commit();
                if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF030001FE");
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNOW = new SimpleDateFormat("HH:mm:ss.SSS");
                    String DATE = sdfNOW.format(date);
                    Log.d("팔1 +",DATE);
                }
                if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF030001FE");
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNOW = new SimpleDateFormat("HH:mm:ss.SSS");
                    String DATE = sdfNOW.format(date);
                    Log.d("팔2 +",DATE);
                }
            }
        }

        if(UARTManager4.getManager()!=null || UARTManager5.getManager()!=null) {
            String count = count4.getText().toString();
            int aCount = Integer.parseInt(count);
            if(aCount<30) {
                count4.setText(String.valueOf(aCount + 1));
                editor.putString("Device4Setting", String.valueOf(aCount+1));
                editor.commit();
                if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF090001FE");
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNOW = new SimpleDateFormat("HH:mm:ss.SSS");
                    String DATE = sdfNOW.format(date);
                    Log.d("다리1 +",DATE);
                }
                if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF090001FE");
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNOW = new SimpleDateFormat("HH:mm:ss.SSS");
                    String DATE = sdfNOW.format(date);
                    Log.d("다리2 +",DATE);
                }
            }

        }
    }
    String useDevice = "";
    public void PlayClick(){
        if(ExTime.getText().toString().equals("00:00")){
            Toast.makeText(MainTestActivity.this, "시간을 설정해 주세요.", Toast.LENGTH_SHORT).show();
        }else {
            // WarmUp 시작에도 타이머 시작
            if (i == 0) {
                useDevice = "";
                if (BleProfileService.getBleProfile() != null) {
                    int a = BleProfileService.getBleProfile().getConnectionState();
                    if (BleProfileService.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
                        useDevice += "배";
                    }
                }
                if (BleProfileService_2.getBleProfile() != null) {
                    if (BleProfileService_2.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
                        if (!useDevice.equals("")) {
                            useDevice += ",";
                        }
                        useDevice += "팔1";
                    }
                }
                if (BleProfileService_3.getBleProfile() != null) {
                    if (BleProfileService_3.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
                        if (!useDevice.equals("")) {
                            useDevice += ",";
                        }
                        useDevice += "팔2";
                    }
                }
                if (BleProfileService_4.getBleProfile() != null) {
                    if (BleProfileService_4.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
                        if (!useDevice.equals("")) {
                            useDevice += ",";
                        }
                        useDevice += "다리1";
                    }
                }
                if (BleProfileService_5.getBleProfile() != null) {
                    if (BleProfileService_5.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
                        if (!useDevice.equals("")) {
                            useDevice += ",";
                        }
                        useDevice += "다리2";
                    }
                }

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNOW = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String DATE = sdfNOW.format(date);

                startTime = DATE.substring(11, 16);

                // 시간 카운트 전달.
//                SendTime();

                Toast.makeText(MainTestActivity.this, "시작", Toast.LENGTH_SHORT).show();
                //warm up 이 끝나는 프로토콜이 전달 되면
                StartTimer();

//                TimerSetProtocol();

                i = 1;


            } else if( i== 1){

                Toast.makeText(MainTestActivity.this, "정지", Toast.LENGTH_SHORT).show();
                StopTimer();
                TimerSetProtocol();
                i = 2;

            }else if(i ==2){
                Toast.makeText(MainTestActivity.this, "시작", Toast.LENGTH_SHORT).show();
                if(timer!=null) {
                    StartTimer();
                }
                TimerSetProtocol();
                i=1;
            }


        }
    }



    public void StopTimer(){
        SharedPreferences pref = getApplication().getSharedPreferences("info",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String ext = ExTime.getText().toString();
        editor.putString("ExTime",ext);
        editor.commit();

        StopExTimer();
//        DayExTimer="00:00";

        /**버전 2 정지 클릭 시 등록**/
        if(timer!=null) {
            timer.cancel();
        }
    }
    public void StartTimer(){

        StartExTimer();
        /**버전 1 하루에 하나씩 등록**/
        timer = new Timer();
        TimerTask tt= timertaskMaker();
        timer.schedule(tt,0,1000);
    }


    public void TimerSetProtocol(){

        Integer bmi = Integer.valueOf(pref.getString("bmi","0").replace(".",""));
        String s_bmi = Integer.toHexString(bmi);
        if (s_bmi.length() < 4) {
            while (s_bmi.length() < 4) {
                s_bmi = "0" + s_bmi;
            }
        }

        // 정지 0000
        if(i ==1){
            if (UARTManager.getManager() != null) {
                UARTManager.getManager().send("FF1A0000FE");
            }
            if (UARTManager2.getManager() != null) {
                UARTManager2.getManager().send("FF1A0000FE");

            }
            if (UARTManager3.getManager() != null) {
                UARTManager3.getManager().send("FF1A0000FE");

            }
            if (UARTManager4.getManager() != null) {
                UARTManager4.getManager().send("FF1A0000FE");

            }
            if (UARTManager5.getManager() != null) {
                UARTManager5.getManager().send("FF1A0000FE");

            }
        }else {
            // 시작 0001
            if (UARTManager.getManager() != null) {
                UARTManager.getManager().send("FF1A0001FE");
                UARTManager.getManager().send("FF19"+s_bmi +"FE");
            }
            if (UARTManager2.getManager() != null) {
                UARTManager2.getManager().send("FF1A0001FE");
                UARTManager2.getManager().send("FF19"+s_bmi +"FE");
            }
            if (UARTManager3.getManager() != null) {
                UARTManager3.getManager().send("FF1A0001FE");
                UARTManager3.getManager().send("FF19"+s_bmi +"FE");
            }
            if (UARTManager4.getManager() != null) {
                UARTManager4.getManager().send("FF1A0001FE");
                UARTManager4.getManager().send("FF19"+s_bmi +"FE");
            }
            if (UARTManager5.getManager() != null) {
                UARTManager5.getManager().send("FF1A0001FE");
                UARTManager5.getManager().send("FF19"+s_bmi +"FE");
            }
        }
    }


    public TimerTask timertaskMaker(){
        TimerTask addTask = new TimerTask() {
            @Override
            public void run() {
                TimeSetMinus();
            }
        };
        return addTask;
    }

    public void TimeSet(){
        min = ExTime.getText().toString().substring(0,2);
        sec = ExTime.getText().toString().substring(3,5);

        int Imin = Integer.parseInt(sec)+1;
        int Ihour = Integer.parseInt(min);
        if(Imin >59){
            Imin = 0;
            Ihour++;
        }
        min = String.valueOf(Ihour);
        if(min.length()<2){
            min = "0"+min;
        }
        sec = String.valueOf(Imin);
        if(sec.length()<2){
            sec = "0"+sec;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ExTime.setText(min+":"+sec);
            }
        });
    }
    public void TimeSetMinus(){
        min = ExTime.getText().toString().substring(0,2);
        sec = ExTime.getText().toString().substring(3,5);

        if(min.equals("00")) {
            if(!sec.equals("00")) {
                int Imin = Integer.parseInt(sec) - 1;
                int Ihour = Integer.parseInt(min);
                if (Imin < 0) {
                    Imin = 59;
                    Ihour--;
                }
                min = String.valueOf(Ihour);
                if (min.length() < 2) {
                    min = "0" + min;
                }
                sec = String.valueOf(Imin);
                if (sec.length() < 2) {
                    sec = "0" + sec;
                }

                editor.putString("ExTime", min + ":" + sec);
                editor.commit();
            }
        }else{
            int Imin = Integer.parseInt(sec) - 1;
            int Ihour = Integer.parseInt(min);
            if (Imin < 0) {
                Imin = 59;
                Ihour--;
            }
            min = String.valueOf(Ihour);
            if (min.length() < 2) {
                min = "0" + min;
            }
            sec = String.valueOf(Imin);
            if (sec.length() < 2) {
                sec = "0" + sec;
            }

            editor.putString("ExTime", min + ":" + sec);
            editor.commit();
        }






        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ExTime.setText(min+":"+sec);
                if(min.equals("00")&&sec.equals("00")) {
                    StopTimer();
                    i=0;
                    Toast.makeText(MainTestActivity.this, "운동이 종료되었습니다.", Toast.LENGTH_SHORT).show();
//                    UARTManager.getManager().setA();

                    if (UARTManager.getManager() != null) {
                        UARTManager.getManager().send("FF130001FE");
                    }
                    if (UARTManager2.getManager() != null) {
                        UARTManager2.getManager().send("FF130001FE");
                    }
                    if (UARTManager3.getManager() != null) {
                        UARTManager3.getManager().send("FF130001FE");
                    }
                    if (UARTManager4.getManager() != null) {
                        UARTManager4.getManager().send("FF130001FE");
                    }
                    if (UARTManager5.getManager() != null) {
                        UARTManager5.getManager().send("FF130001FE");
                    }

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNOW = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String DATE = sdfNOW.format(date);

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("info", MODE_PRIVATE);


                    String email = pref.getString("email", "00");
                    String nickname = pref.getString("nickname", "00");
                    String mode = pref.getString("mode", "M");
                    startTime = DATE.substring(11, 16);

//                    String[] useT = pref.getString("ExTime","00").split(":");
////                    int usetI = Integer.parseInt(useT[0]);
//                    String useTime = String.valueOf(usetI);

//                    String useDevice = "";
//                    if (BleProfileService.getBleProfile() != null) {
//                        int a = BleProfileService.getBleProfile().getConnectionState();
//                        if (BleProfileService.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                            useDevice += "배";
//                        }
//                    }
//                    if (BleProfileService_2.getBleProfile() != null) {
//                        if (BleProfileService_2.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                            if (!useDevice.equals("")) {
//                                useDevice += ",";
//                            }
//                            useDevice += "팔1";
//                        }
//                    }
//                    if (BleProfileService_3.getBleProfile() != null) {
//                        if (BleProfileService_3.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                            if (!useDevice.equals("")) {
//                                useDevice += ",";
//                            }
//                            useDevice += "팔2";
//                        }
//                    }
//                    if (BleProfileService_4.getBleProfile() != null) {
//                        if (BleProfileService_4.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                            if (!useDevice.equals("")) {
//                                useDevice += ",";
//                            }
//                            useDevice += "다리1";
//                        }
//                    }
//                    if (BleProfileService_5.getBleProfile() != null) {
//                        if (BleProfileService_5.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                            if (!useDevice.equals("")) {
//                                useDevice += ",";
//                            }
//                            useDevice += "다리2";
//                        }
//                    }

                    if (!useDevice.equals("")) {
                        SharedPreferences.Editor editor = pref.edit();
                        String useTime = pref.getString("ExTimeToSend", "0");
                        if (!useTime.equals("0")) {
                            if (startTime != null) {

                                String[] ExUseTime = DayExTimer.split(":");
                                int ExUseMin = Integer.parseInt(ExUseTime[0]);
                                int ExUseSec = Integer.parseInt(ExUseTime[1]);
                                String sExUseTime;
                                String sExUseSec;
                                if(ExUseMin !=0) {
                                    if (ExUseSec < 30) {
                                        sExUseSec = "";
                                    } else {
                                        sExUseSec = ".5";
                                    }

                                    sExUseTime = ExUseMin + sExUseSec;

                                    AddInfo task = new AddInfo();
                                    task.execute("http://20.194.57.6/homet/AddInfo.php", email, nickname, mode, startTime, sExUseTime, useDevice);


                                    Toast.makeText(MainTestActivity.this, "데이터를 저장했습니다.", Toast.LENGTH_SHORT).show();
                                    ExTime.setText("21:00");
//                            UARTManager.getManager().setA();
//                            InitUI();
                                }
                            }
                        }
                    } else {
                        Toast.makeText(MainTestActivity.this, "연결된 기기가 없어, 데이터를 저장하지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
    }


    public void StartExTimer(){
        /**버전 1 하루에 하나씩 등록**/
        ExTimer = new Timer();
        TimerTask tt= ExtimertaskMaker();
        ExTimer.schedule(tt,0,1000);
    }
    public void StopExTimer(){
        //데이터 저장
//        SharedPreferences pref = getApplication().getSharedPreferences("info",MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("ExTimer","00:00");
//        editor.commit();

        String email = pref.getString("email", "00");
        String nickname = pref.getString("nickname", "00");
        String mode = pref.getString("mode", "M");

//                    startTime = DATE.substring(11, 16);

//                    String[] useT = pref.getString("ExTime","00").split(":");
////                    int usetI = Integer.parseInt(useT[0]);
//                    String useTime = String.valueOf(usetI);

//        String useDevice = "";
//        if (BleProfileService.getBleProfile() != null) {
//            int a = BleProfileService.getBleProfile().getConnectionState();
//            if (BleProfileService.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                useDevice += "배";
//            }
//        }
//        if (BleProfileService_2.getBleProfile() != null) {
//            if (BleProfileService_2.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                if (!useDevice.equals("")) {
//                    useDevice += ",";
//                }
//                useDevice += "팔1";
//            }
//        }
//        if (BleProfileService_3.getBleProfile() != null) {
//            if (BleProfileService_3.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                if (!useDevice.equals("")) {
//                    useDevice += ",";
//                }
//                useDevice += "팔2";
//            }
//        }
//        if (BleProfileService_4.getBleProfile() != null) {
//            if (BleProfileService_4.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                if (!useDevice.equals("")) {
//                    useDevice += ",";
//                }
//                useDevice += "다리1";
//            }
//        }
//        if (BleProfileService_5.getBleProfile() != null) {
//            if (BleProfileService_5.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                if (!useDevice.equals("")) {
//                    useDevice += ",";
//                }
//                useDevice += "다리2";
//            }
//        }

        if (startTime != null) {
            String[] ExUseTime = DayExTimer.split(":");
            int ExUseMin = Integer.parseInt(ExUseTime[0]);
            int ExUseSec = Integer.parseInt(ExUseTime[1]);
            String sExUseTime;
            String sExUseSec;
            if(ExUseMin !=0) {
                if(ExUseSec < 30){
                    sExUseSec = "";
                }else{
                    sExUseSec = ".5";
                }

                sExUseTime = ExUseMin+sExUseSec;

                AddInfo task = new AddInfo();
                task.execute("http://20.194.57.6/homet/AddInfo.php", email, nickname, mode, startTime, sExUseTime, useDevice);
                DayExTimer = "21:00";

                Toast.makeText(MainTestActivity.this, "데이터를 저장했습니다.", Toast.LENGTH_SHORT).show();
//                            ExTime.setText("00:00");
            }
        }


//        DayExTimer = "00:00";
        /**버전 2 정지 클릭 시 등록**/
        if(ExTimer!=null) {
            ExTimer.cancel();
        }
    }

    public TimerTask ExtimertaskMaker(){
        TimerTask addTask = new TimerTask() {
            @Override
            public void run() {
                ExTimerSetting();
            }
        };
        return addTask;
    }


    public void ExTimerSetting(){
//        editor.putString("ExTimer","00:00");
//        editor.commit();
//        pref.getString("ExTimer","00:00");
        String[] DayExTimeSplit = DayExTimer.split(":");
        int ExMin = Integer.parseInt(DayExTimeSplit[0]);
        int ExSec = Integer.parseInt(DayExTimeSplit[1]);

        if(ExSec<59){
            ExSec++;
        }else{
            ExSec = 0;
            ExMin++;
        }

        String sExMin;
        String sExSec;
        if(String.valueOf(ExMin).length()==1){
            sExMin = "0" + ExMin;
        }else{
            sExMin = String.valueOf(ExMin);
        }

        if(String.valueOf(ExSec).length()==1){
            sExSec = "0"+ExSec;
        }else{
            sExSec = String.valueOf(ExSec);
        }

        DayExTimer = sExMin+":"+sExSec;
        Log.d("ExTimer",DayExTimer);

    }


    class AddInfo extends AsyncTask<String, Void, String> {


        String Respon;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



            try {
                JSONObject obj = new JSONObject(result);
                String sRES = obj.getString("Response");
                Respon=sRES;

                if(Respon.equals("Success")){


                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            }



        }

        @Override
        protected String doInBackground(String... params) {

            String email  = (String)params[1];
            String nickname  = (String)params[2];
            String mode = (String) params[3];
            String startTime  = (String)params[4];
            String useTime  = (String)params[5];
            String useDevice  = (String)params[6];


            String postParameters = "email=" + email +"&"+ "nickname=" + nickname+"&"+ "mode=" + mode+"&"+ "startTime=" + startTime+"&"+ "useTime=" + useTime+"&"+ "useDevice=" + useDevice;


            try {

                URL url = new URL("http://20.194.57.6/homet/AddInfo.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();


                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {



                return new String("Error: " + e.getMessage());
            }

        }
    }

    Intent SttIntent;
    SpeechRecognizer mRecognizer;
    TextToSpeech tts;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //MODE
        if (requestCode == 101) {
            if (resultCode == 1) {
                SharedPreferences pref =getApplication().getSharedPreferences("info",MODE_PRIVATE);
                if(data!=null) {
                    if(data.getStringExtra("mode").equals("M")) {
                        ExMode.setText("근육");
                        if(UARTManager.getManager()!=null){
                            UARTManager.getManager().send("FF000000FE");
                        }
                        if(UARTManager2.getManager()!=null){
                            UARTManager2.getManager().send("FF000000FE");
                        }
                        if(UARTManager3.getManager()!=null){
                            UARTManager3.getManager().send("FF000000FE");
                        }
                        if(UARTManager4.getManager()!=null){
                            UARTManager4.getManager().send("FF000000FE");
                        }
                        if(UARTManager5.getManager()!=null){
                            UARTManager5.getManager().send("FF000000FE");
                        }


                    }else if(data.getStringExtra("mode").equals("S")){
                        ExMode.setText("다이어트");
                        if(UARTManager.getManager()!=null){
                            UARTManager.getManager().send("FF000001FE");
                        }
                        if(UARTManager2.getManager()!=null){
                            UARTManager2.getManager().send("FF000001FE");
                        }
                        if(UARTManager3.getManager()!=null){
                            UARTManager3.getManager().send("FF000001FE");
                        }
                        if(UARTManager4.getManager()!=null){
                            UARTManager4.getManager().send("FF000001FE");
                        }
                        if(UARTManager5.getManager()!=null){
                            UARTManager5.getManager().send("FF000001FE");
                        }



                    }
                }
                if(!ExTime.getText().toString().equals("00:00") && !ExTime.getText().toString().equals("21:00") && !ExTime.getText().toString().equals("26:00") && !ExTime.getText().toString().equals("31:00")){
                    if(!pref.getString("mode","M").equals(data.getStringExtra("mode"))) {

//                    long now = System.currentTimeMillis();
//                    Date date = new Date(now);
//                    SimpleDateFormat sdfNOW = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String DATE = sdfNOW.format(date);
                        String email = pref.getString("email", "00");
                        String nickname = pref.getString("nickname", "00");
                        String mode = pref.getString("mode", "M");

                        editor.putString("mode",data.getStringExtra("mode"));
                        editor.commit();
//                    startTime = DATE.substring(11, 16);

//                    String[] useT = pref.getString("ExTime","00").split(":");
////                    int usetI = Integer.parseInt(useT[0]);
//                    String useTime = String.valueOf(usetI);

//                    String useDevice = "";
//                    if (BleProfileService.getBleProfile() != null) {
//                        int a = BleProfileService.getBleProfile().getConnectionState();
//                        if (BleProfileService.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                            useDevice += "배";
//                        }
//                    }
//                    if (BleProfileService_2.getBleProfile() != null) {
//                        if (BleProfileService_2.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                            if (!useDevice.equals("")) {
//                                useDevice += ",";
//                            }
//                            useDevice += "팔1";
//                        }
//                    }
//                    if (BleProfileService_3.getBleProfile() != null) {
//                        if (BleProfileService_3.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                            if (!useDevice.equals("")) {
//                                useDevice += ",";
//                            }
//                            useDevice += "팔2";
//                        }
//                    }
//                    if (BleProfileService_4.getBleProfile() != null) {
//                        if (BleProfileService_4.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                            if (!useDevice.equals("")) {
//                                useDevice += ",";
//                            }
//                            useDevice += "다리1";
//                        }
//                    }
//                    if (BleProfileService_5.getBleProfile() != null) {
//                        if (BleProfileService_5.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                            if (!useDevice.equals("")) {
//                                useDevice += ",";
//                            }
//                            useDevice += "다리2";
//                        }
//                    }


                        if(!useDevice.equals("")) {
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("mode", data.getStringExtra("mode"));
                            editor.commit();


                            if (startTime != null) {
                                String[] ExUseTime = DayExTimer.split(":");
                                int ExUseMin = Integer.parseInt(ExUseTime[0]);
                                int ExUseSec = Integer.parseInt(ExUseTime[1]);
                                String sExUseTime;
                                String sExUseSec;
                                if(ExUseMin !=0) {
                                    if (ExUseSec < 30) {
                                        sExUseSec = "";
                                    } else {
                                        sExUseSec = ".5";
                                    }

                                    sExUseTime = ExUseMin + sExUseSec;


                                    AddInfo task = new AddInfo();
                                    task.execute("http://20.194.57.6/homet/AddInfo.php", email, nickname, mode, startTime, sExUseTime, useDevice);
                                    DayExTimer = "21:00";

                                    Toast.makeText(MainTestActivity.this, "데이터를 저장했습니다.", Toast.LENGTH_SHORT).show();
//                            ExTime.setText("00:00");
                                }
                            }

                        }else{
                            Toast.makeText(MainTestActivity.this, "연결된 기기가 없어, 데이터를 저장하지 못했습니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        }
        //TIME
        else if(requestCode == 1010 && resultCode ==1010){
            if (i == 1) {
//                UARTManager.getManager().send("FF0B0001FE");
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF1A0000FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF1A0000FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF1A0000FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF1A0000FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF1A0000FE");
                }
                Toast.makeText(MainTestActivity.this, "정지", Toast.LENGTH_SHORT).show();
                StopTimer();
                TimerSetProtocol();
                i = 2;
            }

            String time = data.getStringExtra("exTime");

            SharedPreferences pref = getApplication().getSharedPreferences("info",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("ExTimeToSend",time);
            editor.commit();

            if(time.length() ==1){
                time = "0" + time;
            }



            ExTime.setText(time+":00");
            StopTimer();
            i = 0;

        }
        //Drop
        else if(requestCode ==81){
            if(resultCode == 81){
                i=1;
                StartTimer();
            }else{

                StopTimer();
                String email = pref.getString("email", "00");
                String nickname = pref.getString("nickname", "00");
                String mode = pref.getString("mode", "M");


                if(!useDevice.equals("")) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("mode", pref.getString("mode","M"));
                    editor.commit();



                    if(startTime!=null) {
                        String[] ExUseTime = DayExTimer.split(":");
                        int ExUseMin = Integer.parseInt(ExUseTime[0]);
                        int ExUseSec = Integer.parseInt(ExUseTime[1]);
                        String sExUseTime;
                        String sExUseSec;
                        if (ExUseMin != 0) {
                            if (ExUseSec < 30) {
                                sExUseSec = "";
                            } else {
                                sExUseSec = ".5";
                            }

                            sExUseTime = ExUseMin + sExUseSec;

                            AddInfo task = new AddInfo();
                            task.execute("http://20.194.57.6/homet/AddInfo.php", email, nickname, mode, startTime, sExUseTime, useDevice);
                            Toast.makeText(MainTestActivity.this, "데이터를 저장했습니다.", Toast.LENGTH_SHORT).show();
//                            ExTime.setText("00:00");
                            ExTime.setText("21:00");
//                        UARTManager.getManager().setA();
                            timer = null;

                        }
                    }

                }else{
                    Toast.makeText(MainTestActivity.this, "연결된 기기가 없어, 데이터를 저장하지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else if(requestCode ==103){
            if(resultCode ==1) {
                i = 0;
                SharedPreferences.Editor editor = pref.edit();
                if (timer != null) {
                    timer.cancel();
                }
                editor.remove("Device1Setting");
                editor.remove("Device2Setting");
                editor.remove("Device4Setting");

//                editor.remove("mode");
                editor.remove("ExTime");
                editor.remove("ExTimeToSend");
                editor.commit();
//
//
                if (UARTManager.getManager() != null) {
                    UARTManager.getManager().send("FF110001FE");
                    UARTManager.getManager().send("FF1A0000FE");
//                UARTManager.getManager().send("FF3101000001FE");
                }
//
                if (UARTManager2.getManager() != null) {
                    UARTManager2.getManager().send("FF110001FE");
                    UARTManager2.getManager().send("FF1A0000FE");
                }

                if (UARTManager3.getManager() != null) {
                    UARTManager3.getManager().send("FF110001FE");
                    UARTManager3.getManager().send("FF1A0000FE");
                }

                if (UARTManager4.getManager() != null) {
                    UARTManager4.getManager().send("FF110001FE");
                    UARTManager4.getManager().send("FF1A0000FE");
                }

                if (UARTManager5.getManager() != null) {
                    UARTManager5.getManager().send("FF110001FE");
                    UARTManager5.getManager().send("FF1A0000FE");
                }

                ExTime.setText("21:00");
            }else{

            }
        }


        if(resultCode==1001) {   // 시작
//            btnStart.setText("■");
            if(i!=1) {
                play.performClick();
            }else{
                Toast.makeText(MainTestActivity.this, "이미 운동중입니다.", Toast.LENGTH_SHORT).show();
            }
        }else if(resultCode == 1002){ // 정지
//            btnStart.setText("▶");
            if(i==1) {
                play.performClick();
            }else{
                Toast.makeText(MainTestActivity.this, "아직 운동이 시작되지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        }else if(resultCode == 1003){  //플러스
//            btnPlus.performClick();
            plusAll.performClick();
        }else if(resultCode == 1004){ //마이너스
//            btnMinus.performClick();
            minusAll.performClick();
        }else if(resultCode==1011){
            tts.speak("다시 말씀해주세요", TextToSpeech.QUEUE_FLUSH, null);
//            Toast.makeText(Main2Activity.this, "다시 말씀해주세요", Toast.LENGTH_SHORT).show();
        }
    }
    public void SetSST(){

    }
    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
//            Toast.makeText(getApplicationContext(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {
//            txtSystem.setText("인식 시작.");
//            Toast.makeText(Main2Activity.this, "인식시작", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRmsChanged(float rmsdB) {
//            txtSystem.setText("Rms Changed");
//            Toast.makeText(Main2Activity.this, "Rms Changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
//            txtSystem.setText("onBufferReceived..........."+"\r\n"+txtSystem.getText());
        }

        @Override
        public void onEndOfSpeech() {
//            txtSystem.setText("인식끝");
//            Toast.makeText(Main2Activity.this, "인식끝", Toast.LENGTH_SHORT).show();
            btnSSTStart.performClick();
        }

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    btnSSTStart.performClick();
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    btnSSTStart.performClick();
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    btnSSTStart.performClick();
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }
        }

        @Override
        public void onResults(Bundle results) {
            String key= "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult =results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
//            txtInMsg.setText(rs[0]+"\r\n"+txtInMsg.getText());
//            Toast.makeText(Main2Activity.this, rs[0], Toast.LENGTH_SHORT).show();
            FuncVoiceOrderCheck(rs[0]);
            mRecognizer.startListening(SttIntent);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };


    Button btnSSTStart;
    @Override
    protected void onResume(){
        super.onResume();
        setBle();



        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });



//        AudioManager audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
//        audio.setStreamVolume(AudioManager.STREAM_MUSIC, 0,  AudioManager.FLAG_SHOW_UI);

        btnSSTStart = findViewById(R.id.btnSSTStart);
        btnSSTStart.setOnClickListener(n->{

            AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                amanager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
//            amanager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0);
//            amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
//            amanager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
//            amanager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
            } else {
                amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
//            amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
//            amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
//            amanager.setStreamMute(AudioManager.STREAM_RING, true);
//            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
            }


            if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager
                    .PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainTestActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},1);
            }else{
                try{
                    mRecognizer.startListening(SttIntent);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.tester).setOnClickListener(n->{
            btnSSTStart.performClick();
        });

        SttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        SttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getApplicationContext().getPackageName());
        SttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
        mRecognizer  = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);





//    txtInMsg = findViewById(R.id.txtInMsg);
//    txtSystem = findViewById(R.id.txtSystem);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                txtSystem.setText("어플 실행됨 자동 실행" + "\r\n" + txtSystem.getText());
                btnSSTStart.performClick();
                /**음성인식**/
            }
        },1000);


    }
    private void FuncVoiceOrderCheck(String VoiceMsg){
        if(VoiceMsg.length()<1)return;

        VoiceMsg=VoiceMsg.replace(" ","");//공백제거

//        if(VoiceMsg.indexOf("카카오톡")>-1 || VoiceMsg.indexOf("카톡")>-1){
//            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.kakao.talk");
//            startActivity(launchIntent);
//            onDestroy();
//        }
        Log.d("Voice", VoiceMsg);

        if(VoiceMsg.contains("빅스비")) {

            Intent intent = new Intent(MainTestActivity.this, DialogSpeach.class);
            startActivityForResult(intent, 1);
//            tts.speak("하이 빅스비", TextToSpeech.QUEUE_FLUSH, null);
        }


    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainTestActivity.this);
        builder.setTitle("뒤로가기");
        builder.setMessage("뒤로가기 선택 시 진행 중인 운동은 종료됩니다\n\n  *운동 데이터는 저장됩니다*");
        builder.setCancelable(true);
        builder.setPositiveButton("뒤로가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                MainTestActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


}
