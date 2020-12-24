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
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.copyhomet.Bluetooth.BleConnectionActivity;
import com.example.copyhomet.Bluetooth.Bluetooth1.BLE.BleProfileService;
import com.example.copyhomet.Bluetooth.Bluetooth1.BLE.DialogDevice1Droped;
import com.example.copyhomet.Bluetooth.Bluetooth1.UART.UARTManager;
import com.example.copyhomet.Bluetooth.Bluetooth2.BLE.BleProfileService_2;
import com.example.copyhomet.Bluetooth.Bluetooth2.BLE.DialogDevice2Droped;
import com.example.copyhomet.Bluetooth.Bluetooth2.UART.UARTManager2;
import com.example.copyhomet.Bluetooth.Bluetooth3.BLE.BleProfileService_3;
import com.example.copyhomet.Bluetooth.Bluetooth3.BLE.DialogDevice3Droped;
import com.example.copyhomet.Bluetooth.Bluetooth3.UART.UARTManager3;
import com.example.copyhomet.Bluetooth.Bluetooth4.BLE.BleProfileService_4;
import com.example.copyhomet.Bluetooth.Bluetooth4.BLE.DialogDevice4Droped;
import com.example.copyhomet.Bluetooth.Bluetooth4.UART.UARTManager4;
import com.example.copyhomet.Bluetooth.Bluetooth5.BLE.BleProfileService_5;
import com.example.copyhomet.Bluetooth.Bluetooth5.BLE.DialogDevice5Droped;
import com.example.copyhomet.Bluetooth.Bluetooth5.UART.UARTManager5;
import com.example.copyhomet.Bluetooth.DialogBleToMain;
import com.example.copyhomet.DialogExTimeSet;
import com.example.copyhomet.DialogSpeach;
import com.example.copyhomet.ForecdTerminationService;
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

public class MainActivity extends AppCompatActivity {

    Button ExMode;
    TextView ExTime;
    ConstraintLayout device1,device2,device3,device4,device5;
    TextView count1,count2,count3,count4,count5;
    Button plus1,plus2,plus3,plus4,plus5,plusAll;
    Button minus1,minus2,minus3,minus4,minus5,minusAll;
    Button play;
    Button Datas;
    Timer timer,ExTimer;

    String sec,min;
    String startTime;
    int i =0;
    TextView tvEmptyDevice;

    LinearLayout TimeLayout;
    static MainActivity mainActivity;
    static MainActivity mainActivity2;


    TextView tvWarmUp,tv1,tv2,tv3,tv4,tv5,tv6,tvCoolDown;

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    Button btnInit;

    int ExState = 0; // 0 = WarmUp ,123456, 7= CoolDown
    int ExDo = 0;

    String timeMode;

    TextView moreFunction;
    private DrawerLayout drawerLayout;
    private View drawerView;

    LinearLayout Progress_Ex_Linear;

    String DayExTimer;
    TextView Tester;

    ImageView btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_constraint);

        getSupportActionBar().hide();
        startService(new Intent(this, ForecdTerminationService.class));

        SetSST();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());



        if(UARTManager.getManager()!=null){
            UARTManager.getManager().setAppKilled();
        }
        if(UARTManager2.getManager()!=null){
            UARTManager2.getManager().setAppKilled();
        }
        if(UARTManager3.getManager()!=null){
            UARTManager3.getManager().setAppKilled();
        }
        if(UARTManager4.getManager()!=null){
            UARTManager4.getManager().setAppKilled();
        }
        if(UARTManager5.getManager()!=null){
            UARTManager5.getManager().setAppKilled();
        }

        pref = getApplication().getSharedPreferences("info",MODE_PRIVATE);
        editor = pref.edit();

                    ExState = 0;
            editor.putString("ExState","0");
            editor.commit();
//        getSupportActionBar().hide();
        moreFunction = findViewById(R.id.moreFunction);
        moreFunction.setOnClickListener(n->{
            Intent intent = new Intent(MainActivity.this,DialogMoreFunction.class);
            startActivity(intent);
        });


        tvEmptyDevice = findViewById(R.id.tvEmptyDevice);
        mainActivity = this;
        mainActivity2 = this;
        init();

        SharedPreferences pref = getApplication().getSharedPreferences("info",MODE_PRIVATE);
        if(pref.getString("mode","M").equals("M")) {
            ExMode.setText("근육");
        }else if(pref.getString("mode","M").equals("S")){
            ExMode.setText("다이어트");
        }else{
            ExMode.setText("운동 모드");
        }

        StateProgress();
        ProgressBarClicked();


        btnInit = findViewById(R.id.btnInitAll);
        btnInit.setOnClickListener(n->{

            /**강제 종료 테스트**/
//            Button aa = null;
//            aa.setOnClickListener(na->{
//                Toast.makeText(mainActivity, "hi", Toast.LENGTH_SHORT).show();
//            });
            Intent intent = new Intent(MainActivity.this,DialogReset.class);
            startActivityForResult(intent,103);
        });


        SetDrawer();


        DayExTimer = "00:00";
        editor.putString("ExTimer","00:00");
        editor.commit();


//        Tester=findViewById(R.id.Tester);
//        Tester.setOnClickListener(n->{
//            Device1Drop();
//        });

        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(n->{
            drawerLayout.openDrawer(drawerView);
        });

    }

    int warmTest = 0;

    public void StateProgress(){
        tvWarmUp = findViewById(R.id.tvWarmUp);
        tvCoolDown = findViewById(R.id.tvCoolDown);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);

        timeMode = pref.getString("ExTimeMode","26");
        if(timeMode.equals("21")){
            tv5.setVisibility(GONE);
            tv6.setVisibility(GONE);
        }else if(timeMode.equals("26")){
            tv5.setVisibility(View.VISIBLE);
            tv6.setVisibility(GONE);
        }else{
            tv5.setVisibility(View.VISIBLE);
            tv6.setVisibility(View.VISIBLE);
        }
        tvWarmUp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                UARTManager.getManager().send("FF120002FE");



                return false;
            }
        });

    }
