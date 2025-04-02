package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerFrom, spinnerTo;
    EditText editTextInput;
    Button btnConvert;
    TextView textViewResult;

    String[] units = {
            "Inch", "Foot", "Yard", "Mile",
            "Pound", "Ounce", "Ton",
            "Celsius", "Fahrenheit", "Kelvin"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        editTextInput = findViewById(R.id.editTextInput);
        btnConvert = findViewById(R.id.btnConvert);
        textViewResult = findViewById(R.id.textViewResult);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        btnConvert.setOnClickListener(v -> performConversion());
    }

    private void performConversion() {
        String from = spinnerFrom.getSelectedItem().toString();
        String to = spinnerTo.getSelectedItem().toString();
        String input = editTextInput.getText().toString().trim();

        if (input.isEmpty()) {
            textViewResult.setText(getString(R.string.error_empty_input));
            return;
        }

        double value;
        try {
            value = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            textViewResult.setText(getString(R.string.error_invalid_number));
            return;
        }

        if (from.equals(to)) {
            textViewResult.setText(String.format(getString(R.string.info_same_unit), value));
            return;
        }

        double result = convert(value, from, to);
        textViewResult.setText(String.format(getString(R.string.result_text), result, to));
        Toast.makeText(this, getString(R.string.toast_conversion_done), Toast.LENGTH_SHORT).show();
    }

    private double convert(double value, String from, String to) {
        double inCM = -1;
        switch (from) {
            case "Inch": inCM = value * 2.54; break;
            case "Foot": inCM = value * 30.48; break;
            case "Yard": inCM = value * 91.44; break;
            case "Mile": inCM = value * 160934; break;
        }

        if (inCM != -1) {
            switch (to) {
                case "Inch": return inCM / 2.54;
                case "Foot": return inCM / 30.48;
                case "Yard": return inCM / 91.44;
                case "Mile": return inCM / 160934;
            }
            return inCM;
        }

        double inG = -1;
        switch (from) {
            case "Pound": inG = value * 453.592; break;
            case "Ounce": inG = value * 28.3495; break;
            case "Ton": inG = value * 907185; break;
        }

        if (inG != -1) {
            switch (to) {
                case "Pound": return inG / 453.592;
                case "Ounce": return inG / 28.3495;
                case "Ton": return inG / 907185;
            }
            return inG;
        }

        if (from.equals("Celsius") && to.equals("Fahrenheit")) return (value * 1.8) + 32;
        if (from.equals("Fahrenheit") && to.equals("Celsius")) return (value - 32) / 1.8;
        if (from.equals("Celsius") && to.equals("Kelvin")) return value + 273.15;
        if (from.equals("Kelvin") && to.equals("Celsius")) return value - 273.15;

        return value;
    }
}
