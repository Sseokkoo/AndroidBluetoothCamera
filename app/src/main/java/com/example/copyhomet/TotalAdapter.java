package com.example.copyhomet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.copyhomet.R;

import java.util.ArrayList;

public class TotalAdapter extends RecyclerView.Adapter<TotalAdapter.ItemViewHolder> {



    public MyRecyclerViewClickListener mListener;
    private ArrayList<DataList> listData = new ArrayList<>();
    private Context context;





    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_total, parent, false);
        return new ItemViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));

//        if (mListener != null) {
        final int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mListener.onProductClicked(pos);
            }

        });
//        }

    }
    public void clearAlltems(){
        listData.clear();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(DataList data) {
        listData.add(data);
    }

    public void deleteItem(DataList data){
        listData.remove(data);
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder  {

        private DataList data;

        private TextView Date;
        private TextView Mode;
        private TextView startTime;
        private TextView useTime;
        private TextView useDevice;



        ItemViewHolder(View view) {
            super(view);

            Date = view.findViewById(R.id.recycler_date);
            Mode = view.findViewById(R.id.recycler_mode);
            startTime = view.findViewById(R.id.recycler_startTime);
            useTime = view.findViewById(R.id.recycler_Usetime);
            useDevice = view.findViewById(R.id.recycler_UseDevice);

        }


        public void onBind(DataList data) {

            this.data = data;


            Date.setText(data.getDate());
            Mode.setText(data.getMode());
            startTime.setText(data.getStartTime());
            useTime.setText(data.getUseTime()+" min");
            useDevice.setText(data.getUseDeivce());


        }

    }


    public void setOnClickListener(MyRecyclerViewClickListener listener) {
        mListener = listener;

    }
    public interface  MyRecyclerViewClickListener {

        void onProductClicked(int position);
    }
}


class DataList {
    private String Date;
    private String Mode;
    private String StartTime;
    private String UseTime;
    private String UseDeivce;

    public void setUseDeivce(String device){UseDeivce = device;}
    public void setDate(String date){ Date = date; }
    public void setMode(String mode){Mode = mode;}
    public void setStartTime(String startTime){StartTime = startTime;}
    public void setUseTime(String useTime){UseTime = useTime;}

    public String getUseDeivce(){return UseDeivce;}
    public String getDate() {return  Date;}
    public String getMode(){return Mode;}
    public String getStartTime() { return StartTime;}
    public String getUseTime() {return UseTime;}



}