public void getWarmUp(String Data){

//        Data = Data.substring(5).replace("-","");  // UARTManager 에서 데이터가 넘어올시
        if(Data.equals("FF120000FE")){
//            tv1.setBackgroundColor(Color.parseColor("#000000"));
                ExState = 0;
                editor.putString("ExState", "0");
                editor.commit();
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120000FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120000FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120000FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120000FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120000FE");
                }

//            if (timer == null) {
//                    long now = System.currentTimeMillis();
//                    Date date = new Date(now);
//                    SimpleDateFormat sdfNOW = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String DATE = sdfNOW.format(date);
//
//                    startTime = DATE.substring(11, 16);
//                    ExState = 0;
////                    StartTimer();
//                }

        }else if(Data.equals("FF120001FE")){
//            if(i==1) {
//                StartTimer();
                tvWarmUp.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tvWarmUp.setTextColor(Color.parseColor("#ffffff"));
                tv1.setTextColor(Color.parseColor("#ffffff"));
                tv1.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tv1.setTextColor(Color.parseColor("#ffffff"));
                ExState = 1;
            if(UARTManager.getManager()!=null) {
                UARTManager.getManager().send("FF120001FE");
            }else if(UARTManager2.getManager()!=null) {
                UARTManager2.getManager().send("FF120001FE");
            }else if(UARTManager3.getManager()!=null) {
                UARTManager3.getManager().send("FF120001FE");
            }else if(UARTManager4.getManager()!=null) {
                UARTManager4.getManager().send("FF120001FE");
            }else if(UARTManager5.getManager()!=null) {
                UARTManager5.getManager().send("FF120001FE");
            }
//                PlayClick();
                editor.putString("ExState","1");
                editor.commit();

//                if(timer==null) {
////                    StartTimer();
//                    i=1;
//                    long now = System.currentTimeMillis();
//                    Date date = new Date(now);
//                    SimpleDateFormat sdfNOW = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String DATE = sdfNOW.format(date);
//
//                    startTime = DATE.substring(11, 16);
//                }
//            }
        }else if(Data.equals("FF120002FE")){
            tvWarmUp.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tvWarmUp.setTextColor(Color.parseColor("#ffffff"));
            tv1.setTextColor(Color.parseColor("#ffffff"));
            tv1.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv1.setTextColor(Color.parseColor("#ffffff"));
            ExState = 2;
            editor.putString("ExState","2");
            editor.commit();
            if(UARTManager.getManager()!=null) {
                UARTManager.getManager().send("FF120002FE");
            }else if(UARTManager2.getManager()!=null) {
                UARTManager2.getManager().send("FF120002FE");
            }else if(UARTManager3.getManager()!=null) {
                UARTManager3.getManager().send("FF120002FE");
            }else if(UARTManager4.getManager()!=null) {
                UARTManager4.getManager().send("FF120002FE");
            }else if(UARTManager5.getManager()!=null) {
                UARTManager5.getManager().send("FF120002FE");
            }
//            PlayClick();
//            if(timer==null){
//                StartTimer();
//                i=1;
//                long now = System.currentTimeMillis();
//                Date date = new Date(now);
//                SimpleDateFormat sdfNOW = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String DATE = sdfNOW.format(date);
//
//                startTime = DATE.substring(11, 16);
//            }
        }else if(Data.equals("FF120003FE")){
            tvWarmUp.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tvWarmUp.setTextColor(Color.parseColor("#ffffff"));
            tv1.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv1.setTextColor(Color.parseColor("#ffffff"));
            tv2.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv2.setTextColor(Color.parseColor("#ffffff"));
            ExState = 3;
            if(UARTManager.getManager()!=null) {
                UARTManager.getManager().send("FF120003FE");
            }else if(UARTManager2.getManager()!=null) {
                UARTManager2.getManager().send("FF120003FE");
            }else if(UARTManager3.getManager()!=null) {
                UARTManager3.getManager().send("FF120003FE");
            }else if(UARTManager4.getManager()!=null) {
                UARTManager4.getManager().send("FF120003FE");
            }else if(UARTManager5.getManager()!=null) {
                UARTManager5.getManager().send("FF120003FE");
            }
            editor.putString("ExState","3");
            editor.commit();
//            PlayClick();
//            if(timer==null){
//                StartTimer();
//                i=1;
//                long now = System.currentTimeMillis();
//                Date date = new Date(now);
//                SimpleDateFormat sdfNOW = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String DATE = sdfNOW.format(date);
//
//                startTime = DATE.substring(11, 16);
//            }
        }else if(Data.equals("FF120004FE")){
            tvWarmUp.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tvWarmUp.setTextColor(Color.parseColor("#ffffff"));
            tv1.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv1.setTextColor(Color.parseColor("#ffffff"));
            tv2.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv2.setTextColor(Color.parseColor("#ffffff"));
            tv3.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv3.setTextColor(Color.parseColor("#ffffff"));
            ExState = 4;
            if(UARTManager.getManager()!=null) {
                UARTManager.getManager().send("FF120004FE");
            }else if(UARTManager2.getManager()!=null) {
                UARTManager2.getManager().send("FF120004FE");
            }else if(UARTManager3.getManager()!=null) {
                UARTManager3.getManager().send("FF120004FE");
            }else if(UARTManager4.getManager()!=null) {
                UARTManager4.getManager().send("FF120004FE");
            }else if(UARTManager5.getManager()!=null) {
                UARTManager5.getManager().send("FF120004FE");
            }
            editor.putString("ExState","4");
            editor.commit();
//            PlayClick();
//            if(timer==null){
//                StartTimer();
//                i=1;
//                long now = System.currentTimeMillis();
//                Date date = new Date(now);
//                SimpleDateFormat sdfNOW = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String DATE = sdfNOW.format(date);
//
//                startTime = DATE.substring(11, 16);
//            }
        }else if(Data.equals("FF120005FE")){
            tvWarmUp.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tvWarmUp.setTextColor(Color.parseColor("#ffffff"));
            tv1.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv1.setTextColor(Color.parseColor("#ffffff"));
            tv2.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv2.setTextColor(Color.parseColor("#ffffff"));
            tv3.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv3.setTextColor(Color.parseColor("#ffffff"));
            tv4.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv4.setTextColor(Color.parseColor("#ffffff"));
            ExState = 5;
            if(UARTManager.getManager()!=null) {
                UARTManager.getManager().send("FF120005FE");
            }else if(UARTManager2.getManager()!=null) {
                UARTManager2.getManager().send("FF120005FE");
            }else if(UARTManager3.getManager()!=null) {
                UARTManager3.getManager().send("FF120005FE");
            }else if(UARTManager4.getManager()!=null) {
                UARTManager4.getManager().send("FF120005FE");
            }else if(UARTManager5.getManager()!=null) {
                UARTManager5.getManager().send("FF120005FE");
            }
            editor.putString("ExState","5");
            editor.commit();
//            PlayClick();
//            if(timer==null){
//                StartTimer();
//                i=1;
//                long now = System.currentTimeMillis();
//                Date date = new Date(now);
//                SimpleDateFormat sdfNOW = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String DATE = sdfNOW.format(date);
//
//                startTime = DATE.substring(11, 16);
//            }
        }else if(Data.equals("FF120006FE")){
            tvWarmUp.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tvWarmUp.setTextColor(Color.parseColor("#ffffff"));
            tv1.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv1.setTextColor(Color.parseColor("#ffffff"));
            tv2.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv2.setTextColor(Color.parseColor("#ffffff"));
            tv3.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv3.setTextColor(Color.parseColor("#ffffff"));
            tv4.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv4.setTextColor(Color.parseColor("#ffffff"));
            tv5.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv5.setTextColor(Color.parseColor("#ffffff"));
            ExState = 6;
            if(UARTManager.getManager()!=null) {
                UARTManager.getManager().send("FF120006FE");
            }else if(UARTManager2.getManager()!=null) {
                UARTManager2.getManager().send("FF120006FE");
            }else if(UARTManager3.getManager()!=null) {
                UARTManager3.getManager().send("FF120006FE");
            }else if(UARTManager4.getManager()!=null) {
                UARTManager4.getManager().send("FF120006FE");
            }else if(UARTManager5.getManager()!=null) {
                UARTManager5.getManager().send("FF120006FE");
            }
            editor.putString("ExState","6");
            editor.commit();
//            PlayClick();
//            if(timer==null){
//                StartTimer();
//                i=1;
//                long now = System.currentTimeMillis();
//                Date date = new Date(now);
//                SimpleDateFormat sdfNOW = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String DATE = sdfNOW.format(date);
//
//                startTime = DATE.substring(11, 16);
//            }
        }else if(Data.equals("FF120007FE")){
            tvWarmUp.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tvWarmUp.setTextColor(Color.parseColor("#ffffff"));
            tv1.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv1.setTextColor(Color.parseColor("#ffffff"));
            tv2.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv2.setTextColor(Color.parseColor("#ffffff"));
            tv3.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv3.setTextColor(Color.parseColor("#ffffff"));
            tv4.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv4.setTextColor(Color.parseColor("#ffffff"));
            tv5.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv5.setTextColor(Color.parseColor("#ffffff"));
            tv6.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tv6.setTextColor(Color.parseColor("#ffffff"));
//            ExTime.setText("00:00");
//            timer = null;
            ExState = 7;
//            PlayClick();

//            if(UARTManager.getManager()!=null) {
//                UARTManager.getManager().send("FF120007FE");
//            }else if(UARTManager2.getManager()!=null) {
//                UARTManager2.getManager().send("FF120007FE");
//            }else if(UARTManager3.getManager()!=null) {
//                UARTManager3.getManager().send("FF120007FE");
//            }else if(UARTManager4.getManager()!=null) {
//                UARTManager4.getManager().send("FF120007FE");
//            }else if(UARTManager5.getManager()!=null) {
//                UARTManager5.getManager().send("FF120007FE");
//            }
            editor.putString("ExState","7");
            editor.commit();
            i=1;
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdfNOW = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String DATE = sdfNOW.format(date);

            startTime = DATE.substring(11, 16);

        }else{

        }


}
    public void init(){
        ExMode = findViewById(R.id.ExMode);
        ExMode.setOnClickListener(n->{
            Intent intent = new Intent(MainActivity.this, ModeDialog.class);
            startActivityForResult(intent,101);
        });
        ExTime = findViewById(R.id.ExTime);
        ExTime.setText("26:00");
        editor.putString("ExTimeMode","26");
        editor.commit();
        //오늘 하루 지나면 서버에 전송
        SharedPreferences pref = getApplication().getSharedPreferences("info",MODE_PRIVATE);

//        ExTime.setText(pref.getString("ExTime","25:00"));
//        ExTime.setOnClickListener(n->{
//            Intent intent = new Intent(MainActivity.this,DialogExTimeSet.class);
//            startActivityForResult(intent,1010);
//        });
//        TextView textview2 = findViewById(R.id.textView2);
//        textview2.setOnClickListener(n->{
//            Intent intent = new Intent(MainActivity.this,DialogExTimeSet.class);
//            startActivityForResult(intent,1010);
//        });

        TimeLayout = findViewById(R.id.TimeLayout);
        TimeLayout.setOnClickListener(n->{
            Intent intent = new Intent(MainActivity.this, DialogExTimeSet.class);
            startActivityForResult(intent,1010);
        });


        setBle();

//        device5 = findViewById(R.id.Device5Layout);
//        if(BleProfileService_5.getBleProfile()!=null) {
//            if (BleProfileService_5.getBleProfile().getConnectionState() != 2) {
//                device5.setVisibility(View.GONE);
//
//            }
//        }else{
//            device5.setVisibility(View.GONE);
//
//        }


        count1 = findViewById(R.id.edtCount1);
//        count1.setText(pref.getString("Device1Setting","0"));
        count2= findViewById(R.id.edtCount2);
//        count2.setText(pref.getString("Device2Setting","0"));
//        count3 = findViewById(R.id.edtCount3);
//        count3.setText(pref.getString("Device3Setting","15"));
        count4 = findViewById(R.id.edtCount4);
//        count4.setText(pref.getString("Device4Setting","0"));
//        count5 =findViewById(R.id.edtCount5);
//        count5.setText(pref.getString("Device5Setting","15"));

        plus1 =findViewById(R.id.plus1);
        plus2 =findViewById(R.id.plus2);
//        plus3 = findViewById(R.id.plus3);
        plus4 = findViewById(R.id.plus4);
//        plus5 = findViewById(R.id.plus5);
        plusAll =findViewById(R.id.plusAll);

        minus1 =findViewById(R.id.minus1);
        minus2 = findViewById(R.id.minus2);
//        minus3 = findViewById(R.id.minus3);
        minus4 = findViewById(R.id.minus4);
//        minus5 = findViewById(R.id.minus5);
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
    public void setExState(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ExDo == 0) {
                    if (ExState == 0) {
                        tvWarmUp.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                        tvWarmUp.setTextColor(Color.parseColor("#ffffff"));
                    } else if (ExState == 1) {
                        tv1.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                        tv1.setTextColor(Color.parseColor("#ffffff"));
                    } else if (ExState == 2) {
                        tv2.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                        tv2.setTextColor(Color.parseColor("#ffffff"));
                    } else if (ExState == 3) {
                        tv3.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                        tv3.setTextColor(Color.parseColor("#ffffff"));
                    } else if (ExState == 4) {
                        tv4.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                        tv4.setTextColor(Color.parseColor("#ffffff"));
                    } else if (ExState == 5) {
                        tv5.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                        tv5.setTextColor(Color.parseColor("#ffffff"));
                    } else if (ExState == 6) {
                        tv6.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                        tv6.setTextColor(Color.parseColor("#ffffff"));
                    } else if (ExState == 7) {
//                        timer.cancel();
                        tvCoolDown.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                        tvCoolDown.setTextColor(Color.parseColor("#ffffff"));
                    }
            ExDo =1;
        }else{
            if(ExState == 0){
                tvWarmUp.setBackgroundColor(Color.parseColor("#000000"));
                tvWarmUp.setTextColor(Color.parseColor("#ffffff"));
            }else if(ExState ==1){
                tv1.setBackgroundColor(Color.parseColor("#000000"));
                tv1.setTextColor(Color.parseColor("#ffffff"));
            }else if(ExState ==2){
                tv2.setBackgroundColor(Color.parseColor("#000000"));
                tv2.setTextColor(Color.parseColor("#ffffff"));
            }else if(ExState ==3){
                tv3.setBackgroundColor(Color.parseColor("#000000"));
                tv3.setTextColor(Color.parseColor("#ffffff"));
            }else if(ExState ==4){
                tv4.setBackgroundColor(Color.parseColor("#000000"));
                tv4.setTextColor(Color.parseColor("#ffffff"));
            }else if(ExState ==5){
                tv5.setBackgroundColor(Color.parseColor("#000000"));
                tv5.setTextColor(Color.parseColor("#ffffff"));
            }else if(ExState ==6){
                tv6.setBackgroundColor(Color.parseColor("#000000"));
                tv6.setTextColor(Color.parseColor("#ffffff"));
            }else if(ExState ==7){
                tvCoolDown.setBackgroundColor(Color.parseColor("#000000"));
                tvCoolDown.setTextColor(Color.parseColor("#ffffff"));
            }
            ExDo=0;
        }




                SetProgerssBar("123");


            }
        });
    }



    public void SetProgerssBar(String State){

        if(timeMode.equals("21")) {
            if(min.equals("20")&& sec.equals("30")&&ExState ==0){
                getWarmUp("FF120001FE");
                tvWarmUp.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tvWarmUp.setTextColor(Color.parseColor("#ffffff"));
                //Send BlockData
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120001FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120001FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120001FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120001FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120001FE");
                }

                ExState = 1;
                editor.putString("ExState","1");
                editor.commit();
            }
            else if (min.equals("15")&&sec.equals("30")&&ExState ==1) {
//                        if (Integer.parseInt(min)<14&&ExState ==1) {
                getWarmUp("FF120002FE");
                tv1.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tv1.setTextColor(Color.parseColor("#ffffff"));
                //Send BlockData
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120002FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120002FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120002FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120002FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120002FE");
                }

                ExState = 2;
                editor.putString("ExState","2");
                editor.commit();
            } else if (min.equals("10")&&sec.equals("30")&&ExState ==2) {
                getWarmUp("FF120003FE");
                tv2.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tv2.setTextColor(Color.parseColor("#ffffff"));
                //Send BlockData
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120003FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120003FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120003FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120003FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120003FE");
                }

                ExState = 3;
                editor.putString("ExState","3");
                editor.commit();
            } else if (min.equals("5")&&sec.equals("30")&&ExState ==3) {
                getWarmUp("FF120004FE");
                tv3.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tv3.setTextColor(Color.parseColor("#ffffff"));
                //Send BlockData
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120004FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120004FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120004FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120004FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120004FE");
                }

                ExState = 4;
                editor.putString("ExState","4");
                editor.commit();
            } else if (min.equals("00")&&sec.equals("30")&&ExState ==4) {
                getWarmUp("FF120007FE");
                tv4.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tv4.setTextColor(Color.parseColor("#ffffff"));
                //Send BlockData
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120007FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120007FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120007FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120007FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120007FE");
                }
                //시간 종료 프로토콜 전송
                ExState = 7;
                editor.putString("ExState","7");
                editor.commit();
            } else if (min.equals("00")&&sec.equals("00")&&ExState ==7) {
                getWarmUp("FF120007FE");
                tvCoolDown.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tvCoolDown.setTextColor(Color.parseColor("#ffffff"));
                //Send BlockData
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF130001FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF130001FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF130001FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF130001FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF130001FE");
                }

                StopTimer();
                //시간 종료 프로토콜 전송
                ExState = 7;
                editor.putString("ExState","7");
                editor.commit();

            }


        }else if (timeMode.equals("26")){

            if(min.equals("25")&& sec.equals("30")&&ExState ==0){
                getWarmUp("FF120001FE");
                tvWarmUp.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tvWarmUp.setTextColor(Color.parseColor("#ffffff"));
                //Send BlockData
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120001FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120001FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120001FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120001FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120001FE");
                }

                ExState = 1;
                editor.putString("ExState","1");
                editor.commit();
            }else if (min.equals("20")&&sec.equals("30")&&ExState ==1) {
                tv1.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tv1.setTextColor(Color.parseColor("#ffffff"));
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120002FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120002FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120002FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120002FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120002FE");
                }
                ExState = 2;
                editor.putString("ExState","2");
                editor.commit();
            } else if (min.equals("15")&&sec.equals("30")&&ExState ==2) {
                tv2.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tv2.setTextColor(Color.parseColor("#ffffff"));
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120003FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120003FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120003FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120003FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120003FE");
                }
                ExState = 3;
                editor.putString("ExState","3");
                editor.commit();
            } else if (min.equals("10")&&sec.equals("30")&&ExState ==3) {
                tv3.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tv3.setTextColor(Color.parseColor("#ffffff"));
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120004FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120004FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120004FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120004FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120004FE");
                }
                ExState = 4;
                editor.putString("ExState","4");
                editor.commit();
            } else if (min.equals("05")&&sec.equals("30")&&ExState ==4) {
                tv4.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tv4.setTextColor(Color.parseColor("#ffffff"));
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120005FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120005FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120005FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120005FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120005FE");
                }
                ExState = 5;
                editor.putString("ExState","5");
                editor.commit();
            } else if (min.equals("00")&&sec.equals("30")&&ExState ==5) {
                tv5.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tv5.setTextColor(Color.parseColor("#ffffff"));
                //시간 종료 프로토콜 전송
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120007FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120007FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120007FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120007FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120007FE");
                }
                ExState = 7;
                editor.putString("ExState","7");
                editor.commit();
            }else if (min.equals("00")&&sec.equals("00")&&ExState ==7) {
                getWarmUp("FF120007FE");
                tvCoolDown.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tvCoolDown.setTextColor(Color.parseColor("#ffffff"));
                //Send BlockData
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF130001FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF130001FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF130001FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF130001FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF130001FE");
                }

                StopTimer();
                //시간 종료 프로토콜 전송
                ExState = 7;
                editor.putString("ExState","7");
                editor.commit();

            }


        }else if(timeMode.equals("31")){
            if(min.equals("30")&& sec.equals("30")&&ExState ==0) {
                getWarmUp("FF120001FE");
                tvWarmUp.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tvWarmUp.setTextColor(Color.parseColor("#ffffff"));
                //Send BlockData
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120001FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120001FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120001FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120001FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120001FE");
                }

                ExState = 1;
                editor.putString("ExState", "1");
                editor.commit();
            }else if (min.equals("25")&&sec.equals("30")&&ExState ==1) {
                tv1.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tv1.setTextColor(Color.parseColor("#ffffff"));
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120002FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120002FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120002FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120002FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120002FE");
                }
                ExState = 2;
                editor.putString("ExState","2");
                editor.commit();
            } else if (min.equals("20")&&sec.equals("30")&&ExState ==2) {
                tv2.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tv2.setTextColor(Color.parseColor("#ffffff"));
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120003FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120003FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120003FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120003FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120003FE");
                }
                ExState = 3;
                editor.putString("ExState","3");
                editor.commit();
            } else if (min.equals("15")&&sec.equals("30")&&ExState ==3) {
                tv3.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tv3.setTextColor(Color.parseColor("#ffffff"));
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120004FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120004FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120004FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120004FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120004FE");
                }
                ExState = 4;
                editor.putString("ExState","4");
                editor.commit();
            } else if (min.equals("10")&&sec.equals("30")&&ExState ==4) {
                tv4.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tv4.setTextColor(Color.parseColor("#ffffff"));
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120005FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120005FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120005FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120005FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120005FE");
                }
                ExState = 5;
                editor.putString("ExState","5");
                editor.commit();
            } else if (min.equals("05")&&sec.equals("30")&&ExState ==5) {
                tv5.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tv5.setTextColor(Color.parseColor("#ffffff"));
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120006FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120006FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120006FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120006FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120006FE");
                }
                ExState = 6;
                editor.putString("ExState","6");
                editor.commit();
            }else if (min.equals("00")&&sec.equals("30")&&ExState ==6) {
                tv6.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tv6.setTextColor(Color.parseColor("#ffffff"));
                //시간 종료 프로토콜 전송
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF120007FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF120007FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF120007FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF120007FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF120007FE");
                }
                ExState = 7;
                editor.putString("ExState","7");
                editor.commit();
            }else if (min.equals("00")&&sec.equals("00")&&ExState ==7) {
                getWarmUp("FF120007FE");
                tvCoolDown.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                tvCoolDown.setTextColor(Color.parseColor("#ffffff"));
                //Send BlockData
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF130001FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF130001FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF130001FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF130001FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF130001FE");
                }

