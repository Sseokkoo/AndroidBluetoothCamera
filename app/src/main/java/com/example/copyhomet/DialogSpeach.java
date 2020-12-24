package com.example.copyhomet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.copyhomet.R;

import java.util.ArrayList;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class DialogSpeach extends AppCompatActivity {

    Intent SttIntent;
    SpeechRecognizer mRecognizer;
    TextToSpeech tts;

    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_set_text);

        ImageView imageView = findViewById(R.id.gif_image);
        Glide.with(this).load(R.drawable.loadinggif).into(imageView);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        SttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        SttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getApplicationContext().getPackageName());
        SttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
        mRecognizer  = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);



        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(n->{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager
                    .PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(DialogSpeach.this,new String[]{Manifest.permission.RECORD_AUDIO},1);
            }else{
                try{
                    mRecognizer.startListening(SttIntent);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                txtSystem.setText("어플 실행됨 자동 실행" + "\r\n" + txtSystem.getText());
                btnStart.performClick();
//                Toast.makeText(DialogSpeach.this, "음성인식 시작", Toast.LENGTH_SHORT).show();
            }
        },500);



        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setResult(1011);
                finish();
            }
        }, 10000);

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
            btnStart.performClick();
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
                    btnStart.performClick();
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    btnStart.performClick();
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    btnStart.performClick();
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
        Log.d("Voice2", VoiceMsg);


            if (VoiceMsg.indexOf("스타트") > -1 || VoiceMsg.indexOf("스타토") > -1 || VoiceMsg.indexOf("스타티") > -1 || VoiceMsg.indexOf("스터트") > -1 || VoiceMsg.indexOf("스타투") > -1 || VoiceMsg.indexOf("스터토") > -1
                    || VoiceMsg.indexOf("시작") > -1 || VoiceMsg.indexOf("시적") > -1 || VoiceMsg.indexOf("스작") > -1 || VoiceMsg.indexOf("스터티") > -1 || VoiceMsg.indexOf("스터투") > -1 || VoiceMsg.indexOf("스타터") > -1) {
                setResult(1001);
                finish();
            } else if (VoiceMsg.indexOf("스탑") > -1 || VoiceMsg.indexOf("스톱") > -1 || VoiceMsg.indexOf("스돕") > -1 || VoiceMsg.indexOf("스답") > -1 || VoiceMsg.indexOf("중지") > -1 || VoiceMsg.indexOf("정지") > -1) {
                setResult(1002);
                finish();
            } else if (VoiceMsg.indexOf("플러스") > -1 || VoiceMsg.indexOf("쁘라스") > -1 || VoiceMsg.indexOf("풀러스") > -1 || VoiceMsg.indexOf("플라스") > -1 || VoiceMsg.indexOf("뿔라스") > -1 || VoiceMsg.indexOf("쁠라스") > -1
                    || VoiceMsg.indexOf("강하게") > -1 || VoiceMsg.indexOf("강하개") > -1 || VoiceMsg.indexOf("강허게") > -1 || VoiceMsg.indexOf("강허개") > -1 || VoiceMsg.indexOf("걍하게") > -1 || VoiceMsg.indexOf("광하게") > -1) {
                setResult(1003);
                finish();
            } else if (VoiceMsg.indexOf("마이너스") > -1 || VoiceMsg.indexOf("마이나스") > -1 || VoiceMsg.indexOf("마아나스") > -1 || VoiceMsg.indexOf("마이너수") > -1 || VoiceMsg.indexOf("마이나수") > -1 || VoiceMsg.indexOf("약하게") > -1
                    || VoiceMsg.indexOf("약하게") > -1 || VoiceMsg.indexOf("야가게") > -1 || VoiceMsg.indexOf("약하계") > -1 || VoiceMsg.indexOf("약하개") > -1 || VoiceMsg.indexOf("야가계") > -1 || VoiceMsg.indexOf("약아게") > -1) {
                setResult(1004);
                finish();
            } else {
//                tts.speak("다시 말씀해주세요", TextToSpeech.QUEUE_FLUSH, null);
//                finish();
            }
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
