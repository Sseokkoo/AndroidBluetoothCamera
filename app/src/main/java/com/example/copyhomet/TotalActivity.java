package com.example.copyhomet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.copyhomet.Bluetooth.DialogBleToMain;
import com.example.copyhomet.Graph.GraphActivity;
import com.example.copyhomet.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TotalActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    TotalAdapter adapter;


    ArrayList<String> AllDateArray = new ArrayList<>();
    ArrayList<String> DateArray = new ArrayList<>();
    ArrayList<String> ModeArray = new ArrayList<>();
    ArrayList<String> StartArray = new ArrayList<>();
    ArrayList<String> UseArray = new ArrayList<>();
    ArrayList<String> UseDeviceArray = new ArrayList<>();

    MaterialCalendarView materialCalendarView;
    String clickedDate;

    LinearLayout close;





    public static TotalActivity totalActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);

        getSupportActionBar().hide();

        close = findViewById(R.id.close);
        close.setOnClickListener(n->{
            onBackPressed();
        });


        totalActivity = this;
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        adapter = new TotalAdapter();
//        adapter.setOnClickListener(this);
        recyclerView.setAdapter(adapter);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("info", MODE_PRIVATE);
        String email = pref.getString("email", "");

        GetMemberInfo task = new GetMemberInfo();
        task.execute("http://20.194.57.6/homet/SignIn.php",email);

        materialCalendarView = findViewById(R.id.main_my_page_calendar);
//        materialCalendarView.addDecorators(
//                new SundayDecorator(),
//                new SaturdayDecorator(),
//                oneDayDecorator);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        materialCalendarView.setSelectedDate(date);


        materialCalendarView.setSelectionColor(Color.parseColor("#9a0d1e"));
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                String year = String.valueOf(date.getYear());
                String month = String.valueOf(date.getMonth()+1);
                if(month.length() == 1){
                    month = "0"+month;
                }
                String day = String.valueOf(date.getDay());
                if(day.length() == 1){
                    day = "0"+day;
                }


                clickedDate = year + "-" + month + "-" + day;
//                Toast.makeText(TotalActivity.this, clickedDate, Toast.LENGTH_SHORT).show();

                adapter.clearAlltems();
                adapter.notifyDataSetChanged();

                for(int i =0; i<DateArray.size(); i++){
                if(clickedDate.equals(DateArray.get(i))){
                    if(!UseDeviceArray.get(i).equals("")) {
                        String Devices[] = UseDeviceArray.get(i).replace(" ", "").split(",");
                        for (int c = 0; c< Devices.length; c++){
                            DataList data = new DataList();
                            data.setDate(DateArray.get(i));
                            data.setMode(ModeArray.get(i));
                            data.setStartTime(StartArray.get(i));
                            data.setUseTime(UseArray.get(i));
                            data.setUseDeivce(Devices[c]);
                            adapter.addItem(data);
                            adapter.notifyDataSetChanged();
                        }

                    }else{
                        DataList data = new DataList();
                        data.setDate(DateArray.get(i));
                        data.setMode(ModeArray.get(i));
                        data.setStartTime(StartArray.get(i));
                        data.setUseTime(UseArray.get(i));
                        data.setUseDeivce(UseDeviceArray.get(i));
                        adapter.addItem(data);
                        adapter.notifyDataSetChanged();
                    }

                    }
                }
                int a = adapter.getItemCount();
                if(adapter.getItemCount()==0){
//                    if(DateArray.size()==0){
                    TextView tvEmpty = findViewById(R.id.emptyData);
                    tvEmpty.setVisibility(View.VISIBLE);
                }else{
                    TextView tvEmpty = findViewById(R.id.emptyData);
                    tvEmpty.setVisibility(View.GONE);
                }
            }
        });

        Button btnGraph = findViewById(R.id.ShowGraph);
        btnGraph.setOnClickListener(n->{
            Intent intent = new Intent(TotalActivity.this, GraphActivity.class);
            startActivity(intent);
        });
    }

        private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -2);
            ArrayList<CalendarDay> dates = new ArrayList<>();