//                timer.cancel();
                StopTimer();
                //시간 종료 프로토콜 전송
                ExState = 7;
                editor.putString("ExState","7");
                editor.commit();

            }
        }
    }

    public void PlusAllClick(){
        plusAll.performClick();
    }
    public void MinusAllClick(){
        minusAll.performClick();
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
                Toast.makeText(MainActivity.this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
            }
        });
        minusAll.setOnClickListener(n->{
            if(i==1) {
                MinusClick();
            }else{
                Toast.makeText(MainActivity.this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "연결된 기기가 없습니다.", Toast.LENGTH_SHORT).show();
            }

        });
        Datas.setOnClickListener(n->{
            Intent intent = new Intent(MainActivity.this, TotalActivity.class);
            startActivity(intent);
        });
}
    String useDevice = "";
public void PlayClick(){
    if(ExTime.getText().toString().equals("00:00")){
        Toast.makeText(MainActivity.this, "시간을 설정해 주세요.", Toast.LENGTH_SHORT).show();
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
            SendTime();

            Toast.makeText(MainActivity.this, "시작", Toast.LENGTH_SHORT).show();
            //warm up 이 끝나는 프로토콜이 전달 되면
            StartTimer();
//            ExState = 0;
//            editor.putString("ExState","0");
//            editor.commit();
            tvWarmUp.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
            tvWarmUp.setTextColor(Color.parseColor("#ffffff"));
//            UARTManager.getManager().send("FF120000FE");
            if(ExState == 0 ) {
                getWarmUp("FF120000FE");
            }else if(ExState == 1 ){
                getWarmUp("FF120001FE");
            }else if(ExState == 2 ){
                getWarmUp("FF120002FE");
            }else if(ExState == 3 ){
                getWarmUp("FF120003FE");
            }else if(ExState == 4 ){
                getWarmUp("FF120004FE");
            }else if(ExState == 5 ){
                getWarmUp("FF120005FE");
            }else if(ExState == 6 ){
                getWarmUp("FF120006FE");
            }else if(ExState == 7 ){
                getWarmUp("FF120007FE");
            }
            TimerSetProtocol();

            i = 1;


        } else if( i== 1){

            Toast.makeText(MainActivity.this, "정지", Toast.LENGTH_SHORT).show();
            StopTimer();
            TimerSetProtocol();
            i = 2;

        }else if(i ==2){
            Toast.makeText(MainActivity.this, "시작", Toast.LENGTH_SHORT).show();
            if(timer!=null) {
                StartTimer();
            }
            TimerSetProtocol();
            i=1;
        }


    }
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
                    UARTManager.getManager().send("FF0B0000FE");
                }
                if (UARTManager2.getManager() != null) {
                    UARTManager2.getManager().send("FF0B0000FE");

                }
                if (UARTManager3.getManager() != null) {
                    UARTManager3.getManager().send("FF0B0000FE");

                }
                if (UARTManager4.getManager() != null) {
                    UARTManager4.getManager().send("FF0B0000FE");

                }
                if (UARTManager5.getManager() != null) {
                    UARTManager5.getManager().send("FF0B0000FE");

                }
            }else {
                // 시작 0001
                if (UARTManager.getManager() != null) {
                    UARTManager.getManager().send("FF0B0001FE");
                    UARTManager.getManager().send("FF19"+s_bmi +"FE");
                }
                if (UARTManager2.getManager() != null) {
                    UARTManager2.getManager().send("FF0B0001FE");
                    UARTManager2.getManager().send("FF19"+s_bmi +"FE");
                }
                if (UARTManager3.getManager() != null) {
                    UARTManager3.getManager().send("FF0B0001FE");
                    UARTManager3.getManager().send("FF19"+s_bmi +"FE");
                }
                if (UARTManager4.getManager() != null) {
                    UARTManager4.getManager().send("FF0B0001FE");
                    UARTManager4.getManager().send("FF19"+s_bmi +"FE");
                }
                if (UARTManager5.getManager() != null) {
                    UARTManager5.getManager().send("FF0B0001FE");
                    UARTManager5.getManager().send("FF19"+s_bmi +"FE");
                }
            }
}

