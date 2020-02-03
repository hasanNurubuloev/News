package com.geektech.news;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.geektech.news.data.RetrofitBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Object[] keys;
    private ArrayList<String> values;
    private Spinner spinnerOne;
    private Spinner spinnerTwo;
    private TextView tvResult;
    private EditText etCurrent;
    private Button change;
    private double firstCurrency;
    private double secondCurrency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        fetchCurrencies();
        initListeners();
        setupListeners();
    }

    private void fetchCurrencies() {
        RetrofitBuilder.getServise()
                .getCurrencies("1fc2a9bd0a85d3520261791025761f74")
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            parseJson(response.body());
                            Log.d("TAG", "onResponse: ");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void parseJson(JsonObject object) {

        JsonObject rates = object.getAsJsonObject("rates");
        keys = rates.keySet().toArray();
        values = new ArrayList<>();
        for (Object item : keys) {
            values.add(rates.getAsJsonPrimitive(item.toString()).toString());
        }
        setupAdapter();
        Log.d("TAG", "parseJson: ");
    }
    private void initListeners() {
        spinnerOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                firstCurrency = Double.parseDouble(values.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerTwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               secondCurrency = Double.parseDouble(values.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initViews() {
        spinnerOne = findViewById(R.id.spinerOne);
        spinnerTwo = findViewById(R.id.spinerTwo);
        tvResult = findViewById(R.id.tvResult);
        etCurrent = findViewById(R.id.etCurrent);
        change = findViewById(R.id.change_btn);
    }

    private void setupAdapter() {
        ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_item, keys);
        spinnerOne.setAdapter(adapter);
        spinnerTwo.setAdapter(adapter);

    }

    private void setupListeners() {
        etCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvResult.setText( (String.valueOf((Double.parseDouble(s.toString())/firstCurrency)*secondCurrency)));
            }
        });
    }


}
