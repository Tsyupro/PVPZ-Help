package com.example.ukrposhtaboxing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BoxingInfoAdapter extends BaseAdapter {
    private Context context;
    private List<BoxingInfo> boxingInfoList;
    private List<BoxingInfo> originalList;
    private LayoutInflater inflater;

    public BoxingInfoAdapter(Context context, List<BoxingInfo> boxingInfoList) {
        this.context = context;
        this.boxingInfoList = boxingInfoList;
        this.originalList = new ArrayList<>(boxingInfoList);
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

        text1.setText(String.format("Місто | Село: %s - %s", boxingInfo.getLocality(), boxingInfo.getTrackingNumber()));
        text2.setText(String.format("Отримувач: %s\nПісля плата: %.2f\nДоставка: %.2f\nПереказ: %.2f\nРазом до оплати: %.2f\n%s",
                boxingInfo.getName(), boxingInfo.getPostpaid(), boxingInfo.getDelivery(), boxingInfo.getCommission(),
                (boxingInfo.getPostpaid() + boxingInfo.getDelivery() + boxingInfo.getCommission()), boxingInfo.getAdditionalInfo()));

        if (boxingInfo.isSelected()) {
            convertView.setBackgroundColor(Color.parseColor("#b7d5ad"));
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }

    public void filter(String query) {
        boxingInfoList.clear(); // Очищуємо поточний список

        // Якщо пошуковий запит порожній, повертаємо весь початковий список
        if (query.isEmpty()) {
            boxingInfoList.addAll(originalList);
        } else {
            // Фільтруємо за введеним запитом
            for (BoxingInfo info : originalList) {
                if (info.getTrackingNumber().toLowerCase().contains(query.toLowerCase()) ||
                        info.getLocality().toLowerCase().contains(query.toLowerCase()) ||
                        info.getName().toLowerCase().contains(query.toLowerCase())) {
                    boxingInfoList.add(info); // Додаємо елементи, що відповідають запиту
                }
            }
        }
        // Після кожної зміни оновлюємо адаптер
        notifyDataSetChanged();
    }

}