public void SendTime(){

        String useTime = ExTime.getText().toString();
        String[] USE = useTime.split(":");
        int min = Integer.parseInt(USE[0])*60;
        int sec = Integer.parseInt(USE[1]);
        int time = min + sec;


        try {

            if (UARTManager.getManager() != null) {
                String s_time = Integer.toHexString(time);
                if (s_time.length() < 4) {
                    while (s_time.length() < 4) {
                        s_time = "0" + s_time;
                    }
                }
                UARTManager.getManager().send("FF01" + s_time + "FE");
            }

            if (UARTManager2.getManager() != null) {
                String s_time = Integer.toHexString(time);
                if (s_time.length() < 4) {
                    while (s_time.length() < 4) {
                        s_time = "0" + s_time;
                    }
                }
                UARTManager2.getManager().send("FF01" + s_time + "FE");
            }

            if (UARTManager3.getManager() != null) {
                String s_time = Integer.toHexString(time);
                if (s_time.length() < 4) {
                    while (s_time.length() < 4) {
                        s_time = "0" + s_time;
                    }
                }
                UARTManager3.getManager().send("FF01" + s_time + "FE");
            }

            if (UARTManager4.getManager() != null) {
                String s_time = Integer.toHexString(time);
                if (s_time.length() < 4) {
                    while (s_time.length() < 4) {
                        s_time = "0" + s_time;
                    }
                }
                UARTManager4.getManager().send("FF01" + s_time + "FE");
            }

            if (UARTManager5.getManager() != null) {
                String s_time = Integer.toHexString(time);
                if (s_time.length() < 4) {
                    while (s_time.length() < 4) {
                        s_time = "0" + s_time;
                    }
                }
                UARTManager5.getManager().send("FF01" + s_time + "FE");
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "데이터 오류입니다.", Toast.LENGTH_SHORT).show();
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


        setExState();



        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ExTime.setText(min+":"+sec);
                if(min.equals("00")&&sec.equals("00")) {
                    StopTimer();
                    warmTest = 0;
                    i=0;
                    Toast.makeText(MainActivity.this, "운동이 종료되었습니다.", Toast.LENGTH_SHORT).show();
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


                                    Toast.makeText(MainActivity.this, "데이터를 저장했습니다.", Toast.LENGTH_SHORT).show();
                                    ExTime.setText("00:00");
//                            UARTManager.getManager().setA();
                                    editor.putString("ExTime", "00:00");
                                    editor.commit();
//                            InitUI();
                                }
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "연결된 기기가 없어, 데이터를 저장하지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }

                    tvWarmUp.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                    tvWarmUp.setTextColor(Color.parseColor("#ffffff"));
                    tv1.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                    tv1.setTextColor(Color.parseColor("#ffffff"));
                    tv2.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                    tv2.setTextColor(Color.parseColor("#ffffff"));
                    tv3.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                    tv3.setTextColor(Color.parseColor("#ffffff"));
                    tv4.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                    tv4.setTextColor(Color.parseColor("#ffffff"));
                    tv5.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                    tv5.setTextColor(Color.parseColor("#ffffff"));
                    tv6.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                    tv6.setTextColor(Color.parseColor("#ffffff"));
                    tvCoolDown.setBackground(getResources().getDrawable(R.drawable.button_back_blue));
                    tvCoolDown.setTextColor(Color.parseColor("#ffffff"));
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
                            DayExTimer = "00:00";

                            Toast.makeText(MainActivity.this, "데이터를 저장했습니다.", Toast.LENGTH_SHORT).show();
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
                                    DayExTimer = "00:00";

                                    Toast.makeText(MainActivity.this, "데이터를 저장했습니다.", Toast.LENGTH_SHORT).show();
//                            ExTime.setText("00:00");
                                    InitUICoolDown();
                                }
                            }

                    }else{
                        Toast.makeText(MainActivity.this, "연결된 기기가 없어, 데이터를 저장하지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }

                }
                }
            }
        }
        //TIME
        else if(requestCode == 1010 && resultCode ==1010){
                ExState=0;
            if (i == 1) {
//                UARTManager.getManager().send("FF0B0001FE");
                if(UARTManager.getManager()!=null) {
                    UARTManager.getManager().send("FF0B0000FE");
                }else if(UARTManager2.getManager()!=null) {
                    UARTManager2.getManager().send("FF0B0000FE");
                }else if(UARTManager3.getManager()!=null) {
                    UARTManager3.getManager().send("FF0B0000FE");
                }else if(UARTManager4.getManager()!=null) {
                    UARTManager4.getManager().send("FF0B0000FE");
                }else if(UARTManager5.getManager()!=null) {
                    UARTManager5.getManager().send("FF0B0000FE");
                }
                Toast.makeText(MainActivity.this, "정지", Toast.LENGTH_SHORT).show();
                StopTimer();
                TimerSetProtocol();
                i = 2;
            }

            String time = data.getStringExtra("exTime");
            timeMode = data.getStringExtra("exTimeMode");

            SharedPreferences pref = getApplication().getSharedPreferences("info",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("ExTimeToSend",time);
            editor.commit();

            if(time.equals("21")){
                tv5.setVisibility(GONE);
                tv6.setVisibility(GONE);
            }else if(time.equals("26")){
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(GONE);
            }else{
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.VISIBLE);
            }

            if(time.length() ==1){
                time = "0" + time;
            }



            ExTime.setText(time+":00");
            StopTimer();
            InitUIExceptPower();
            i = 0;

            SendTime();
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
//                    startTime = DATE.substring(11, 16);

//                    String[] useT = pref.getString("ExTime","00").split(":");
////                    int usetI = Integer.parseInt(useT[0]);
//                    String useTime = String.valueOf(usetI);

//                String useDevice = "";
//                if (BleProfileService.getBleProfile() != null) {
//                    int a = BleProfileService.getBleProfile().getConnectionState();
//                    if (BleProfileService.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                        useDevice += "배";
//                    }
//                }
//                if (BleProfileService_2.getBleProfile() != null) {
//                    if (BleProfileService_2.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                        if (!useDevice.equals("")) {
//                            useDevice += ",";
//                        }
//                        useDevice += "팔1";
//                    }
//                }
//                if (BleProfileService_3.getBleProfile() != null) {
//                    if (BleProfileService_3.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                        if (!useDevice.equals("")) {
//                            useDevice += ",";
//                        }
//                        useDevice += "팔2";
//                    }
//                }
//                if (BleProfileService_4.getBleProfile() != null) {
//                    if (BleProfileService_4.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                        if (!useDevice.equals("")) {
//                            useDevice += ",";
//                        }
//                        useDevice += "다리1";
//                    }
//                }
//                if (BleProfileService_5.getBleProfile() != null) {
//                    if (BleProfileService_5.getBleProfile().getConnectionState() == BleProfileService.STATE_CONNECTING) {
//                        if (!useDevice.equals("")) {
//                            useDevice += ",";
//                        }
//                        useDevice += "다리2";
//                    }
//                }


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
                                Toast.makeText(MainActivity.this, "데이터를 저장했습니다.", Toast.LENGTH_SHORT).show();
//                            ExTime.setText("00:00");
                                InitUIExceptPower();
                                ExTime.setText("00:00");
//                        UARTManager.getManager().setA();
                                timer = null;

                    }
                    }

                }else{
                    Toast.makeText(MainActivity.this, "연결된 기기가 없어, 데이터를 저장하지 못했습니다.", Toast.LENGTH_SHORT).show();
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
                    UARTManager.getManager().send("FF0B0000FE");
//                UARTManager.getManager().send("FF3101000001FE");
                }
