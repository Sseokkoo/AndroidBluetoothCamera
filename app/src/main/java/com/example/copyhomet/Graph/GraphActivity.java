package com.example.copyhomet.Graph;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GraphActivity extends AppCompatActivity {

    /**그래프 표시**/

    BarChart barChart;
    TextView tvName;

    ArrayList<String> GetDate = new ArrayList<>();
    ArrayList<String> GetDateXValue = new ArrayList<>();

    ArrayList<String> MonthArrayM = new ArrayList<>();
    ArrayList<String> useTimeArrayM = new ArrayList<>();

    ArrayList<String> MonthArrayS = new ArrayList<>();
    ArrayList<String> useTimeArrayS = new ArrayList<>();
    Button SelectGraphDate;

    TextView xValue;

    String email;
    String nickname;

    LinearLayout close;

    int GraphCode;
    XAxis xAxis;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_graph);

        getSupportActionBar().hide();

        close = findViewById(R.id.close);
        close.setOnClickListener(n->{
            onBackPressed();
        });
        GraphCode = 3001;

        xValue = findViewById(R.id.xValue);

        barChart = findViewById(R.id.total_bar_graph);
        tvName = findViewById(R.id.tvName);
        SharedPreferences pref = getApplication().getSharedPreferences("info",MODE_PRIVATE);

       email = pref.getString("email","");
       nickname= pref.getString("nickname","");
        tvName.setText(nickname + "님 운동시간 그래프");
        String modeM = "M";

        long now = System.currentTimeMillis();
        Date Todate = new Date(now);
        SimpleDateFormat sdfNOW = new SimpleDateFormat("yyyy");
        String year = sdfNOW.format(Todate);

        TextView tvDate = findViewById(R.id.tvDate);
        tvDate.setText(year+"년 통계");

        // 일년  근육모드(M)
        GetDataYearM task = new GetDataYearM();
        task.execute("http://20.194.57.6/homet/GetExData_Year.php", email, year,modeM);

        // 일년  다이어트 모드(S)
        String modeS = "S";
        GetDataYearS task2 = new GetDataYearS();
        task2.execute("http://20.194.57.6/homet/GetExData_Year.php", email, year,modeS);

        xValue.setText("단위 (월)");
