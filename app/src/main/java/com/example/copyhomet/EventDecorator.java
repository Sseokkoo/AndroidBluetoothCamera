package com.example.copyhomet;

import android.app.Activity;
import android.graphics.Color;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

//    private int color;
    private int back;
    private HashSet<CalendarDay> dates;

    public EventDecorator(int back, Collection<CalendarDay> dates, Activity context) {

        this.back = back;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
//        view.setBackgroundDrawable(drawable);

//        view.setSelectionDrawable(drawable); //테두리
        view.addSpan(new DotSpan(5, Color.RED)); // 날짜밑에 점
    }
}