//
                if (UARTManager2.getManager() != null) {
                    UARTManager2.getManager().send("FF110001FE");
                    UARTManager2.getManager().send("FF0B0000FE");
                }

                if (UARTManager3.getManager() != null) {
                    UARTManager3.getManager().send("FF110001FE");
                    UARTManager3.getManager().send("FF0B0000FE");
                }

                if (UARTManager4.getManager() != null) {
                    UARTManager4.getManager().send("FF110001FE");
                    UARTManager4.getManager().send("FF0B0000FE");
                }

                if (UARTManager5.getManager() != null) {
                    UARTManager5.getManager().send("FF110001FE");
                    UARTManager5.getManager().send("FF0B0000FE");
                }

                InitUI();
                ExTime.setText("00:00");
            }else{

            }
        }


        if(resultCode==1001) {   // 시작
//            btnStart.setText("■");
            if(i!=1) {
                play.performClick();
            }else{
                Toast.makeText(MainActivity.this, "이미 운동중입니다.", Toast.LENGTH_SHORT).show();
            }
        }else if(resultCode == 1002){ // 정지
//            btnStart.setText("▶");
            if(i==1) {
                play.performClick();
            }else{
                Toast.makeText(MainActivity.this, "아직 운동이 시작되지 않았습니다.", Toast.LENGTH_SHORT).show();
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

    public int getStartState(){
        if(i !=0){
            return i;
        }else{
            return 0;
        }
    }

    Button btnSSTStart;
    @Override
    protected void onResume(){
        super.onResume();
        mainActivity= this;
        mainActivity2= this;
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
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},1);
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

    public static MainActivity getMain(){
        return mainActivity;
    }
    public static MainActivity getMain2(){
        return mainActivity2;
    }

    public void device1Vis(){
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StopTimer();
                TimerSetProtocol();
                i = 2;
                device1.setVisibility(GONE);
                Intent intent = new Intent(MainActivity.this, DialogBleToMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                if(device2.getVisibility() == GONE&& device4.getVisibility() == GONE){
                    tvEmptyDevice.setVisibility(View.VISIBLE);
                }else{
                    tvEmptyDevice.setVisibility(GONE);
                }
            }
        }, 0);
    }
    public void device2Vis(){
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StopTimer();
                TimerSetProtocol();
                i = 2;
                device2.setVisibility(GONE);
                Intent intent = new Intent(MainActivity.this, DialogBleToMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                if(device1.getVisibility() == GONE&& device4.getVisibility() == GONE){
                    tvEmptyDevice.setVisibility(View.VISIBLE);
                }else{
                    tvEmptyDevice.setVisibility(GONE);
                }
            }
        }, 0);
    }
    public void device3Vis(){
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StopTimer();
                TimerSetProtocol();
                i = 2;
                device2.setVisibility(GONE);
                Intent intent = new Intent(MainActivity.this, DialogBleToMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                if(device1.getVisibility() == GONE&& device4.getVisibility() == GONE){
                    tvEmptyDevice.setVisibility(View.VISIBLE);
                }else{
                    tvEmptyDevice.setVisibility(GONE);
                }
            }
        }, 0);
    }
    public void device4Vis(){
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StopTimer();
                TimerSetProtocol();
                i = 2;
                device4.setVisibility(GONE);
                Intent intent = new Intent(MainActivity.this, DialogBleToMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                if(device1.getVisibility() == GONE&& device2.getVisibility() == GONE){
                    tvEmptyDevice.setVisibility(View.VISIBLE);
                }else{
                    tvEmptyDevice.setVisibility(GONE);
                }
            }
        }, 0);
    }
    public void device5Vis(){
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StopTimer();
                TimerSetProtocol();
                i = 2;
                device4.setVisibility(GONE);
                Intent intent = new Intent(MainActivity.this, DialogBleToMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                if(device1.getVisibility() == GONE&& device2.getVisibility() == GONE){
                    tvEmptyDevice.setVisibility(View.VISIBLE);
                }else{
                    tvEmptyDevice.setVisibility(GONE);
                }
            }
        }, 0);
    }


    @Override
    protected void onPause() {
        super.onPause();
//        onDestroy();
        mainActivity = null;


        if(mRecognizer!=null){
            mRecognizer.destroy();
            mRecognizer.cancel();
        }
    }


    public void Device1Drop(){
        if(UARTManager.getManager()!=null) {
            UARTManager.getManager().send("FF0B0000FE");
        }
        StopTimer();
        i=2;
        TimerSetProtocol();
        Intent intent = new Intent(MainActivity.this, DialogDevice1Droped.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent,81);
    }
    public void Device2Drop(){
        if(UARTManager2.getManager()!=null) {
            UARTManager2.getManager().send("FF0B0000FE");
        }
        StopTimer();
        i=2;
        TimerSetProtocol();
        Intent intent = new Intent(MainActivity.this, DialogDevice2Droped.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent,81);
    }
    public void Device3Drop(){
        if(UARTManager3.getManager()!=null) {
            UARTManager3.getManager().send("FF0B0000FE");
        }
        StopTimer();
        i=2;
        TimerSetProtocol();
        Intent intent = new Intent(MainActivity.this, DialogDevice3Droped.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent,81);
    }
    public void Device4Drop(){
        if(UARTManager4.getManager()!=null) {
            UARTManager4.getManager().send("FF0B0000FE");
        }
        StopTimer();
        i=2;
        TimerSetProtocol();
        Intent intent = new Intent(MainActivity.this, DialogDevice4Droped.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent,81);
    }
    public void Device5Drop(){
        if(UARTManager5.getManager()!=null) {
            UARTManager5.getManager().send("FF0B0000FE");
        }
        StopTimer();
        i=2;
        TimerSetProtocol();
        Intent intent = new Intent(MainActivity.this, DialogDevice5Droped.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent,81);
    }



    public void InitUI(){
        tvWarmUp.setBackgroundColor(Color.parseColor("#000000"));
        tvWarmUp.setTextColor(Color.parseColor("#ffffff"));
        tv1.setBackgroundColor(Color.parseColor("#000000"));
        tv1.setTextColor(Color.parseColor("#ffffff"));
        tv2.setBackgroundColor(Color.parseColor("#000000"));
        tv2.setTextColor(Color.parseColor("#ffffff"));
        tv3.setBackgroundColor(Color.parseColor("#000000"));
        tv3.setTextColor(Color.parseColor("#ffffff"));
        tv4.setBackgroundColor(Color.parseColor("#000000"));
        tv4.setTextColor(Color.parseColor("#ffffff"));
        tv5.setBackgroundColor(Color.parseColor("#000000"));
        tv5.setTextColor(Color.parseColor("#ffffff"));
        tv6.setBackgroundColor(Color.parseColor("#000000"));
        tv6.setTextColor(Color.parseColor("#ffffff"));
        tvCoolDown.setBackgroundColor(Color.parseColor("#000000"));
        tvCoolDown.setTextColor(Color.parseColor("#ffffff"));


        editor.putString("Device1Setting","0");
        editor.putString("Device2Setting","0");
        editor.putString("Device4Setting","0");
        editor.commit();


//        count1.setText(pref.getString("Device1Setting","0"));
//        count2.setText(pref.getString("Device2Setting","0"));
//        count4.setText(pref.getString("Device4Setting","0"));

        count1.setText("0");
        count2.setText("0");
        count4.setText("0");


    }


    public void InitUIExceptPower(){
        tvWarmUp.setBackgroundColor(Color.parseColor("#000000"));
        tvWarmUp.setTextColor(Color.parseColor("#ffffff"));
        tv1.setBackgroundColor(Color.parseColor("#000000"));
        tv1.setTextColor(Color.parseColor("#ffffff"));
        tv2.setBackgroundColor(Color.parseColor("#000000"));
        tv2.setTextColor(Color.parseColor("#ffffff"));
        tv3.setBackgroundColor(Color.parseColor("#000000"));
        tv3.setTextColor(Color.parseColor("#ffffff"));
        tv4.setBackgroundColor(Color.parseColor("#000000"));
        tv4.setTextColor(Color.parseColor("#ffffff"));
        tv5.setBackgroundColor(Color.parseColor("#000000"));
        tv5.setTextColor(Color.parseColor("#ffffff"));
        tv6.setBackgroundColor(Color.parseColor("#000000"));
        tv6.setTextColor(Color.parseColor("#ffffff"));
        tvCoolDown.setBackgroundColor(Color.parseColor("#000000"));
        tvCoolDown.setTextColor(Color.parseColor("#ffffff"));


//        editor.putString("Device1Setting","0");
//        editor.putString("Device2Setting","0");
//        editor.putString("Device3Setting","0");
//        editor.commit();


//        count1.setText(pref.getString("Device1Setting","0"));
//        count2.setText(pref.getString("Device2Setting","0"));
//        count4.setText(pref.getString("Device4Setting","0"));

//        count1.setText("0");
//        count2.setText("0");
//        count4.setText("0");


    }

    public void InitUICoolDown(){



        editor.putString("Device1Setting","0");
        editor.putString("Device2Setting","0");
        editor.putString("Device4Setting","0");
        editor.commit();


        count1.setText("0");
        count2.setText("0");
        count4.setText("0");
    }

