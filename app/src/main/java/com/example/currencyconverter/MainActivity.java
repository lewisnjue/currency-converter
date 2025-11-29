package com.example.currencyconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private EditText editAmount;
    private Button buttonConvert;
    private TextView textResult;

    // rates: 1 unit of currency = USD value
    private static final Map<String, Double> RATES_TO_USD = new HashMap<>();

    static {
        RATES_TO_USD.put("USD", 1.0);
        RATES_TO_USD.put("EUR", 1.08); // 1 EUR = 1.08 USD
        RATES_TO_USD.put("GBP", 1.25); // 1 GBP = 1.25 USD
        RATES_TO_USD.put("JPY", 0.0072); // 1 JPY = 0.0072 USD
        RATES_TO_USD.put("CNY", 0.14); // 1 CNY = 0.14 USD
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerFrom = findViewById(R.id.spinner_from);
        spinnerTo = findViewById(R.id.spinner_to);
        editAmount = findViewById(R.id.edit_amount);
        buttonConvert = findViewById(R.id.button_convert);
        textResult = findViewById(R.id.text_result);

        final String[] currencies = RATES_TO_USD.keySet().toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convert();
            }
        });
    }

    private void convert() {
        String from = spinnerFrom.getSelectedItem().toString();
        String to = spinnerTo.getSelectedItem().toString();
        String amtStr = editAmount.getText().toString().trim();
        if (amtStr.isEmpty()) {
            Toast.makeText(this, "Enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amtStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show();
            return;
        }

        Double rateFrom = RATES_TO_USD.get(from);
        Double rateTo = RATES_TO_USD.get(to);
        if (rateFrom == null || rateTo == null) {
            Toast.makeText(this, "Unsupported currency", Toast.LENGTH_SHORT).show();
            return;
        }

        // convert: amount_in_usd = amount * rateFrom (because rateFrom = USD per 1 unit of from)
        double amountInUsd = amount * rateFrom;
        double result = amountInUsd / rateTo;

        textResult.setText(String.format("%.4f %s", result, to));
    }
}
