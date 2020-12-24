package com.example.copyhomet.Main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.copyhomet.Bluetooth.BleConnectionActivity;
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
import com.example.copyhomet.DialogSpeach;
import com.example.copyhomet.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class CustomExActivity extends AppCompatActivity {


    Intent SttIntent;

    SpeechRecognizer mRecognizer;
    TextToSpeech tts;

    Button btnPlus1,btnPlus2,btnPlus3,btnPlus4;
    Button btnMinus1,btnMinus2,btnMinus3,btnMinus4;

    TextView tv1, tv2, tv3;
    EditText tv4;

    int startState;

    ProgressBar progressBar;
    Button btnPlay;

    int PlayState =0;
    int StopState=0;
    Button btnSSTStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ex);

        LinearLayout close = findViewById(R.id.close);
        close.setOnClickListener(n->{
            onBackPressed();
        });
        getSupportActionBar().hide();

        init();




    }

    public void init(){
        btnPlus1 = findViewById(R.id.plus1);
        btnPlus2 = findViewById(R.id.plus2);
        btnPlus3 = findViewById(R.id.plus3);
        btnPlus4 = findViewById(R.id.plus4);

        btnMinus1 = findViewById(R.id.minus1);
        btnMinus2 = findViewById(R.id.minus2);
        btnMinus3 = findViewById(R.id.minus3);
        btnMinus4 = findViewById(R.id.minus4);

        tv1 = findViewById(R.id.edtCount1);
        tv2 = findViewById(R.id.edtCount2);
//        tv3 = findViewById(R.id.edtCount3);
        tv4 = findViewById(R.id.edtCount4);

        tv4.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff f
                    tv4.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tv4.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        btnPlus1.setOnClickListener(n->{
            String count = tv1.getText().toString();
            int iCount = Integer.parseInt(count);
                    iCount = iCount + 1;
                    tv1.setText(String.valueOf(iCount));

            sendAllCustomProtocol();
        });
        btnPlus2.setOnClickListener(n->{
            String count = tv2.getText().toString();
            int iCount = Integer.parseInt(count);
                iCount = iCount + 1;
                tv2.setText(String.valueOf(iCount));

            sendAllCustomProtocol();
        });
        btnPlus3.setOnClickListener(n->{

            if(StopState == 1) {
//                if (UARTManager.getManager() != null) {
//                    UARTManager.getManager().send("FF0D0001FE");
//                } else
                    if (UARTManager2.getManager() != null) {
                    UARTManager2.getManager().send("FF0D0001FE");
                }
                else if (UARTManager3.getManager() != null) {
                    UARTManager3.getManager().send("FF0D0001FE");
                }
//                else if (UARTManager4.getManager() != null) {
//                    UARTManager4.getManager().send("FF0D0001FE");
//                } else if (UARTManager5.getManager() != null) {
//                    UARTManager5.getManager().send("FF0D0001FE");
//                }
            }else{
                Toast.makeText(this, "운동중에만 조작이 가능합니다.", Toast.LENGTH_SHORT).show();
            }

        });
        btnPlus4.setOnClickListener(n->{
            String count = tv4.getText().toString();
            int iCount = Integer.parseInt(count);
            if(iCount<30) {
                    iCount = iCount + 1;
                    tv4.setText(String.valueOf(iCount));
            }
            sendAllCustomProtocol();
        });


        btnMinus1.setOnClickListener(n->{
            String count = tv1.getText().toString();
            int iCount = Integer.parseInt(count);
            if(iCount>1) {
                    iCount = iCount - 1;
                    tv1.setText(String.valueOf(iCount));
            }
            sendAllCustomProtocol();
        });

        btnMinus2.setOnClickListener(n->{
            String count = tv2.getText().toString();
            int iCount = Integer.parseInt(count);
            if(iCount>1) {
                    iCount = iCount - 1;
                    tv2.setText(String.valueOf(iCount));
            }
            sendAllCustomProtocol();
        });

        btnMinus3.setOnClickListener(n->{
            if(StopState ==1 ) {
//                if (UARTManager.getManager() != null) {
//                    UARTManager.getManager().send("FF0C0001FE");
//                } else
                    if (UARTManager2.getManager() != null) {
                    UARTManager2.getManager().send("FF0C0001FE");
                } else if (UARTManager3.getManager() != null) {
                    UARTManager3.getManager().send("FF0C0001FE");
                }
//                    else if (UARTManager4.getManager() != null) {
//                    UARTManager4.getManager().send("FF0C0001FE");
//                } else if (UARTManager5.getManager() != null) {
//                    UARTManager5.getManager().send("FF0C0001FE");
//                }
            }else{
                Toast.makeText(this, "운동중에만 조작이 가능합니다. ", Toast.LENGTH_SHORT).show();
            }
        });

        btnMinus4.setOnClickListener(n->{
            String count = tv4.getText().toString();
            int iCount = Integer.parseInt(count);
            if(iCount>1) {
                    iCount = iCount - 1;
                    tv4.setText(String.valueOf(iCount));
            }
            sendAllCustomProtocol();
        });

        btnPlay = findViewById(R.id.play);
        progressBar = findViewById(R.id.progress_bar);

        btnPlay.setOnClickListener(n->{


                if (BleProfileService_2.getBleProfile() != null) {
                    if (BleProfileService_2.getBleProfile().getConnectionState() == 2 || BleProfileService_3.getBleProfile().getConnectionState() == 2) {
                        PlayClick();
                    } else {
                        Toast.makeText(CustomExActivity.this, "팔에 연결된 기기가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else if (BleProfileService_3.getBleProfile() != null) {
                    if (BleProfileService_2.getBleProfile().getConnectionState() == 2 || BleProfileService_3.getBleProfile().getConnectionState() == 2) {
                        PlayClick();
                    } else {
                        Toast.makeText(CustomExActivity.this, "팔에 연결된 기기가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CustomExActivity.this, "팔에 연결된 기기가 없습니다.", Toast.LENGTH_SHORT).show();
                }


        });

    }

    public void PlayClick(){

        PlayCount =0;
        GetCount = Integer.parseInt(tv4.getText().toString());

        if(StopState == 0) {
            if(GetCount !=0) {
                if (PlayState == 0 || PlayState == 1) {
                    Toast.makeText(this, "맞춤 운동 시작", Toast.LENGTH_SHORT).show();
                    setProgressBarPlus();
                    PlayState = 1;
                    StopState = 1;

                    SharedPreferences pref = getSharedPreferences("info",MODE_PRIVATE);
                    Integer bmi = Integer.valueOf(pref.getString("bmi","0").replace(".",""));
                    String s_bmi = Integer.toHexString(bmi);
                    if (s_bmi.length() < 4) {
                        while (s_bmi.length() < 4) {
                            s_bmi = "0" + s_bmi;
                        }
                    }

                    sendAllCustomProtocol();
//                    if(UARTManager.getManager()!=null) {
//                        UARTManager.getManager().send("FF0B0001FE");
//                        UARTManager.getManager().send("FF19"+s_bmi +"FE");
//                    }else
                        if(UARTManager2.getManager()!=null) {
                        UARTManager2.getManager().send("FF0B0001FE");
                        UARTManager2.getManager().send("FF19"+s_bmi +"FE");
                    }else if(UARTManager3.getManager()!=null) {
                        UARTManager3.getManager().send("FF0B0001FE");
                        UARTManager3.getManager().send("FF19"+s_bmi +"FE");
                    }
//                        else if(UARTManager4.getManager()!=null) {
//                        UARTManager4.getManager().send("FF0B0001FE");
//                        UARTManager4.getManager().send("FF19"+s_bmi +"FE");
//                    }else if(UARTManager5.getManager()!=null) {
//                        UARTManager5.getManager().send("FF0B0001FE");
//                        UARTManager5.getManager().send("FF19"+s_bmi +"FE");
//                    }





                } else {
                    setProgressBarMinus();
                    StopState = 1;
                    PlayState = 1;
                    sendAllCustomProtocol();
//                    if(UARTManager.getManager()!=null) {
//                        UARTManager.getManager().send("FF0B0000FE");
//                    }else
                        if(UARTManager2.getManager()!=null) {
                        UARTManager2.getManager().send("FF0B0000FE");
                    }else if(UARTManager3.getManager()!=null) {
                        UARTManager3.getManager().send("FF0B0000FE");
                    }
//                        else if(UARTManager4.getManager()!=null) {
//                        UARTManager4.getManager().send("FF0B0000FE");
//                    }else if(UARTManager5.getManager()!=null) {
//                        UARTManager5.getManager().send("FF0B0000FE");
//                    }

                }
            }
        }else{
            StopState =0;
            Toast.makeText(this, "맞춤 운동 정지", Toast.LENGTH_SHORT).show();
        }
    }
    int PlusDelay=0;
    int progress =0;
    int PlayCount = 0;
    int GetCount;
    public void setProgressBarPlus(){

        GetCount = Integer.parseInt(tv4.getText().toString());
        String D = tv1.getText().toString();
        if(!D.equals("0")) {
            PlusDelay = Integer.parseInt(D + "000") / 100;

            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (progressBar.getProgress() < 100) {
                        progress = progress + 1;
                        progressBar.setProgress(progress);

                        if(progress ==100){
                            long now = System.currentTimeMillis();
                            Date Todate = new Date(now);
                            SimpleDateFormat sdfNOW = new SimpleDateFormat("HH:mm:ss");
                            String Time = sdfNOW.format(Todate);

                            Log.d("MinusStart",Time);
                            if(PlayCount < GetCount ) {

                                setProgressBarMinus();
                                PlayState=2;
                                PlayCount++;
                            }
                        }else {
                            if(StopState == 1) {
                                PlayState=1;
                                setProgressBarPlus();
                            }
                        }
                    }
                }
            }, PlusDelay);
        }else{
            StopState = 0;
        }
    }
    int MinusDelay=0;

    public void setProgressBarMinus(){

        GetCount = Integer.parseInt(tv4.getText().toString());

        String D = tv2.getText().toString();
        if(!D.equals("0")) {
            MinusDelay = Integer.parseInt(D + "000") / 100;
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (progressBar.getProgress() > 0) {
                        progress = progress - 1;
                        progressBar.setProgress(progress);

                        if(progress == 0 ) {
                            long now = System.currentTimeMillis();
                            Date Todate = new Date(now);
                            SimpleDateFormat sdfNOW = new SimpleDateFormat("HH:mm:ss");
                            String Time = sdfNOW.format(Todate);

                            Log.d("PlusStart",Time);
                            if(PlayCount < GetCount ) {
                                PlayState=1;
                                setProgressBarPlus();
                            }else{
                                StopState =0;
                                PlayState = 0;
                                Toast.makeText(CustomExActivity.this, "맞춤 운동 종료", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            if(StopState == 1) {
                                PlayState=2;
                                setProgressBarMinus();
                            }
                        }
                    }
                }
            }, MinusDelay);
        }else{
            StopState = 0;
        }
    }


    public void sendAllCustomProtocol(){


        Integer Time1 = Integer.valueOf(tv1.getText().toString());
        String s_time1 = Integer.toHexString(Time1);
        if (s_time1.length() < 4) {
            while (s_time1.length() < 4) {
                s_time1 = "0" + s_time1;
            }
        }
        //수축 시간
//        if(UARTManager.getManager()!=null) {
//            UARTManager.getManager().send("FF16"+s_time1+"FE");
//        }else
            if(UARTManager2.getManager()!=null) {
            UARTManager2.getManager().send("FF16"+s_time1+"FE");
        }else if(UARTManager3.getManager()!=null) {
            UARTManager3.getManager().send("FF16"+s_time1+"FE");
        }
//            else if(UARTManager4.getManager()!=null) {
//            UARTManager4.getManager().send("FF16"+s_time1+"FE");
//        }else if(UARTManager5.getManager()!=null) {
//            UARTManager5.getManager().send("FF16"+s_time1+"FE");
//        }




        Integer Time2 = Integer.valueOf(tv2.getText().toString());
        String s_time2 = Integer.toHexString(Time2);
        if (s_time2.length() < 4) {
            while (s_time2.length() < 4) {
                s_time2 = "0" + s_time2;
            }
        }
        //이완 시간
//        if(UARTManager.getManager()!=null) {
//            UARTManager.getManager().send("FF17"+s_time2+"FE");
//        }else
            if(UARTManager2.getManager()!=null) {
            UARTManager2.getManager().send("FF17"+s_time2+"FE");
        }else if(UARTManager3.getManager()!=null) {
            UARTManager3.getManager().send("FF17"+s_time2+"FE");
        }
//            else if(UARTManager4.getManager()!=null) {
//            UARTManager4.getManager().send("FF17"+s_time2+"FE");
//        }else if(UARTManager5.getManager()!=null) {
//            UARTManager5.getManager().send("FF17"+s_time2+"FE");
//        }


        Integer Count = Integer.valueOf(tv4.getText().toString());
        String s_count = Integer.toHexString(Count);
        if (s_count.length() < 4) {
            while (s_count.length() < 4) {
                s_count = "0" + s_count;
            }
        }
        //횟수
//       if(UARTManager.getManager()!=null) {
//            UARTManager.getManager().send("FF18"+s_count+"FE");
//        }else
            if(UARTManager2.getManager()!=null) {
            UARTManager2.getManager().send("FF18"+s_count+"FE");
        }else if(UARTManager3.getManager()!=null) {
            UARTManager3.getManager().send("FF18"+s_count+"FE");
        }
//            else if(UARTManager4.getManager()!=null) {
//            UARTManager4.getManager().send("FF18"+s_count+"FE");
//        }else if(UARTManager5.getManager()!=null) {
//            UARTManager5.getManager().send("FF18"+s_count+"FE");
//        }

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

    /**음성인식**/
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

            Intent intent = new Intent(CustomExActivity.this, DialogSpeach.class);
            startActivityForResult(intent, 1);
//            tts.speak("하이 빅스비", TextToSpeech.QUEUE_FLUSH, null);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==1001) {
            if(StopState == 0) {
                btnPlay.performClick(); // 시작
            }else{
                Toast.makeText(CustomExActivity.this, "이미 운동중입니다.", Toast.LENGTH_SHORT).show();
            }
        }else if(resultCode == 1002){
            if(StopState == 1) {
                btnPlay.performClick(); // 정지
            }else{
                Toast.makeText(CustomExActivity.this, "아직 운동이 시작되지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        }else if(resultCode == 1003){
            btnPlus3.performClick();
        }else if(resultCode == 1004){
            btnMinus3.performClick();
        }else{
            tts.speak("다시 말씀해주세요", TextToSpeech.QUEUE_FLUSH, null);
//            Toast.makeText(Main2Activity.this, "다시 말씀해주세요", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
//        Intent intent = new Intent(Main2Activity.this, DialogSpeach.class);
//        startActivityForResult(intent, 1);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });



        btnSSTStart = findViewById(R.id.btnSSTStart);
        btnSSTStart.setOnClickListener(n->{

            AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

//            amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                amanager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
//            amanager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0);
//            amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
//            amanager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
//            amanager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
            } else {
//            amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
//            amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
//            amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
//            amanager.setStreamMute(AudioManager.STREAM_RING, true);
//            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
            }


            if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager
                    .PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(CustomExActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},1);
            }else{
                try{
                    mRecognizer.startListening(SttIntent);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
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
            }
        },1000);


    }

    @Override
    protected void onPause() {

//        if(tts !=null ){
//            tts.stop();
//            tts.shutdown();
//            tts = null;
//        }
        if(mRecognizer!=null){
            mRecognizer.destroy();
            mRecognizer.cancel();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
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

}
