package com.example.copyhomet.Graph;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.OrientationHelper;

import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.example.copyhomet.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarRange extends AppCompatActivity {


    /**달력 날짜 지정후 날짜 받아오기 ( 배열)**/

    CalendarView calendarView;
    Button btnSelect;

    String StartDate, EndDate;
    ArrayList<String> DATE = new ArrayList<>();

    String month2,day2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_range_library);

        btnSelect = findViewById(R.id.Select);
        btnSelect.setOnClickListener(n -> {
                List<Calendar> days = calendarView.getSelectedDates();
                String result = "";
                for (int i = 0; i < days.size(); i++) {
                    Calendar calendar = days.get(i);
                    final int day = calendar.get(Calendar.DAY_OF_MONTH);
                    final int month = calendar.get(Calendar.MONTH);
                    final int year = calendar.get(Calendar.YEAR);
                    String week = new SimpleDateFormat("EE").format(calendar.getTime());
//                String day_full = year + "년 " + (month + 1) + "월 " + day + "일 " + week + "요일";
                    if (String.valueOf(month+1).length() == 1) {
                        month2 = "0" + (month);
                    } else {
                        month2 = String.valueOf(month + 1);
                    }

                    if (String.valueOf(day).length() == 1) {
                        day2 = "0" + (day);
                    } else {
                        day2 = String.valueOf(day);
                    }

                    String day_full = year + "-" + month2 + "-" + day2;
                    result += (day_full + "\n");
                    DATE.add(day_full);

                }
//            Toast.makeText(CalendarRange.this, result, Toast.LENGTH_LONG).show();
            if(DATE.size()>2) {
                if (DATE.size() != 0 || DATE.size() != 1) {
                    StartDate = DATE.get(0);
                    EndDate = DATE.get(DATE.size() - 1);
                    Intent intent = new Intent();
                    intent.putExtra("start", StartDate);
                    intent.putExtra("end", EndDate);
                    setResult(3002, intent);
                    finish();
                } else {
                    Toast.makeText(CalendarRange.this, "날짜를 선택해주세요.", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this, "시작날짜와 마지막날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
        calendarView = findViewById(R.id.calendar_view);
        calendarView.setCalendarOrientation(OrientationHelper.HORIZONTAL);
        calendarView.setSelectedDayBackgroundColor(Color.parseColor("#6798FD"));
        calendarView.setSelectedDayBackgroundStartColor(Color.parseColor("#1C8AE8"));
        calendarView.setSelectedDayBackgroundEndColor(Color.parseColor("#1C8AE8"));
        calendarView.setSelectionType(SelectionType.RANGE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e){
        Rect dialogBounds = new Rect();
        getWindow().getDecorView().getHitRect(dialogBounds);
        if(!dialogBounds.contains((int)e.getX(),(int)e.getY())){
            return false;
        }
        return super.dispatchTouchEvent(e);
    }
}
