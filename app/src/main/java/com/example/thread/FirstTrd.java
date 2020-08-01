package com.example.thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class FirstTrd extends View {

    private GameThread gameThread;
    private planetMake pM;
    boolean gameRunning;
    int Width;
    int Height;

    int button_width;
    Bitmap planetimg;
    Bitmap planetImg2;

    ArrayList<Planet> planets;
    ArrayList<Planet2> planet2s;
    int planetSpeed = 10;

    public FirstTrd(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.Width = w;
        this.Height = h;

        planets = new ArrayList<Planet>();
        planet2s = new ArrayList<Planet2>();

        int x = Width/8;
        int y = Height/11;
        button_width = Width/6;
        planetimg = BitmapFactory.decodeResource(getResources(),R.drawable.planet1);
        planetimg = Bitmap.createScaledBitmap(planetimg,button_width,button_width,true);
        planetImg2 = BitmapFactory.decodeResource(getResources(),R.drawable.planet2);
        planetImg2 = Bitmap.createScaledBitmap(planetImg2,button_width/2,button_width/2, true);

        if(gameThread == null){
            gameThread = new GameThread();
            gameThread.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        gameRunning = false;
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p1 = new Paint();
        p1.setColor(Color.WHITE);
        p1.setTextSize(50);

        Paint p2 = new Paint();
        p2.setColor(Color.RED);
        p2.setTextSize(200);

        for(Planet tmp : planets){
            canvas.drawBitmap(planetimg, tmp.x, tmp.y, p1);
        }
        for(Planet2 tmp : planet2s){
            canvas.drawBitmap(planetImg2, tmp.x,tmp.y,p1);
        }
    }

    public void movePlanet(){
        for(int i = planets.size()-1; i>= 0; i--){
            planets.get(i).move();
        }
        for(int i = planets.size()-1; i>= 0; i--){
            if(planets.get(i).y > Height) planets.remove(i);
        }
    }

    public void movePlanet2(){
        for(int i = planet2s.size()-1; i >= 0; i--){
            planet2s.get(i).move();
        }
        for(int i = planet2s.size()-1; i>= 0; i--){
            if(planet2s.get(i).y > Height) planet2s.remove(i);
        }
    }


    class GameThread extends Thread {
        @Override
        public void run() {
            pM = new planetMake();
            pM.start();
            gameRunning = true;
            while(gameRunning){
                try{
                    postInvalidate();
                    movePlanet();
                    movePlanet2();
                    sleep(30);

                }catch (Exception e){
                }
            }
        }
    }

    class planetMake extends  Thread {
        @Override
        public void run() {
            try {
                while(gameRunning) {
                    Random r1 = new Random();
                    int x = r1.nextInt(Width);
                    int y = r1.nextInt(Height/3);
                    int dir = r1.nextInt(1);
                    if(planets.size()<15) {
                        planets.add(new Planet(x, 0, dir,planetSpeed));
                    }
                    if (planet2s.size() < 4) {
                        planet2s.add(new Planet2(x, 0, dir));
                    }
                    sleep(700);
                }
            }catch (Exception e){
            }
        }
    }
}
