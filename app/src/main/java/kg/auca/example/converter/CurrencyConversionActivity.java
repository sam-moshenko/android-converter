package kg.auca.example.converter;

import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Dictionary;

public class CurrencyConversionActivity extends AppCompatActivity {
    EditText edtFirst, edtSecond, edtRatio;
    Spinner firstSpinner, secondSpinner;
    JSONObject currencyRatios;

    double ratio = 1;

    Boolean textWatcherDeactivate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_conversion);

        loadCurrencies();
        setupSpinners();
        setupEditTextFields();
    }

    private void loadCurrencies() {
        InputStream stream = getResources().openRawResource(R.raw.currencies);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            Log.e("loadCurrencies", e.getMessage());
            e.printStackTrace();
        }

        try {
            currencyRatios = new JSONObject(builder.toString());
        } catch (JSONException e) {
            Log.e("loadCurrencies", e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupSpinners() {
        firstSpinner = findViewById(R.id.spinnerFirst);
        secondSpinner = findViewById(R.id.spinnerSecond);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.currencyUnits, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        firstSpinner.setAdapter(adapter);
        secondSpinner.setAdapter(adapter);

        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateCurrencyRatio(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateCurrencyRatio(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void setupEditTextFields() {
        edtFirst = findViewById(R.id.edtFirstValue);
        edtSecond = findViewById(R.id.edtSecondValue);
        edtRatio = findViewById(R.id.edtFactor);

        edtFirst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculate(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        edtSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculate(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void updateCurrencyRatio(Boolean isFirstFrom) {
        String firstItem = (String)firstSpinner.getSelectedItem();
        String secondItem = (String)secondSpinner.getSelectedItem();
        String key = firstItem + " - " + secondItem;
        try {
            double ratio = currencyRatios.getDouble(key);
            edtRatio.setText(ratio + "");
            calculate(isFirstFrom);
        } catch (JSONException e) {

        }
    }

    private void calculate(Boolean isFirstFrom) {
        if (textWatcherDeactivate) {
            textWatcherDeactivate = false;
            return;
        }
        textWatcherDeactivate = true;

        double ratio = 0;
        try {
            ratio = Double.parseDouble(edtRatio.getText().toString());
        } catch (NumberFormatException e) {

        }

        EditText edtFrom, edtTo;
        if (isFirstFrom) {
            edtFrom = edtFirst;
            edtTo = edtSecond;
        } else {
            edtFrom = edtSecond;
            edtTo = edtFirst;
            ratio = 1/ratio;
        }

        double from = 0;

        try {
            from = Double.parseDouble(edtFrom.getText().toString());
        } catch (Exception e) {

        }

        Double result = from * ratio;

        edtTo.setText(result + "");
    }
}
