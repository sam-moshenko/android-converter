package kg.auca.example.converter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String CONVERSION_TYPE = "ConversionType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClick(View view) {
        Intent intent = new Intent();

        intent.putExtra(CONVERSION_TYPE, view.getId());

        switch (view.getId()) {
            case R.id.btnLength:
            case R.id.btnMass:
                intent.setClass(this, UnitConversionActivity.class);
                break;
            case R.id.btnCurrency:
                intent.setClass(this, CurrencyConversionActivity.class);
                break;
        }

        startActivity(intent);
    }
}
