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

import com.example.copyhomet.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyPageActivity extends AppCompatActivity {


    EditText edtNickname,edtHeight,edtAge,edtWeight;
    TextView edtGender,edtEmail;

    Button btnSave;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String iGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        getSupportActionBar().hide();

        pref = getSharedPreferences("info",MODE_PRIVATE);
        editor = pref.edit();


        edtEmail = findViewById(R.id.edtEmail);
        edtNickname = findViewById(R.id.edtNickname);
        edtGender = findViewById(R.id.edtGender);
        edtHeight = findViewById(R.id.edtHeight);
        edtAge = findViewById(R.id.edtAge);
        edtWeight = findViewById(R.id.edtWeight);
        btnSave = findViewById(R.id.btnSave);


        edtEmail.setText(pref.getString("email",""));
        edtNickname.setText(pref.getString("nickname",""));
        edtGender.setText(pref.getString("isMale",""));
        edtHeight.setText(pref.getString("Height",""));
        edtWeight.setText(pref.getString("Weight",""));
        edtAge.setText(pref.getString("birth",""));


        edtGender.setOnClickListener(n->{
            Intent intent = new Intent(MyPageActivity.this,DialogGender.class);
            if(edtGender.getText().toString().equals("남자")){
                iGender = "M";
            }else{
                iGender = "F";
            }
            intent.putExtra("Gender",iGender);
            startActivityForResult(intent,1);
        });




        btnSave.setOnClickListener(n->{

            String email = edtEmail.getText().toString();
            String isMale = edtGender.getText().toString();
            String birth = edtAge.getText().toString().replace("세","")+" 세";
            String nickname = edtNickname.getText().toString();
            String height = edtHeight.getText().toString().replace("c","").replace("m","")+"cm";
            String weight = edtWeight.getText().toString().replace("k","").replace("g","")+"kg";

            FixInfo task = new FixInfo();
            task.execute("http://20.194.57.6/homet/UpDateMemberInfo.php", email,  isMale, birth, nickname, height, weight);



            int iHeight = Integer.parseInt(height.replace("cm",""));
            int iWeight = Integer.parseInt(weight.replace("kg",""));
            double bmi = iWeight / ((iHeight *0.01)*(iHeight *0.01));
            String sBMI = String.format("%.2f",bmi);




            SharedPreferences pref = getApplicationContext().getSharedPreferences("info",MODE_PRIVATE);
            SharedPreferences.Editor editor =pref.edit();
            editor.putString("email",email);
            editor.putString("isMale",isMale);
            editor.putString("Height",height);
            editor.putString("Weight",weight);
            editor.putString("birth",birth);
            editor.putString("nickname",nickname);
            editor.putString("bmi",sBMI);
            editor.commit();

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode ==1) {
            String gender = data.getStringExtra("gender");
            if (gender.equals("male")) {
                edtGender.setText("남자");
            } else {
                edtGender.setText("여자");
            }
        }
    }


    class FixInfo extends AsyncTask<String, Void, String> {


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

                    Intent intent = new Intent(MyPageActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MyPageActivity.this,"fail",Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
                Toast.makeText(MyPageActivity.this,"fail",Toast.LENGTH_SHORT).show();
            }



        }

        @Override
        protected String doInBackground(String... params) {

            String email  = (String)params[1];
            String ismale  = (String)params[2];
            String birth  = (String)params[3];
            String nickname  = (String)params[4];
            String height  = (String)params[5];
            String weight  = (String)params[6];



            String postParameters = "email=" + email+"&"+ "ismale=" + ismale+"&"+ "birth=" + birth
                    +"&"+ "nickname=" + nickname+"&"+ "height=" + height+"&"+ "weight=" + weight;


            try {

                URL url = new URL("http://20.194.57.6/homet/UpDateMemberInfo.php");
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
