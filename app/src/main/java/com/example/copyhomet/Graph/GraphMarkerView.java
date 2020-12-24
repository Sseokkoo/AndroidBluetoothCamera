package com.example.copyhomet.Graph;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.example.copyhomet.R;

public class GraphMarkerView extends MarkerView {

    private TextView tvMarker;


    public GraphMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvMarker = findViewById(R.id.tvMarker);



    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        String y = String.format("%.0f",e.getY());
        if(!y.equals("0")) {
            tvMarker.setText(y);
        }else{
            tvMarker.setText("");
        }
    }

    @Override
    public MPPointF getOffset() {

        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}