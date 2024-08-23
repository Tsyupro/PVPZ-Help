package com.example.ukrposhtaboxing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.view.GestureDetectorCompat;

import java.util.ArrayList;

public class BoxingActivity extends Activity {

    private static final int REQUEST_CODE_ADD = 1;
    private static final int REQUEST_CODE_EDIT = 2;
    private ListView listView;
    private BoxingInfoAdapter adapter;
    private ArrayList<BoxingInfo> boxingInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boxing);

        listView = findViewById(R.id.listView);
        Button buttonAdd = findViewById(R.id.button3);

        boxingInfoList = new ArrayList<>();
        adapter = new BoxingInfoAdapter(this, boxingInfoList);
        listView.setAdapter(adapter);

        // Реєструємо контекстне меню для ListView
        registerForContextMenu(listView);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoxingActivity.this, AddBoxingInfoActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
            String locality = data.getStringExtra("locality");
            String trackingNumber = data.getStringExtra("trackingNumber");
            String name = data.getStringExtra("name");
            double postpaid = data.getDoubleExtra("postpaid", 0);
            double delivery = data.getDoubleExtra("delivery", 0);
            double commission = data.getDoubleExtra("commission", 0);

            BoxingInfo boxingInfo = new BoxingInfo(trackingNumber, locality, name, postpaid, delivery, commission);
            boxingInfoList.add(boxingInfo);
            adapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                BoxingInfo updatedBoxingInfo = boxingInfoList.get(position);
                updatedBoxingInfo.setTrackingNumber(data.getStringExtra("trackingNumber"));
                updatedBoxingInfo.setLocality(data.getStringExtra("locality"));
                updatedBoxingInfo.setName(data.getStringExtra("name"));
                updatedBoxingInfo.setPostpaid(data.getDoubleExtra("postpaid", 0));
                updatedBoxingInfo.setDelivery(data.getDoubleExtra("delivery", 0));
                updatedBoxingInfo.setCommission(data.getDoubleExtra("commission", 0));

                adapter.notifyDataSetChanged();
            }
        }
    }

    // Метод для створення контекстного меню
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_boxing, menu);
    }

    // Метод для обробки вибраного елемента меню
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemId = item.getItemId();

        if (itemId == R.id.item1) {
            Intent intent = new Intent(BoxingActivity.this, AddBoxingInfoActivity.class);

            // Передаємо дані для редагування
            BoxingInfo selectedBoxingInfo = boxingInfoList.get(info.position);
            intent.putExtra("position", info.position);
            intent.putExtra("trackingNumber", selectedBoxingInfo.getTrackingNumber());
            intent.putExtra("locality", selectedBoxingInfo.getLocality());
            intent.putExtra("name", selectedBoxingInfo.getName());
            intent.putExtra("postpaid", selectedBoxingInfo.getPostpaid());
            intent.putExtra("delivery", selectedBoxingInfo.getDelivery());
            intent.putExtra("commission", selectedBoxingInfo.getCommission());

            startActivityForResult(intent, REQUEST_CODE_EDIT);
            return true;
        } else if (itemId == R.id.item2) {
            boxingInfoList.remove(info.position);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Елемент видалено", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }
}
