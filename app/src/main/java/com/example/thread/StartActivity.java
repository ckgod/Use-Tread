package com.example.thread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class StartActivity extends AppCompatActivity {

    Button btn_start;
    ImageView iv_logo;
    Button btn_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btn_score = findViewById(R.id.btn_score);
        btn_start = findViewById(R.id.btn_start);
        iv_logo = findViewById(R.id.iv_logo);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(iv_logo);
        Glide.with(this).load(R.drawable.giphy).into(gifImage);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectGameActivity();
            }
        });

        btn_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectScoreActivity();
            }
        });

    }

    public void redirectGameActivity(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void redirectScoreActivity(){
        Intent intent = new Intent(getApplicationContext(),ScoreActivity.class);
        startActivity(intent);
    }

}
