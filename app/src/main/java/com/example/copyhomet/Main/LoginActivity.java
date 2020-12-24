package com.example.copyhomet.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.Bluetooth.BleConnectionActivity;
import com.example.copyhomet.R;
import com.example.copyhomet.SignInActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail,edtPassword;
    TextView findPassword;
    Button btnLogin,btnSignIn,btnGuestLogin;


    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        edtEmail = findViewById(R.id.edtemail);
        edtPassword = findViewById(R.id.edtpassword);
        findPassword = findViewById(R.id.findPassword);
        btnLogin = findViewById(R.id.login);
        btnSignIn =findViewById(R.id.signin);
//        btnGuestLogin = findViewById(R.id.noSign);



        btnLogin.setOnClickListener(n->{
            email = edtEmail.getText().toString().replace(" ","");
            password =edtPassword.getText().toString();


            Login task = new Login();
            task.execute("http://20.194.57.6/homet/SignIn.php",email,password,"general");
        });
        btnSignIn.setOnClickListener(n->{
            Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
            startActivity(intent);
        });
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
//                Toast.makeText(LoginActivity.this, "가입 완료.", Toast.LENGTH_SHORT).show();
                   GetMemberInfo task = new GetMemberInfo();
                   task.execute("http://20.194.57.6/homet/GetMemberInfo.php",email);

                    Intent intent = new Intent(LoginActivity.this, BleConnectionActivity.class);
                    startActivity(intent);


                }else if(Respon.contains("Email")){
                   Toast.makeText(LoginActivity.this, "이메일을 확인해 주세요.", Toast.LENGTH_SHORT).show();
               }else if(Respon.contains("Password")){
                   Toast.makeText(LoginActivity.this, "잘못된 비밀번호입니다.", Toast.LENGTH_SHORT).show();
               }else {
//                Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
            }

            } catch (JSONException ex) {
                ex.printStackTrace();
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

    long backKeyPressedTime;
    @Override
    public void onBackPressed() {
        //1번째 백버튼 클릭
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로가기 버튼을 두번 누르면 앱이 종료 됩니다.", Toast.LENGTH_SHORT).show();
        }
        //2번째 백버튼 클릭 (종료)
        else {
            AppFinish();
        }
    }

    //앱종료
    public void AppFinish() {
        finishAffinity();
        System.exit(0);
    }
}
