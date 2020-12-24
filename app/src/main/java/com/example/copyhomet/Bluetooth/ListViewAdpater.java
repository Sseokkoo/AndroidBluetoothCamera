package com.example.copyhomet.Bluetooth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.copyhomet.R;

import java.util.ArrayList;

class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ItemViewHolder> {



    public MyRecyclerViewClickListener mListener;
    public AddMemoClickListener AddMemoListener;
    private ArrayList<DataList> listData = new ArrayList<>();
    private Context context;





    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connected_device, parent, false);
        return new ItemViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));

        final int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onProductClicked(pos);
            }

        });

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
        TextView connectDeviceListItem;


//        private String FoodType;




        ItemViewHolder(View view) {
            super(view);

            connectDeviceListItem = view.findViewById(R.id.connectDeviceListItem);

        }
        public void onBind(DataList data) {
            this.data = data;
            String a = data.getDeviceName();
            connectDeviceListItem.setText(data.getDeviceName());

        }
    }
    public void setOnClickListener(MyRecyclerViewClickListener listener) {
        mListener = listener;

    }
    public interface  MyRecyclerViewClickListener {

        void onProductClicked(int position);
    }
    public void setAddMemoListener(AddMemoClickListener listener) {
        AddMemoListener = listener;

    }
    public interface  AddMemoClickListener {

        void AddMemoClicked(int position);
    }

    public static class DataList {
        private String DeviceName;


        public void setDeviceName(String tempUnit) {DeviceName = tempUnit;}


        public String getDeviceName() {return DeviceName;}
    }


}
