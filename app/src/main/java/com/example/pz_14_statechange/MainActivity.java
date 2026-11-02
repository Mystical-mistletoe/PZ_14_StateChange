package com.example.pz_14_statechange;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModelProvider;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_USER = "user_json";
    private SharedPreferences prefs;
    private final Gson gson = new Gson();
    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Привязка ViewModel к Activity
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        // считать данные (выше) в поля ввода
        String json = prefs.getString(KEY_USER, null);
        if (json != null) {
            User saved = gson.fromJson(json, User.class);
            viewModel.setUser(saved);
            EditText nameEx = findViewById(R.id.nameBox);
            EditText yearEx = findViewById(R.id.yearBox);
            nameEx.setText(saved.getName());
            yearEx.setText(String.valueOf(saved.getAge()));
            TextView dataView = findViewById(R.id.dataView);
            dataView.setText("Name: " + saved.getName() + " Age: " + saved.getAge());
        }
    }

    public void saveData(View view) {
        //получение введенных данных
        EditText nameBox = findViewById(R.id.nameBox);
        EditText yearBox = findViewById(R.id.yearBox);
        String name = nameBox.getText().toString();
        int age = 0;
        try {
            age = Integer.parseInt(yearBox.getText().toString());
        }
        catch (NumberFormatException ex) {}
        //для поворота
        viewModel.saveUser(name, age);
        //сериализ в строку и сохр в SharedPreferences
        String json = gson.toJson(viewModel.getUser());
        prefs.edit().putString(KEY_USER, json).apply();

        TextView dataView = findViewById(R.id.dataView);
        dataView.setText("Name: " + name + " Age: " + age);
    }
    public void getData(View view) {
        User user = viewModel.getUser();
        TextView dataView = findViewById(R.id.dataView);
        dataView.setText("Name: " + user.getName() + " Age: " + user.getAge());
    }
}