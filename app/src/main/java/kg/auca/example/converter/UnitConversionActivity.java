package kg.auca.example.converter;

import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class UnitConversionActivity extends AppCompatActivity {
    TypedArray factors;
    TypedArray names;
    int factorId, namesId;

    EditText edtFirst, edtSecond;
    Spinner firstSpinner, secondSpinner;

    Boolean textWatcherDeactivate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_conversion);

        setupArrays();
        setupSpinners();
        setupEditTextFields();
    }

    private void setupSpinners() {
        firstSpinner = findViewById(R.id.spinnerFirst);
        secondSpinner = findViewById(R.id.spinnerSecond);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, namesId, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        firstSpinner.setAdapter(adapter);
        secondSpinner.setAdapter(adapter);

        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculate(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calculate(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void setupArrays() {
        int type = getIntent().getIntExtra(MainActivity.CONVERSION_TYPE, 0);
        switch (type) {
            case R.id.btnLength:
                factorId = R.array.lengthUnitsFactor;
                namesId = R.array.lengthUnits;
                break;
            case R.id.btnMass:
                factorId = R.array.massUnitsFactor;
                namesId = R.array.massUnits;
                break;
        }
        factors = getResources().obtainTypedArray(factorId);
        names = getResources().obtainTypedArray(namesId);
    }

    private void setupEditTextFields() {
        edtFirst = findViewById(R.id.edtFirstValue);
        edtSecond = findViewById(R.id.edtSecondValue);

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

    private void calculate(Boolean isFirstFrom) {
        if (textWatcherDeactivate) {
            textWatcherDeactivate = false;
            return;
        }
        textWatcherDeactivate = true;

        Spinner fromSpinner = isFirstFrom ? firstSpinner : secondSpinner;
        Spinner toSpinner = !isFirstFrom ? firstSpinner : secondSpinner;

        EditText edtFrom = isFirstFrom ? edtFirst : edtSecond;
        EditText edtTo = !isFirstFrom ? edtFirst : edtSecond;

        double fromValue;
        try {
            fromValue = Double.parseDouble(edtFrom.getText().toString());
        } catch (NumberFormatException e) {
            return;
        }

        int fromSelectedIndex = fromSpinner.getSelectedItemPosition();
        int toSelectedIndex = toSpinner.getSelectedItemPosition();
        double factorFrom = factors.getFloat(fromSelectedIndex, 1.0f);
        double factorTo = factors.getFloat(toSelectedIndex, 1.0f);

        double result = factorFrom * fromValue / factorTo;

        edtTo.setText(result + "");
    }
}
