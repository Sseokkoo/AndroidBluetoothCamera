package com.example.copyhomet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copyhomet.Bluetooth.BleConnectionActivity;
import com.example.copyhomet.Main.LoginActivity;
import com.example.copyhomet.R;
import com.example.copyhomet.SignDialog.DialogBirth;
import com.example.copyhomet.SignDialog.DialogHeight;
import com.example.copyhomet.SignDialog.DialogIsMale;
import com.example.copyhomet.SignDialog.DialogNickname;
import com.example.copyhomet.SignDialog.DialogWeight;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignInActivity extends AppCompatActivity {

    EditText email, password,passwordchk,edtnickname;
    LinearLayout isMaleLayout,heightLayout,WeightLayout,birthLayout,nicknameLayout,inbodyLayout;
    TextView tvIsMale,tvHeight,tvWeight,tvBirth,tvNickname,tvInbody;

    Button signin,Login;

    String getemail,getpw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().hide();

        init();
    }

    public void init(){


        isMaleLayout = findViewById(R.id.isMaleLayout);
        isMaleLayout.setOnClickListener(n->{
            Intent intent = new Intent(SignInActivity.this, DialogIsMale.class);
            startActivityForResult(intent,1);
        });

        heightLayout = findViewById(R.id.heightLayout);
        heightLayout.setOnClickListener(n->{
            Intent intent = new Intent(SignInActivity.this, DialogHeight.class);
            startActivityForResult(intent,2);
        });
        WeightLayout = findViewById(R.id.weightLayout);
        WeightLayout.setOnClickListener(n->{
            Intent intent = new Intent(SignInActivity.this, DialogWeight.class);
            startActivityForResult(intent,3);
        });
        birthLayout =findViewById(R.id.birthLayout);
        birthLayout.setOnClickListener(n->{
            Intent intent = new Intent(SignInActivity.this, DialogBirth.class);
            startActivityForResult(intent,4);
        });
        nicknameLayout =findViewById(R.id.nicknameLayout);
        nicknameLayout.setOnClickListener(n->{
            Intent intent = new Intent(SignInActivity.this, DialogNickname.class);
            startActivityForResult(intent,5);
        });
        inbodyLayout =findViewById(R.id.inbodyLayout);
        inbodyLayout.setOnClickListener(n->{
//            Intent intent = new Intent(SignInActivity.this, DialogHeight.class);
//            startActivity(intent);
        });


        tvIsMale = findViewById(R.id.tvisMale);
        tvHeight = findViewById(R.id.tvheight);
        tvWeight =findViewById(R.id.tvweight);
        tvBirth = findViewById(R.id.tvbirth);
        tvNickname =findViewById(R.id.tvnickname);
        tvInbody =findViewById(R.id.tvinbody);



        email = findViewById(R.id.edtemail);
        password = findViewById(R.id.PW);
        passwordchk = findViewById(R.id.PWC);
        edtnickname = findViewById(R.id.edtNickname);

        signin = findViewById(R.id.signin);
        signin.setOnClickListener(n->{
            if(email.getText().toString().equals("") || passwordchk.getText().toString().equals("") || password.getText().toString().equals("") || edtnickname.getText().toString().equals("")){
                Toast.makeText(SignInActivity.this,"입력란을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
            }else {
                getemail = email.getText().toString();
                getpw = password.getText().toString();
                String isMale = tvIsMale.getText().toString();
                String Height = tvHeight.getText().toString();
                String Weight = tvWeight.getText().toString();
                String birth = tvBirth.getText().toString();
                String nickname = edtnickname.getText().toString();
                String inbody = tvInbody.getText().toString();


                int iHeight = Integer.parseInt(Height.replace("cm",""));
                int iWeight = Integer.parseInt(Weight.replace("kg",""));
                double bmi = iWeight / ((iHeight *0.01)*(iHeight *0.01));
                String sBMI = String.format("%.2f",bmi);


                SharedPreferences pref = getApplicationContext().getSharedPreferences("info",MODE_PRIVATE);
                SharedPreferences.Editor editor =pref.edit();
                editor.putString("email",getemail);
                editor.putString("password",getpw);
                editor.putString("isMale",isMale);
                editor.putString("Height",Height);
                editor.putString("Weight",Weight);
                editor.putString("birth",birth);
                editor.putString("nickname",nickname);
                editor.putString("inbody",inbody);
                editor.putString("bmi",sBMI);
                editor.commit();
//            else if(nickname.equals("닉네임")) {
//                Toast.makeText(SignInActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
//            }

                if (passwordchk.getText().toString().equals(password.getText().toString())) {
                    if (isMale.equals("성별")) {
                        Toast.makeText(SignInActivity.this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    } else if (Height.equals("키")) {
                        Toast.makeText(SignInActivity.this, "키를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else if (Weight.equals("몸무게")) {
                        Toast.makeText(SignInActivity.this, "몸무게를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else if (birth.equals("나이")) {
                        Toast.makeText(SignInActivity.this, "나이를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {

                        editor.putString("email",getemail);
                        editor.putString("password",getpw);
                        editor.putString("nickname",nickname);

                        SignTask task = new SignTask();
                        task.execute("http://20.194.57.6/homet/SignUp.php", getemail, getpw, isMale, birth, nickname, Height, Weight, inbody, "general");
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "비밀번호 확인이 다릅니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Login =findViewById(R.id.login);
        Login.setOnClickListener(n->{
            Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
    class SignTask extends AsyncTask<String, Void, String> {


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

                    CreateTable task = new CreateTable();
                    task.execute("http://20.194.57.6/homet/CreateTable.php",getemail);

                    Toast.makeText(SignInActivity.this, "가입 완료.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInActivity.this, BleConnectionActivity.class);
                    startActivity(intent);
                }else if(Respon.equals("EMail_Duplication")){
                    Toast.makeText(SignInActivity.this, "이미 가입되어있는 이메일 입니다.", Toast.LENGTH_SHORT).show();
                }else if(Respon.contains("Password")){
                    Toast.makeText(SignInActivity.this, "비밀번호는 숫자 4자리를 사용해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SignInActivity.this, result, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
                Toast.makeText(SignInActivity.this, result, Toast.LENGTH_SHORT).show();
            }



        }

        @Override
        protected String doInBackground(String... params) {

            String email  = (String)params[1];
            String password  = (String)params[2];
            String ismale  = (String)params[3];
            String birth  = (String)params[4];
            String nickname  = (String)params[5];
            String height  = (String)params[6];
            String weight  = (String)params[7];
            String inbody  = (String)params[8];
            String type = (String) params[9];



            String postParameters = "email=" + email +"&"+ "password=" + password+"&"+ "ismale=" + ismale+"&"+ "birth=" + birth
                    +"&"+ "nickname=" + nickname+"&"+ "height=" + height+"&"+ "weight=" + weight+"&"+ "inbody=" + inbody+"&"+ "type=" + type;


            try {

                URL url = new URL("http://20.194.57.6/homet/SignUp.php");
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
        if (resultCode == 1) {

            if (requestCode ==1) {
                String male = data.getStringExtra("isMale");
                switch (male){
                    case "Male" :
                        male = "남자";
                        break;
                    case "FeMale":
                        male ="여자";
                }
                tvIsMale.setText(male);
            }else if(requestCode ==2){
                String height = data.getStringExtra("height");
                tvHeight.setText(height+"cm");
            }else if(requestCode ==3){
                String weight = data.getStringExtra("Weight");
                tvWeight.setText(weight+"kg");
            }else if(requestCode ==4){
                String birth = data.getStringExtra("birth");
                tvBirth.setText(birth+ " 세");
            }else if(requestCode == 5){
                String nickname = data.getStringExtra("Nickname");
                tvNickname.setText(nickname);

            }


        }
    }


    class CreateTable extends AsyncTask<String, Void, String> {


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
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            }



        }

        @Override
        protected String doInBackground(String... params) {

            String email  = (String)params[1];



            String postParameters = "email=" + email;


            try {

                URL url = new URL("http://20.194.57.6/homet/CreateTable.php");
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

}