public void SetDrawer(){

    drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
    drawerView = (View)findViewById(R.id.drawer);

    TextView tvProfile = findViewById(R.id.tvProfile);
    tvProfile.setOnClickListener(n->{
        Intent intent = new Intent(MainActivity.this,MyPageActivity.class);
        startActivity(intent);
    });

}

public void ProgressBarClicked(){
    timeMode = pref.getString("ExTimeMode","26");

    tvWarmUp.setOnClickListener(n->{
        if(timeMode.equals("21")){
            ExTime.setText("21:00");
        }else if(timeMode.equals("26")){
            ExTime.setText("26:00");
        }else if(timeMode.equals("31")){
            ExTime.setText("31:00");
        }
        tv1.setBackgroundColor(Color.parseColor("#000000"));
        tv2.setBackgroundColor(Color.parseColor("#000000"));
        tv3.setBackgroundColor(Color.parseColor("#000000"));
        tv4.setBackgroundColor(Color.parseColor("#000000"));
        tv5.setBackgroundColor(Color.parseColor("#000000"));
        tv6.setBackgroundColor(Color.parseColor("#000000"));
        tvCoolDown.setBackgroundColor(Color.parseColor("#000000"));
        getWarmUp("FF120000FE");
        if(UARTManager.getManager()!=null) {
            UARTManager.getManager().send("FF120000FE");
        }else if(UARTManager2.getManager()!=null) {
            UARTManager2.getManager().send("FF120000FE");
        }else if(UARTManager3.getManager()!=null) {
            UARTManager3.getManager().send("FF12000FE");
        }else if(UARTManager4.getManager()!=null) {
            UARTManager4.getManager().send("FF120000FE");
        }else if(UARTManager5.getManager()!=null) {
            UARTManager5.getManager().send("FF120000FE");
        }
//            ExState=1;
    });
        tv1.setOnClickListener(n->{
            if(timeMode.equals("21")){
                ExTime.setText("20:30");
            }else if(timeMode.equals("26")){
                ExTime.setText("25:30");
            }else if(timeMode.equals("31")){
                ExTime.setText("30:30");
            }
            tv2.setBackgroundColor(Color.parseColor("#000000"));
            tv3.setBackgroundColor(Color.parseColor("#000000"));
            tv4.setBackgroundColor(Color.parseColor("#000000"));
            tv5.setBackgroundColor(Color.parseColor("#000000"));
            tv6.setBackgroundColor(Color.parseColor("#000000"));
            tvCoolDown.setBackgroundColor(Color.parseColor("#000000"));
            getWarmUp("FF120001FE");
            if(UARTManager.getManager()!=null) {
                UARTManager.getManager().send("FF120001FE");
            }else if(UARTManager2.getManager()!=null) {
                UARTManager2.getManager().send("FF120001FE");
            }else if(UARTManager3.getManager()!=null) {
                UARTManager3.getManager().send("FF120001FE");
            }else if(UARTManager4.getManager()!=null) {
                UARTManager4.getManager().send("FF120001FE");
            }else if(UARTManager5.getManager()!=null) {
                UARTManager5.getManager().send("FF120001FE");
            }
//            ExState=1;
        });
        tv2.setOnClickListener(n->{
            if(timeMode.equals("21")){
                ExTime.setText("15:30");
            }else if(timeMode.equals("26")){
                ExTime.setText("20:30");
            }else if(timeMode.equals("31")){
                ExTime.setText("25:30");
            }
            tv3.setBackgroundColor(Color.parseColor("#000000"));
            tv4.setBackgroundColor(Color.parseColor("#000000"));
            tv5.setBackgroundColor(Color.parseColor("#000000"));
            tv6.setBackgroundColor(Color.parseColor("#000000"));
            tvCoolDown.setBackgroundColor(Color.parseColor("#000000"));
            getWarmUp("FF120002FE");
            if(UARTManager.getManager()!=null) {
                UARTManager.getManager().send("FF120002FE");
            }else if(UARTManager2.getManager()!=null) {
                UARTManager2.getManager().send("FF120002FE");
            }else if(UARTManager3.getManager()!=null) {
                UARTManager3.getManager().send("FF120002FE");
            }else if(UARTManager4.getManager()!=null) {
                UARTManager4.getManager().send("FF120002FE");
            }else if(UARTManager5.getManager()!=null) {
                UARTManager5.getManager().send("FF120002FE");
            }
//            ExState=2;
        });
        tv3.setOnClickListener(n->{
            if(timeMode.equals("21")){
                ExTime.setText("10:30");
            }else if(timeMode.equals("26")){
                ExTime.setText("15:30");
            }else if(timeMode.equals("31")){
                ExTime.setText("20:30");
            }

            tv4.setBackgroundColor(Color.parseColor("#000000"));
            tv5.setBackgroundColor(Color.parseColor("#000000"));
            tv6.setBackgroundColor(Color.parseColor("#000000"));
            tvCoolDown.setBackgroundColor(Color.parseColor("#000000"));
            getWarmUp("FF120003FE");
            if(UARTManager.getManager()!=null) {
                UARTManager.getManager().send("FF120003FE");
            }else if(UARTManager2.getManager()!=null) {
                UARTManager2.getManager().send("FF120003FE");
            }else if(UARTManager3.getManager()!=null) {
                UARTManager3.getManager().send("FF120003FE");
            }else if(UARTManager4.getManager()!=null) {
                UARTManager4.getManager().send("FF120003FE");
            }else if(UARTManager5.getManager()!=null) {
                UARTManager5.getManager().send("FF120003FE");
            }
//            ExState=3;
        });
        tv4.setOnClickListener(n->{
            if(timeMode.equals("21")){
                ExTime.setText("05:30");
            }else if(timeMode.equals("26")){
                ExTime.setText("10:30");
            }else if(timeMode.equals("31")){
                ExTime.setText("15:30");
            }
            tv5.setBackgroundColor(Color.parseColor("#000000"));
            tv6.setBackgroundColor(Color.parseColor("#000000"));
            tvCoolDown.setBackgroundColor(Color.parseColor("#000000"));
            getWarmUp("FF120004FE");
            if(UARTManager.getManager()!=null) {
                UARTManager.getManager().send("FF120004FE");
            }else if(UARTManager2.getManager()!=null) {
                UARTManager2.getManager().send("FF120004FE");
            }else if(UARTManager3.getManager()!=null) {
                UARTManager3.getManager().send("FF120004FE");
            }else if(UARTManager4.getManager()!=null) {
                UARTManager4.getManager().send("FF120004FE");
            }else if(UARTManager5.getManager()!=null) {
                UARTManager5.getManager().send("FF120004FE");
            }
//            ExState=4;
        });
        tv5.setOnClickListener(n->{
            if(timeMode.equals("26")){
                ExTime.setText("05:30");
            }else if(timeMode.equals("31")){
                ExTime.setText("10:30");
            }
            tv6.setBackgroundColor(Color.parseColor("#000000"));
            tvCoolDown.setBackgroundColor(Color.parseColor("#000000"));
            getWarmUp("FF120005FE");
            if(UARTManager.getManager()!=null) {
                UARTManager.getManager().send("FF120005FE");
            }else if(UARTManager2.getManager()!=null) {
                UARTManager2.getManager().send("FF120005FE");
            }else if(UARTManager3.getManager()!=null) {
                UARTManager3.getManager().send("FF120005FE");
            }else if(UARTManager4.getManager()!=null) {
                UARTManager4.getManager().send("FF120005FE");
            }else if(UARTManager5.getManager()!=null) {
                UARTManager5.getManager().send("FF120005FE");
            }
//            ExState=5;
        });
        tv6.setOnClickListener(n->{
             if(timeMode.equals("31")){
                 //시간 수정 필요
                ExTime.setText("05:31");
            }
            getWarmUp("FF120006FE");
            if(UARTManager.getManager()!=null) {
                UARTManager.getManager().send("FF120006FE");
            }else if(UARTManager2.getManager()!=null) {
                UARTManager2.getManager().send("FF120006FE");
            }else if(UARTManager3.getManager()!=null) {
                UARTManager3.getManager().send("FF120006FE");
            }else if(UARTManager4.getManager()!=null) {
                UARTManager4.getManager().send("FF120006FE");
            }else if(UARTManager5.getManager()!=null) {
                UARTManager5.getManager().send("FF120006FE");
            }
//            ExState=6;
        });
    tvCoolDown.setOnClickListener(n->{
        if(timeMode.equals("21")){
            ExTime.setText("00:30");
            if(UARTManager.getManager()!=null) {
                UARTManager.getManager().send("FF120005FE");
            }else if(UARTManager2.getManager()!=null) {
                UARTManager2.getManager().send("FF120005FE");
            }else if(UARTManager3.getManager()!=null) {
                UARTManager3.getManager().send("FF12005FE");
            }else if(UARTManager4.getManager()!=null) {
                UARTManager4.getManager().send("FF120005FE");
            }else if(UARTManager5.getManager()!=null) {
                UARTManager5.getManager().send("FF120005FE");
            }
        }else if(timeMode.equals("26")){
            ExTime.setText("00:30");
            if(UARTManager.getManager()!=null) {
                UARTManager.getManager().send("FF120006FE");
            }else if(UARTManager2.getManager()!=null) {
                UARTManager2.getManager().send("FF120006FE");
            }else if(UARTManager3.getManager()!=null) {
                UARTManager3.getManager().send("FF12006FE");
            }else if(UARTManager4.getManager()!=null) {
                UARTManager4.getManager().send("FF120006FE");
            }else if(UARTManager5.getManager()!=null) {
                UARTManager5.getManager().send("FF120006FE");
            }
        }else if(timeMode.equals("31")){
            ExTime.setText("00:30");
            if(UARTManager.getManager()!=null) {
                UARTManager.getManager().send("FF120007FE");
            }else if(UARTManager2.getManager()!=null) {
                UARTManager2.getManager().send("FF120007FE");
            }else if(UARTManager3.getManager()!=null) {
                UARTManager3.getManager().send("FF12007FE");
            }else if(UARTManager4.getManager()!=null) {
                UARTManager4.getManager().send("FF120007FE");
            }else if(UARTManager5.getManager()!=null) {
                UARTManager5.getManager().send("FF120007FE");
            }
        }
        tvCoolDown.setBackgroundColor(Color.parseColor("#000000"));
        getWarmUp("FF120007FE");

//            ExState=1;
    });
}



    @Override
    public void onDestroy() {
        super.onDestroy();
        StopTimer();
        String email = pref.getString("email", "00");
        String nickname = pref.getString("nickname", "00");
        String mode = pref.getString("mode", "M");

//        if(UARTManager.getManager()!=null) {
//            UARTManager.getManager().send("FF100001FE");
//        }else if(UARTManager2.getManager()!=null) {
//            UARTManager2.getManager().send("FF100001FE");
//        }else if(UARTManager3.getManager()!=null) {
//            UARTManager3.getManager().send("FF100001FE");
//        }else if(UARTManager4.getManager()!=null) {
//            UARTManager4.getManager().send("FF100001FE");
//        }else if(UARTManager5.getManager()!=null) {
//            UARTManager5.getManager().send("FF100001FE");
//        }

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
                DayExTimer = "00:00";

                Toast.makeText(MainActivity.this, "데이터를 저장했습니다.", Toast.LENGTH_SHORT).show();
//                            ExTime.setText("00:00");
            }
        }

        if(tts !=null ){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        if(mRecognizer!=null){
            mRecognizer.destroy();
            mRecognizer.cancel();
            mRecognizer = null;
        }
    }

    public void SetedtCount1(String count){
        count1.setText(count);
    }

    int exDeviceNum2= 0;
    int device2Count = 100;
    int device3Count = 100;
    public void SetedtCount2(String count,int deviceNum){
        if(deviceNum ==2){
            device2Count = Integer.parseInt(count);
        }else{
            device3Count = Integer.parseInt(count);
        }

        if(device2Count ==100 && device3Count ==100){
            count2.setText("0");
        }else if(device2Count==100&&device3Count!=100){
            count2.setText(String.valueOf(device3Count));
        }else if(device2Count!=100&&device3Count==100){
            count2.setText(String.valueOf(device2Count));
        }else if(device2Count!=100&&device3Count!=100){
            if(device2Count>device3Count){
                count2.setText(String.valueOf(device3Count));
            }else{
                count2.setText(String.valueOf(device2Count));
            }
        }
    }
    int device4Count = 100;
    int device5Count = 100;
    public void SetedtCount4(String count,int deviceNum){
        if(deviceNum ==4){
            device4Count = Integer.parseInt(count);
        }else{
            device5Count = Integer.parseInt(count);
        }

        if(device4Count ==100 && device5Count ==100){
            count4.setText("0");
        }else if(device4Count==100&&device5Count!=100){
            count4.setText(String.valueOf(device5Count));
        }else if(device4Count!=100&&device5Count==100){
            count4.setText(String.valueOf(device4Count));
        }else if(device4Count!=100&&device5Count!=100){
            if(device4Count>device5Count){
                count4.setText(String.valueOf(device5Count));
            }else{
                count4.setText(String.valueOf(device4Count));
            }
        }
    }

    Intent SttIntent;
    SpeechRecognizer mRecognizer;
    TextToSpeech tts;

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

            Intent intent = new Intent(MainActivity.this, DialogSpeach.class);
            startActivityForResult(intent, 1);
