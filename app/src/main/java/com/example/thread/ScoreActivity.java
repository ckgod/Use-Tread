package com.example.thread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    ImageButton btn_back;
    TextView tv_rank_1;
    TextView tv_rank_2;
    TextView tv_rank_3;
    TextView tv_rank_4;
    TextView tv_rank_5;

    private static final String SCORES = "scores";
    ArrayList<Integer> scores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);


        init();
        btn_back = findViewById(R.id.btn_back);
        tv_rank_1 = findViewById(R.id.tv_rank_1);
        tv_rank_2 = findViewById(R.id.tv_rank_2);
        tv_rank_3 = findViewById(R.id.tv_rank_3);
        tv_rank_4 = findViewById(R.id.tv_rank_4);
        tv_rank_5 = findViewById(R.id.tv_rank_5);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        scores = getScoresArrayPref();
        tv_rank_1.setText(scores.get(scores.size()-6).toString());
        tv_rank_2.setText(scores.get(scores.size()-7).toString());
        tv_rank_3.setText(scores.get(scores.size()-8).toString());
        tv_rank_4.setText(scores.get(scores.size()-9).toString());
        tv_rank_5.setText(scores.get(scores.size()-10).toString());
    }

    public void init(){
        scores = getScoresArrayPref();
        for(int i = 0; i < 5; i++){
            scores.add(0);
        }
        setScoresArrayPref(SCORES, scores);
    }

    private ArrayList<Integer> getScoresArrayPref() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        SharedPreferences Prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = Prefs.getString(SCORES, "");
        if (!json.equals("")) {
            Type type = new TypeToken<ArrayList<Integer>>() {
            }.getType();
            arrayList = gson.fromJson(json, type);
        }
        return arrayList;
    }

    private void setScoresArrayPref(String key, ArrayList<Integer> values) {
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        String json = gson.toJson(values, listType);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, json);
        editor.apply();
    }
}