//        Handler mHandler = new Handler(Looper.getMainLooper());
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//        SetBarGraph();
//            }
//        }, 1000);


        SelectGraphDate = findViewById(R.id.btnSelectDate);
        SelectGraphDate.setOnClickListener(n->{
            Intent intent = new Intent(GraphActivity.this,DialogSelectGraphValue.class);
            startActivityForResult(intent,3000);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode ==3000){
                if(resultCode==3001){
                    GraphCode = 3001;
                    MonthArrayM = new ArrayList<>();
                     useTimeArrayM = new ArrayList<>();

                    MonthArrayS = new ArrayList<>();
                    useTimeArrayS = new ArrayList<>();

                    String SelectYear = data.getStringExtra("SelectYear");
                    SharedPreferences pref = getApplication().getSharedPreferences("info",MODE_PRIVATE);

                    TextView tvDate = findViewById(R.id.tvDate);
                    tvDate.setText(SelectYear+"년 통계");

                    tvName.setText(nickname + "님 운동시간 그래프");
                    String modeM = "M";
                    String modeS = "S";


                    // 일년  근육모드(M)
                    GetDataYearM task = new GetDataYearM();
                    task.execute("http://20.194.57.6/homet/GetExData_Year.php", email, SelectYear,modeM);

                    // 일년  다이어트 모드(S)
                    GetDataYearS task2 = new GetDataYearS();
                    task2.execute("http://20.194.57.6/homet/GetExData_Year.php", email, SelectYear,modeS);
                    xValue.setText("단위 (월)");
                }else if(resultCode == 3002){
                    GraphCode = 3002;
                    String StartDate = data.getStringExtra("start");
                    String EndDate = data.getStringExtra("end");



                    GetDate = new ArrayList<>();
                    GetDateXValue = new ArrayList<>();
                    Calendar cal = Calendar.getInstance();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date Start = null;
                    try {
                        Start = df.parse(StartDate);


                    cal.setTime(Start);

                    String End = StartDate;
                    while(!End.equals(EndDate)){
                        GetDate.add(df.format(cal.getTime()));

                        End = End.substring(5).replace("-","/");

                        String[] DateS = End.split("/");
                        if(DateS[0].substring(0,1).equals("0")){
                            End= DateS[0].substring(1)+"/"+DateS[1];
                        }

                        GetDateXValue.add(End.replace("-","/"));
                        cal.add(Calendar.DATE,1);
                        End = df.format(cal.getTime());

                    }
                    GetDate.add(EndDate);

                    String[] DateS = EndDate.substring(5).replace("-","/").split("/");
                    if(DateS[0].substring(0,1).equals("0")){
                        End= DateS[0].substring(1)+"/"+DateS[1];
                    }
                    GetDateXValue.add(EndDate.substring(5).replace("-","/"));



                    TextView tvDate = findViewById(R.id.tvDate);
                    tvDate.setText(StartDate+" ~ "+EndDate);

                    MonthArrayM = new ArrayList<>();
                    useTimeArrayM = new ArrayList<>();

                    MonthArrayS = new ArrayList<>();
                    useTimeArrayS = new ArrayList<>();

                    SharedPreferences pref = getApplication().getSharedPreferences("info",MODE_PRIVATE);

                    tvName.setText(nickname + "님 운동시간 그래프");
                    String modeM = "M";
                    String modeS = "S";


                    // 날짜 선택  근육모드(M)
                    GetDateSelectDayM task = new GetDateSelectDayM();
                    task.execute("http://20.194.57.6/homet/GetExData_Year.php", email, StartDate,EndDate,modeM);

                    // 날짜 선택  모드(S)
                    GetDateSelectDayS task2 = new GetDateSelectDayS();
                    task2.execute("http://20.194.57.6/homet/GetExData_Year.php", email, StartDate,EndDate,modeS);

                    xValue.setText("단위 (일)");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

    }

    /**일년치 데이터 가져오기**/
    class GetDataYearM extends AsyncTask<String, Void, String> {


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


                    String Month = subJsonObject.getString("Month");
                    String useTime = subJsonObject.getString("useTime");

//                    MonthArrayM.add(Month);
                    useTimeArrayM.add(useTime);

                    Log.d("Month",Month);
                    Log.d("useTime",useTime);



                }
                if(MonthArrayM.size()==0) {
                    MonthArrayM.add("1");
                    MonthArrayM.add("2");
                    MonthArrayM.add("3");
                    MonthArrayM.add("4");
                    MonthArrayM.add("5");
                    MonthArrayM.add("6");
                    MonthArrayM.add("7");
                    MonthArrayM.add("8");
                    MonthArrayM.add("9");
                    MonthArrayM.add("10");
                    MonthArrayM.add("11");
                    MonthArrayM.add("12");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        @Override
        protected String doInBackground(String... params) {

            String email =(String) params[1];
            String year = (String) params[2];
            String mode = (String) params[3];

            String postParameters = "email=" + email+"&"+"year="+year+"&"+"mode="+mode;


            try {

                URL url = new URL("http://20.194.57.6/homet/GetExData_Year.php");
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
    class GetDataYearS extends AsyncTask<String, Void, String> {


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


                    String Month = subJsonObject.getString("Month");
                    String useTime = subJsonObject.getString("useTime");

//                    MonthArrayS.add(Month);
                    useTimeArrayS.add(useTime);

//                    Log.d("Month",Month);
//                    Log.d("useTime",useTime);
                    if(MonthArrayS.size()==0) {
                        MonthArrayS.add("1");
                        MonthArrayS.add("2");
                        MonthArrayS.add("3");
                        MonthArrayS.add("4");
                        MonthArrayS.add("5");
                        MonthArrayS.add("6");
                        MonthArrayS.add("7");
                        MonthArrayS.add("8");
                        MonthArrayS.add("9");
                        MonthArrayS.add("10");
                        MonthArrayS.add("11");
                        MonthArrayS.add("12");
                    }


                }


                SetBarGraph();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String email =(String) params[1];
            String year = (String) params[2];
            String mode = (String) params[3];

            String postParameters = "email=" + email+"&"+"year="+year+"&"+"mode="+mode;


            try {

                URL url = new URL("http://20.194.57.6/homet/GetExData_Year.php");
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

    /**일년치 데이터 가져오기**/
    class GetDateSelectDayM extends AsyncTask<String, Void, String> {


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


                    String Date = subJsonObject.getString("Date");
                    String useTime = subJsonObject.getString("useTime");


                    MonthArrayM.add(Date);
                    useTimeArrayM.add(useTime);



                    Log.d("Month",Date);
                    Log.d("useTime",useTime);



                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        @Override
        protected String doInBackground(String... params) {

            String email =(String) params[1];
            String selectStartDay = (String) params[2];
            String selectEndDay  = (String) params[3];
            String mode = (String) params[4];

            String postParameters = "email=" + email+"&"+"selectStartDay="+selectStartDay+"&"+"selectEndDay="+selectEndDay+"&"+"mode="+mode;


            try {

                URL url = new URL("http://20.194.57.6/homet/GetExData_Select.php");
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
    class GetDateSelectDayS extends AsyncTask<String, Void, String> {


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



                    String Date = subJsonObject.getString("Date");
                    String useTime = subJsonObject.getString("useTime");


                    MonthArrayS.add(Date);
                    useTimeArrayS.add(useTime);

//                    Log.d("Month",Month);
//                    Log.d("useTime",useTime);



                }


                SetDateBarGraph();


            } catch (JSONException e) {
                e.printStackTrace();
            }




        }


        @Override
        protected String doInBackground(String... params) {

            String email =(String) params[1];
            String selectStartDay = (String) params[2];
            String selectEndDay  = (String) params[3];
            String mode = (String) params[4];

            String postParameters = "email=" + email+"&"+"selectStartDay="+selectStartDay+"&"+"selectEndDay="+selectEndDay+"&"+"mode="+mode;


            try {

                URL url = new URL("http://20.194.57.6/homet/GetExData_Select.php");
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


    public void SetBarGraph(){

        RoundedBarChartRenderer roundedBarChartRenderer= new RoundedBarChartRenderer(barChart,barChart.getAnimator(),barChart.getViewPortHandler());
        roundedBarChartRenderer.setmRadius(20f);
        barChart.setRenderer(roundedBarChartRenderer);
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<BarEntry> barEntries1 = new ArrayList<>();
        ArrayList<BarEntry> barEntries2 = new ArrayList<>();
        ArrayList<BarEntry> barEntries3 = new ArrayList<>();




        for(int m=0; m< useTimeArrayM.size(); m++){
            barEntries2.add(new BarEntry(m, Float.parseFloat(useTimeArrayM.get(m))));
        }

        for(int m=0; m< useTimeArrayS.size(); m++){
            barEntries3.add(new BarEntry(m, Float.parseFloat(useTimeArrayS.get(m))));
        }

        if(barEntries2.size()==0 && barEntries3.size()==0){
            barEntries2.add(new BarEntry(0,0));
            barEntries3.add(new BarEntry(0,0));


            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setDrawLabels(false);
            YAxis rightAxis = barChart.getAxisRight();
            rightAxis.setDrawLabels(false);
        }else{
            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setDrawLabels(true);
            YAxis rightAxis = barChart.getAxisRight();
            rightAxis.setDrawLabels(true);
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"DATA SET 1");
        barDataSet.setColor(Color.parseColor("#F44336"));
        BarDataSet barDataSet1 = new BarDataSet(barEntries1,"DATA SET 1");
        barDataSet1.setColors(Color.parseColor("#9C27B0"));



        BarDataSet barDataSet2 = new BarDataSet(barEntries2,"근육");
        barDataSet2.setColors(Color.parseColor("#9a0d1e"));
        BarDataSet barDataSet3 = new BarDataSet(barEntries3,"다이어트");
        barDataSet3.setColors(Color.parseColor("#5355c3"));


        barDataSet2.setDrawValues(false);
        barDataSet3.setDrawValues(false);





        BarData data = new BarData(barDataSet,barDataSet1,barDataSet2,barDataSet3);
        barChart.setData(data);

        xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(MonthArrayM));
        barChart.getAxisLeft().setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);
//        xAxis.setLabelCount(MonthArrayM.size());
        xAxis.setTextSize(16);
        barChart.setExtraBottomOffset(5);
        xAxis.setLabelCount(12);


        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularity(1f);


        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setGranularity(1f);

        float barSpace = 0.02f;
        float groupSpace = 0.3f;
//        int groupCount = barEntries2.size();
        int groupCount = MonthArrayM.size();


        //IMPORTANT *****
        data.setBarWidth(0.15f);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        barChart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
        //***** IMPORTANT


        GraphMarkerView marker = new GraphMarkerView(this, R.layout.markerview);
        marker.setChartView(barChart);
        barChart.setMarker(marker);
        barChart.getDescription().setEnabled(false);
        barChart.invalidate();


    }
    public void SetDateBarGraph(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<BarEntry> barEntries1 = new ArrayList<>();
        ArrayList<BarEntry> barEntries2 = new ArrayList<>();
        ArrayList<BarEntry> barEntries3 = new ArrayList<>();



        for(int c = 0; c<GetDate.size(); c++) {
            for(int m =0; m< MonthArrayM.size(); m++) {
                if (MonthArrayM.contains(GetDate.get(c))) {
                    if (GetDate.get(c).equals(MonthArrayM.get(m))) {
                        barEntries1.add(new BarEntry(c, Float.parseFloat(useTimeArrayM.get(m))));
                        break;
                    }
                } else {
                    barEntries1.add(new BarEntry(c, 0));
                    break;
                }
            }

                    for(int s =0; s< MonthArrayS.size(); s++) {
                        if (MonthArrayS.contains(GetDate.get(c))) {
                            if (GetDate.get(c).equals(MonthArrayS.get(s))) {
                                barEntries2.add(new BarEntry(c, Float.parseFloat(useTimeArrayS.get(s))));
                                break;
                            }
                        } else {
                            barEntries2.add(new BarEntry(c, 0));
                            break;
                        }
                    }

        }



        if(barEntries1.size()==0 && barEntries2.size()==0){
            barEntries1.add(new BarEntry(0,0));
            barEntries2.add(new BarEntry(0,0));


            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setDrawLabels(false);
            YAxis rightAxis = barChart.getAxisRight();
            rightAxis.setDrawLabels(false);
        }else{
            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setDrawLabels(true);
            YAxis rightAxis = barChart.getAxisRight();
            rightAxis.setDrawLabels(true);
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"DATA SET 1");
        barDataSet.setColor(Color.parseColor("#F44336"));
        BarDataSet barDataSet1 = new BarDataSet(barEntries1,"근육");
        barDataSet1.setColors(Color.parseColor("#9a0d1e"));
        BarDataSet barDataSet2 = new BarDataSet(barEntries2,"다이어트");
        barDataSet2.setColors(Color.parseColor("#5355c3"));
        BarDataSet barDataSet3 = new BarDataSet(barEntries3,"다이어트");
        barDataSet3.setColors(Color.parseColor("#5355c3"));


        barDataSet1.setDrawValues(false);
        barDataSet2.setDrawValues(false);





        BarData data = new BarData(barDataSet,barDataSet1,barDataSet2,barDataSet3);
        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(GetDateXValue));
        barChart.getAxisLeft().setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(4);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularity(1f);


        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setGranularity(1f);

        float barSpace = 0.02f;
        float groupSpace = 0.32f;
//        int groupCount = barEntries2.size();
        int groupCount = GetDateXValue.size();


        //IMPORTANT *****
        data.setBarWidth(0.15f);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        barChart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
        //***** IMPORTANT


        GraphMarkerView marker = new GraphMarkerView(this, R.layout.markerview);
        marker.setChartView(barChart);
        barChart.setMarker(marker);

        barChart.invalidate();


    }

}