//            tts.speak("하이 빅스비", TextToSpeech.QUEUE_FLUSH, null);
        }


    }

        int DeviceRunning = 0;
    public void DeviceIsAlreadyStated(int state){

        if(DeviceRunning == 0) {
            switch (state) {
                case 1:
                    tv1.performClick();
                    play.performClick();
                    DeviceRunning = 1;
                    break;
                case 2:
                    tv2.performClick();
                    play.performClick();
                    DeviceRunning = 1;
                    break;
                case 3:
                    tv3.performClick();
                    play.performClick();
                    DeviceRunning = 1;
                    break;
                case 4:
                    tv4.performClick();
                    play.performClick();
                    DeviceRunning = 1;
                    break;
                case 5:
                    tv5.performClick();
                    play.performClick();
                    DeviceRunning = 1;
                    break;
                case 6:
                    tv6.performClick();
                    play.performClick();
                    DeviceRunning = 1;
                    break;
                case 7:
                    tvCoolDown.performClick();
                    play.performClick();
                    DeviceRunning = 1;
                    break;


            }
        }

    }


 class ExceptionHandler implements Thread.UncaughtExceptionHandler{
     @Override
     public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {

         onDestroy();
         Toast.makeText(MainActivity.this, "앱 사용도중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        AppFinish();

     }
 }

    public void AppFinish() {
        finishAffinity();
        System.exit(0);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("기기 연결 화면으로 돌아가기");
        builder.setMessage("기기 연결화면으로 돌아가실 경우 진행 중인 운동은 종료됩니다\n\n  *운동 데이터는 저장됩니다*");
        builder.setCancelable(true);
        builder.setPositiveButton("돌아가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.super.onBackPressed();
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
