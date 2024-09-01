package com.example.ukrposhtaboxing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

        buttonSave.setEnabled(false);
        buttonSave.setBackgroundColor(getResources().getColor(R.color.gray));

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
        editTextLocality.addTextChangedListener(inputWatcher);
        editTextTrackingNumber.addTextChangedListener(inputWatcher);
        editTextName.addTextChangedListener(inputWatcher);

        checkInputFields();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("position", position);
                resultIntent.putExtra("locality", editTextLocality.getText().toString());
                resultIntent.putExtra("trackingNumber", editTextTrackingNumber.getText().toString());
                resultIntent.putExtra("name", editTextName.getText().toString());

                // Перевірка на порожність та присвоєння значення 0 за замовчуванням
                double postpaid = parseDoubleOrDefault(editTextPostpaid, 0);
                double delivery = parseDoubleOrDefault(editTextDelivery, 0);
                double commission = parseDoubleOrDefault(editTextCommission, 0);


                resultIntent.putExtra("postpaid", postpaid);
                resultIntent.putExtra("delivery", delivery);
                resultIntent.putExtra("commission", commission);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

    }
    private double parseDoubleOrDefault(EditText editText, double defaultValue) {
        String text = editText.getText().toString();
        if (text.isEmpty() || !text.matches("\\d+(\\.\\d+)?")) {
            // Перевірка, що введено число
            return defaultValue;
        }
        return Double.parseDouble(text);
    }
    private TextWatcher inputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkInputFields();
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void checkInputFields() {
        boolean isLocalityEmpty = editTextLocality.getText().toString().trim().isEmpty();
        boolean isTrackingNumberEmpty = editTextTrackingNumber.getText().toString().trim().isEmpty();
        boolean isNameEmpty = editTextName.getText().toString().trim().isEmpty();

        if (isLocalityEmpty) {
            editTextLocality.setError("Введіть будь ласка данні");
        }

        if (isTrackingNumberEmpty) {
            editTextTrackingNumber.setError("Введіть будь ласка данні");
        }

        if (isNameEmpty) {
            editTextName.setError("Введіть будь ласка данні");
        }

        // Деактивація або активація кнопки в залежності від стану полів
        boolean isButtonEnabled = !isLocalityEmpty && !isTrackingNumberEmpty && !isNameEmpty;
        buttonSave.setEnabled(isButtonEnabled);

        // Зміна кольору кнопки в залежності від стану
        int buttonColor = isButtonEnabled ? R.color.yellow : R.color.gray;
        buttonSave.setBackgroundColor(getResources().getColor(buttonColor));
    }

}
