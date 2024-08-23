package com.example.ukrposhtaboxing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BoxingInfoAdapter extends BaseAdapter {
    private Context context;
    private List<BoxingInfo> boxingInfoList;
    private LayoutInflater inflater;

    public BoxingInfoAdapter(Context context, List<BoxingInfo> boxingInfoList) {
        this.context = context;
        this.boxingInfoList = boxingInfoList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return boxingInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return boxingInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_boxing_info, parent, false);
        }

        BoxingInfo boxingInfo = boxingInfoList.get(position);

        TextView text1 = convertView.findViewById(R.id.text1);
        TextView text2 = convertView.findViewById(R.id.text2);

        String locality = boxingInfo.getLocality();
        String trackingNumber = boxingInfo.getTrackingNumber();
        String name = boxingInfo.getName();
        double postpaid = boxingInfo.getPostpaid();
        double delivery = boxingInfo.getDelivery();
        double commission = boxingInfo.getCommission();

        text1.setText(String.format("Місто | Село: %s - %s", locality, trackingNumber));
        text2.setText(String.format("Отримувач: %s\nПісля плата: %.2f\nДоставка: %.2f\nПереказ: %.2f\nРазом до оплати: %.2f",
                name, postpaid, delivery, commission,(postpaid+delivery+commission)));

        return convertView;
    }

}