//            for (int i = 0; i < 30; i++) {
//                CalendarDay day = CalendarDay.from(calendar);
//                dates.add(day);
//                calendar.add(Calendar.DATE, 1);
//            }
            for(int i = 0; i< AllDateArray.size(); i++){
                String[] DATE = AllDateArray.get(i).split("-");
                int year = Integer.parseInt(DATE[0]);
                int month = Integer.parseInt(DATE[1]);
                int day = Integer.parseInt(DATE[2]);
                calendar.set(year,month-1,day);
                CalendarDay day1 = CalendarDay.from(calendar);
                dates.add(day1);
//                dates.add(day);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }
            materialCalendarView.addDecorator(new EventDecorator(1, calendarDays, TotalActivity.this));
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
                    String nickname = subJsonObject.getString("nickname");
                    String date = subJsonObject.getString("date");
                    String Mode = subJsonObject.getString("Mode");
                    String startTime = subJsonObject.getString("startTime");
                    String useTime = subJsonObject.getString("useTime");
                    String useDevice = subJsonObject.getString("useDevice");
                    AllDateArray.add(date);


                    DateArray.add(date);
                    ModeArray.add(Mode);
                    StartArray.add(startTime);
                    UseArray.add(useTime);
                    UseDeviceArray.add(useDevice);






                    long now = System.currentTimeMillis();
                    Date Todate = new Date(now);
                    SimpleDateFormat sdfNOW = new SimpleDateFormat("yyyy-MM-dd");
                    String DATE = sdfNOW.format(Todate);



//                    clickedDate = year + "-" + month + "-" + day;
                    clickedDate = DATE;
//                Toast.makeText(TotalActivity.this, clickedDate, Toast.LENGTH_SHORT).show();

                    adapter.clearAlltems();
                    adapter.notifyDataSetChanged();

                    for(int e =0; e<DateArray.size(); e++){
                        if(clickedDate.equals(DateArray.get(e))){
                            if(!UseDeviceArray.get(e).equals("")) {
                                String Devices[] = UseDeviceArray.get(e).replace(" ", "").split(",");
                                for (int c = 0; c< Devices.length; c++){
                                    DataList data = new DataList();
                                    data.setDate(DateArray.get(e));
                                    data.setMode(ModeArray.get(e));
                                    data.setStartTime(StartArray.get(e));
                                    data.setUseTime(UseArray.get(e));
                                    data.setUseDeivce(Devices[c]);
                                    adapter.addItem(data);
                                    adapter.notifyDataSetChanged();
                                }

                            }else{
                                DataList data = new DataList();
                                data.setDate(DateArray.get(e));
                                data.setMode(ModeArray.get(e));
                                data.setStartTime(StartArray.get(e));
                                data.setUseTime(UseArray.get(e));
                                data.setUseDeivce(UseDeviceArray.get(e));
                                adapter.addItem(data);
                                adapter.notifyDataSetChanged();
                            }



                        }
                    }
                    if(adapter.getItemCount()==0){
//                    if(DateArray.size()==0){
                        TextView tvEmpty = findViewById(R.id.emptyData);
                        tvEmpty.setVisibility(View.VISIBLE);
                    }else{
                        TextView tvEmpty = findViewById(R.id.emptyData);
                        tvEmpty.setVisibility(View.GONE);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            ApiSimulator task2 = new ApiSimulator();
            task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//            LineChartSet();

        }


        @Override
        protected String doInBackground(String... params) {

            String email =(String) params[1];

            String postParameters = "email=" + email;


            try {

                URL url = new URL("http://20.194.57.6/homet/GetExData.php");
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
        public static TotalActivity getTotalActivity(){
        return totalActivity;
        }

        public void setDialog(){
            Intent intent = new Intent(TotalActivity.this, DialogBleToMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
    }


