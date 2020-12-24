package com.example.copyhomet.Main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.copyhomet.Bluetooth.BleConnectionActivity;
import com.example.copyhomet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadingActivity extends AppCompatActivity {



    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    String email,password;

    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("info",MODE_PRIVATE);
            email = pref.getString("email","");
            password = pref.getString("password","");

            Login task = new Login();
            task.execute("http://20.194.57.6/homet/SignIn.php",email,password,"general");


        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        getSupportActionBar().hide();


        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }

    }
    class Login extends AsyncTask<String, Void, String> {


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
                Toast.makeText(LoadingActivity.this, "자동 로그인", Toast.LENGTH_SHORT).show();
                    GetMemberInfo task = new GetMemberInfo();
                    task.execute("http://20.194.57.6/homet/GetMemberInfo.php",email);

                    Intent intent = new Intent(LoadingActivity.this, BleConnectionActivity.class);
                    startActivity(intent);


                }else if(Respon.contains("Email")){
                    Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else if(Respon.contains("Password")){
                    Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
                Toast.makeText(LoadingActivity.this, "서버 오류 입니다. 잠시후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
//                finish();
//                Intent intent = new Intent(LoadingActivity.this, BleConnectionActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                startActivity(intent);
            }



        }

        @Override
        protected String doInBackground(String... params) {

            String email  = (String)params[1];
            String password  = (String)params[2];
            String type = (String) params[3];



            String postParameters = "email=" + email +"&"+ "password=" + password+"&"+ "type=" + type;


            try {

                URL url = new URL("http://20.194.57.6/homet/SignIn.php");
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

    class GetMemberInfo extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            try {


                JSONObject Land = new JSONObject(result);


                JSONArray jsonArray = Land.getJSONArray("Response");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject subJsonObject = jsonArray.getJSONObject(i);


                    String email = subJsonObject.getString("email");
                    String ismale = subJsonObject.getString("ismale");
                    String birth = subJsonObject.getString("birth");
                    String nickname = subJsonObject.getString("nickname");
                    String height = subJsonObject.getString("height");
                    String weight = subJsonObject.getString("weight");
                    String inbody = subJsonObject.getString("inbody");



                    int iHeight = Integer.parseInt(height.replace("cm",""));
                    int iWeight = Integer.parseInt(weight.replace("kg",""));
                    double bmi = iWeight / ((iHeight *0.01)*(iHeight *0.01));
                    String sBMI = String.format("%.2f",bmi);

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("info",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("email",email);
                    editor.putString("password",password);
                    editor.putString("isMale",ismale);
                    editor.putString("Height",height);
                    editor.putString("Weight",weight);
                    editor.putString("birth",birth);
                    editor.putString("nickname",nickname);
                    editor.putString("inbody",inbody);
                    editor.putString("bmi",sBMI);
                    editor.commit();

                    break;

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        @Override
        protected String doInBackground(String... params) {

            String email =(String) params[1];

            String postParameters = "email=" + email;


            try {

                URL url = new URL("http://20.194.57.6/homet/GetMemberInfo.php");
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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
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
    protected  void onResume() {
        super.onResume();
        handler.postDelayed(r,100);
    }
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(r);
    }
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoadingActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }



    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(LoadingActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(LoadingActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoadingActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(LoadingActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(LoadingActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(LoadingActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
//        super.onRequestPermissionsResult(requestCode, permissions, grantResult);
//        //requestPermission 메서드에서 리퀘스트 코드로 지정한, 마지막 매개변수에 0을 넣어 줬으므로
//        if(requestCode == 100){
//            // requestPermission의 두번째 매개변수는 배열이므로 아이템이 여러개 있을 수 있기 때문에 결과를 배열로 받는다.
//            // 해당 예시는 요청 퍼미션이 한개 이므로 i=0 만 호출한다.
//            if(grantResult[0] == 0){
//                //해당 권한이 승낙된 경우.
//            }else{
//                //해당 권한이 거절된 경우.
//                AlertDialog.Builder builder = new AlertDialog.Builder(LoadingActivity.this);
//                builder.setTitle("권한 설정");
//                builder.setMessage("권한을 허용하지 않으시면 앱을 사용하실수 없습니다.");
//                builder.setPositiveButton("설정",new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog , int id){
//                        Intent appDetail = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+getPackageName()));
//                        appDetail.addCategory(Intent.CATEGORY_DEFAULT);
//                        appDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(appDetail);
//                    }
//                });
//
//                builder.setNegativeButton("종료",new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog,int id){
//                        moveTaskToBack(true);
//                        finish();
//                        android.os.Process.killProcess(android.os.Process.myPid());
//
//                    }
//                });
//                builder.create().show();
//            }
//        }
//    }




}
