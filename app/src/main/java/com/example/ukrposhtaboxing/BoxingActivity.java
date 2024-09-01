package com.example.ukrposhtaboxing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.view.GestureDetectorCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

// Ваша активність
public class BoxingActivity extends Activity {

    private static final int REQUEST_CODE_ADD = 1;
    private static final int REQUEST_CODE_EDIT = 2;
    private static final String PREFS_NAME = "BoxingPrefs";
    private static final String KEY_BOXING_INFO_LIST = "BoxingInfoList";

    private EditText searchEditText;
    private Button searchButton;
    private ListView listView;
    private BoxingInfoAdapter adapter;
    private ArrayList<BoxingInfo> boxingInfoList;
    private GestureDetectorCompat gestureDetector;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private ArrayList<BoxingInfo> originalBoxingInfoList;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boxing);

        listView = findViewById(R.id.listView);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        Button buttonAdd = findViewById(R.id.button3);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();

        originalBoxingInfoList = loadBoxingInfoList(); // Завантажуємо повний список
        boxingInfoList = new ArrayList<>(originalBoxingInfoList); // Копіюємо для фільтрації
        adapter = new BoxingInfoAdapter(this, boxingInfoList);
        listView.setAdapter(adapter);




        // Ініціалізація GestureDetector
        GestureListener gestureListener = new GestureListener();
        gestureDetector = new GestureDetectorCompat(this, gestureListener);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString();
                adapter.filter(query);
            }
        });
        // Обробка подвійного кліку
        listView.setOnTouchListener((v, event) -> {
            if (gestureDetector.onTouchEvent(event)) {
                int position = listView.pointToPosition((int) event.getX(), (int) event.getY());
                if (position != ListView.INVALID_POSITION) {
                    BoxingInfo boxingInfo = boxingInfoList.get(position);

                    // Додаємо опис про видачу посилки
                    String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    boxingInfo.setAdditionalInfo("Посилка видана: " + currentDateTime);
                    boxingInfo.setSelected(true); // Помічаємо як видану

                    adapter.notifyDataSetChanged();
                    saveBoxingInfoList(); // Зберігаємо дані після зміни
                }
                return true;
            }
            return false;
        });

        // Заповнення списку
//        for (int i = 1; i <= 10; i++) {
//            String trackingNumber = "UA123456789" + i;
//            String locality = "Місто " + i;
//            String name = "Одержувач " + i;
//            double postpaid = i * 10.0;
//            double delivery = i * 5.0;
//            double commission = i * 2.0;
//
//            BoxingInfo boxingInfo = new BoxingInfo(trackingNumber, locality, name, postpaid, delivery, commission);
//            boxingInfoList.add(boxingInfo);
//        }

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
    protected void onPause() {
        super.onPause();
        saveBoxingInfoList(); // Зберігаємо дані при паузі
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
            originalBoxingInfoList.add(0, boxingInfo); // Додаємо до оригінального списку
            boxingInfoList.add(0, boxingInfo); // Додаємо до поточного списку
            adapter.notifyDataSetChanged();
            saveBoxingInfoList(); // Зберігаємо дані після додавання
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
                saveBoxingInfoList(); // Зберігаємо дані після редагування
            }
        }
    }

    // Метод для створення контекстного меню
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_boxing, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        BoxingInfo selectedBoxingInfo = boxingInfoList.get(info.position);

        MenuItem item3 = menu.findItem(R.id.item3);
        if (selectedBoxingInfo.isSelected()) {
            item3.setVisible(true);
        } else {
            item3.setVisible(false);
        }
    }

    // Метод для обробки вибраного елемента меню
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemId = item.getItemId();
        BoxingInfo selectedBoxingInfo = boxingInfoList.get(info.position);

        if (itemId == R.id.item1) {
            Intent intent = new Intent(BoxingActivity.this, AddBoxingInfoActivity.class);

            // Передаємо дані для редагування
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
            saveBoxingInfoList(); // Зберігаємо дані після видалення
            Toast.makeText(this, "Елемент видалено", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.item3) {
            if (selectedBoxingInfo.isSelected()) {
                // Очистити додатковий опис і зняти зелений колір
                selectedBoxingInfo.setAdditionalInfo("");
                selectedBoxingInfo.setSelected(false);
                adapter.notifyDataSetChanged();
                saveBoxingInfoList(); // Зберігаємо дані після скасування видачі
                Toast.makeText(this, "Видачу посилки скасовано", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Цей пункт не видано", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    private void saveBoxingInfoList() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(originalBoxingInfoList); // Зберігаємо повний список
        editor.putString(KEY_BOXING_INFO_LIST, json);
        editor.apply();
    }



    private ArrayList<BoxingInfo> loadBoxingInfoList() {
        String json = sharedPreferences.getString(KEY_BOXING_INFO_LIST, null);
        Type type = new TypeToken<ArrayList<BoxingInfo>>() {}.getType();
        return json != null ? gson.fromJson(json, type) : new ArrayList<>();
    }


}
