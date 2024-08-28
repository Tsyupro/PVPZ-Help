package com.example.ukrposhtaboxing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddBoxingInfoActivity extends Activity {

    private EditText editTextLocality;
    private EditText editTextTrackingNumber;
    private EditText editTextName;
    private EditText editTextPostpaid;
    private EditText editTextDelivery;
    private EditText editTextCommission;
    private Button buttonSave;

    private int position = -1; // Позиція елемента для редагування

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_boxing_info);

        editTextLocality = findViewById(R.id.editTextLocality);
        editTextTrackingNumber = findViewById(R.id.editTextTrackingNumber);
        editTextName = findViewById(R.id.editTextName);
        editTextPostpaid = findViewById(R.id.editTextPostpaid);
        editTextDelivery = findViewById(R.id.editTextDelivery);
        editTextCommission = findViewById(R.id.editTextCommission);
        buttonSave = findViewById(R.id.buttonSave);

        // Перевірка, чи активність була викликана для редагування
        Intent intent = getIntent();
        if (intent.hasExtra("position")) {
            position = intent.getIntExtra("position", -1);
            editTextLocality.setText(intent.getStringExtra("locality"));
            editTextTrackingNumber.setText(intent.getStringExtra("trackingNumber"));
            editTextName.setText(intent.getStringExtra("name"));
            editTextPostpaid.setText(String.valueOf(intent.getDoubleExtra("postpaid", 0)));
            editTextDelivery.setText(String.valueOf(intent.getDoubleExtra("delivery", 0)));
            editTextCommission.setText(String.valueOf(intent.getDoubleExtra("commission", 0)));
            buttonSave.setText("Оновити");
        } else {
            buttonSave.setText("Зберегти");
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("position", position);
                resultIntent.putExtra("locality", editTextLocality.getText().toString());
                resultIntent.putExtra("trackingNumber", editTextTrackingNumber.getText().toString());
                resultIntent.putExtra("name", editTextName.getText().toString());
                resultIntent.putExtra("postpaid", Double.parseDouble(editTextPostpaid.getText().toString()));
                resultIntent.putExtra("delivery", Double.parseDouble(editTextDelivery.getText().toString()));
                resultIntent.putExtra("commission", Double.parseDouble(editTextCommission.getText().toString()));
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
