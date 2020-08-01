package com.example.thread;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Game extends View {


    private static final String SCORES = "scores";
    ArrayList<Integer> scores = new ArrayList<>();

    boolean screenChange = true;
    private GameThread gameThread;
    private planetMake pM;
    private bossMake bM;
    boolean gameRunning;
    boolean gameDie;
    int Width;
    int Height;
    Bitmap spaceship, leftKey, rightKey, screen, screen2;

    int spaceshipWidth;
    int spaceship_middle;
    int spaceship_x,spaceship_y;
    int leftKey_x, leftKey_y;
    int rightKey_x, rightKey_y;
    int button_width;
    int score;

    Bitmap missileButton;
    int missileButton_x,missileButton_y;
    int missileWidth;
    int missile_middle; // 미사일 크기 반
    Bitmap missile;
    Bitmap planetimg;
    Bitmap planetBossImg;
    Bitmap planetImg2;

    int count;
    ArrayList<Missile> Msi;
    ArrayList<Planet> planets;
    ArrayList<Planet2> planet2s;
    ArrayList<PlanetBoss> planetBosses;
    int planetSpeed = 10;
    Comparator<Integer> compare = new Comparator<Integer>() {
        @Override public int compare(Integer lhs, Integer rhs) {
            return lhs.compareTo(rhs);
        }
    };

    public Game(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.Width = w;
        this.Height = h;


        Msi = new ArrayList<Missile>();
        planets = new ArrayList<Planet>();
        planet2s = new ArrayList<Planet2>();
        planetBosses = new ArrayList<PlanetBoss>();

        spaceship = BitmapFactory.decodeResource(getResources(),R.drawable.spaceship);
        int x = Width/8;
        int y = Height/11;
        spaceship = Bitmap.createScaledBitmap(spaceship,x,y,true);
        spaceshipWidth = spaceship.getWidth();
        spaceship_middle = spaceshipWidth/2;
        spaceship_x = Width*1/9;
        spaceship_y = Height*6/9;


        leftKey = BitmapFactory.decodeResource(getResources(),R.drawable.leftkey);
        leftKey_x = Width*5/9;
        leftKey_y = Height*7/9;
        button_width = Width/6;
        leftKey = Bitmap.createScaledBitmap(leftKey, button_width, button_width ,true);

        rightKey = BitmapFactory.decodeResource(getResources(),R.drawable.rightkey);
        rightKey_x = Width*7/9;
        rightKey_y = Height*7/9;
        rightKey = Bitmap.createScaledBitmap(rightKey,button_width,button_width,true);

        missileButton = BitmapFactory.decodeResource(getResources(),R.drawable.missilebutton);
        missileButton = Bitmap.createScaledBitmap(missileButton, button_width,button_width, true);
        missileButton_x = Width*1/11;
        missileButton_y = Height*7/9;

        missile = BitmapFactory.decodeResource(getResources(),R.drawable.tmpmissile);
        missile = Bitmap.createScaledBitmap(missile, button_width/4,button_width/4,true);
        missileWidth = missile.getWidth();
        missile_middle = missileWidth/2;

        planetimg = BitmapFactory.decodeResource(getResources(),R.drawable.planet1);
        planetimg = Bitmap.createScaledBitmap(planetimg,button_width,button_width,true);
        planetImg2 = BitmapFactory.decodeResource(getResources(),R.drawable.planet2);
        planetImg2 = Bitmap.createScaledBitmap(planetImg2,button_width/2,button_width/2, true);
        planetBossImg = BitmapFactory.decodeResource(getResources(),R.drawable.planetboss22);
        planetBossImg = Bitmap.createScaledBitmap(planetBossImg,button_width*3,button_width*3, true);
        screen = BitmapFactory.decodeResource(getResources(), R.drawable.screen);
        screen = Bitmap.createScaledBitmap(screen,Width,Height,true);
        screen2 = BitmapFactory.decodeResource(getResources(),R.drawable.screen2);
        screen2 = Bitmap.createScaledBitmap(screen2,Width,Height,true);

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


        if(screenChange){
            canvas.drawBitmap(screen,0,0,p1);
        }
        else{
            canvas.drawBitmap(screen2,0,0,p1);
            screenChange = true;
        }

        canvas.drawBitmap(spaceship,spaceship_x,spaceship_y,p1);
        canvas.drawText(Integer.toString(count),0,300,p1);
        canvas.drawText("점수 : "+Integer.toString(score),0,200,p1);
        canvas.drawBitmap(leftKey, leftKey_x, leftKey_y,p1);
        canvas.drawBitmap(rightKey, rightKey_x, rightKey_y,p1);
        canvas.drawBitmap(missileButton,missileButton_x,missileButton_y,p1);

        for(Missile tmp : Msi){
            canvas.drawBitmap(missile,tmp.x,tmp.y,p1);
        }
        for(Planet tmp : planets){
            canvas.drawBitmap(planetimg, tmp.x, tmp.y, p1);
        }
        for(Planet2 tmp : planet2s){
            canvas.drawBitmap(planetImg2, tmp.x,tmp.y,p1);
        }
        for(PlanetBoss tmp : planetBosses){
            canvas.drawBitmap(planetBossImg,tmp.x,tmp.y,p1);
        }
        planetSpeed += 0.001;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = 0, y = 0;
        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            x = (int)event.getX();
            y = (int)event.getY();
        }
        if((x> leftKey_x) && (x< leftKey_x +button_width) && (y> leftKey_y) && (y< leftKey_y + button_width)){
            spaceship_x -= 20;
        }
        if((x> rightKey_x) && (x< rightKey_x +button_width) && (y> rightKey_y) && (y< leftKey_y +button_width)){
            spaceship_x += 20;
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if((x>missileButton_x) && (x<missileButton_x + button_width) && (y>missileButton_y) && (y<missileButton_y+button_width)) {
                if(Msi.size() < 1) {
                    Msi.add(new Missile(spaceship_x + spaceshipWidth / 2 - missileWidth / 2, spaceship_y));
                }
            }
        }
        return true;
    }

    public void moveMissile(){
        for(int i = Msi.size()-1; i >= 0; i--){
            Msi.get(i).move();
        }
        for(int i = Msi.size()-1; i >=0; i--){
            if(Msi.get(i).y < 0) Msi.remove(i);
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

    public void movePlanetBoss(){
        for(int i = planetBosses.size()-1; i >= 0; i--){
            planetBosses.get(i).move();
        }
        for(int i = planetBosses.size()-1; i>= 0; i--){
            if(planetBosses.get(i).y > Height) planetBosses.remove(i);
        }
    }

    public void checkCollision(){
        for(int i = planets.size()-1; i >= 0; i--){
            for(int j = Msi.size()-1; j >= 0; j--){
                if(
                        Msi.get(j).x + missile_middle > planets.get(i).x
                                &&
                                Msi.get(j).x + missile_middle < planets.get(i).x + button_width
                                &&
                                Msi.get(j).y > planets.get(i).y
                                &&
                                Msi.get(j).y < planets.get(i).y+button_width)
                {
                    planets.remove(i);
                    Msi.get(j).y = -40;
                    score+= 10;
                }
            }
        }
        for(int i = planetBosses.size()-1; i >= 0; i--){
            for(int j = Msi.size()-1; j >= 0; j--){
                if(
                        Msi.get(j).x + missile_middle > planetBosses.get(i).x
                                &&
                                Msi.get(j).x + missile_middle < planetBosses.get(i).x + button_width*3
                                &&
                                Msi.get(j).y > planetBosses.get(i).y
                                &&
                                Msi.get(j).y < planetBosses.get(i).y+button_width*3)
                {
                    planetBosses.remove(i);
                    Msi.get(j).y = -40;
                    score+= 500;
                }
            }
        }
        for(int i = planet2s.size()-1; i >= 0; i--){
            for(int j = Msi.size()-1; j >= 0; j--){
                if(
                        Msi.get(j).x + missile_middle > planet2s.get(i).x
                                &&
                                Msi.get(j).x + missile_middle < planet2s.get(i).x + button_width/2
                                &&
                                Msi.get(j).y > planet2s.get(i).y
                                &&
                                Msi.get(j).y < planet2s.get(i).y+button_width/2)
                {
                    planet2s.remove(i);
                    Msi.get(j).y = -40;
                    score+= 100;
                }
            }
        }
    }

    public void checkDie(){
        for(int i = planets.size()-1; i >= 0; i--){
            if(
                    spaceship_x + spaceship_middle > planets.get(i).x
                            &&
                            spaceship_x + spaceship_middle < planets.get(i).x + button_width*0.7
                            &&
                            spaceship_y > planets.get(i).y
                            &&
                            spaceship_y < planets.get(i).y+button_width*0.7)
            {
                gameRunning = false;
            }
        }
        for(int i = planet2s.size()-1; i >= 0; i--){
            if(
                    spaceship_x + spaceship_middle > planet2s.get(i).x
                            &&
                            spaceship_x + spaceship_middle < planet2s.get(i).x + button_width/2*0.7
                            &&
                            spaceship_y > planet2s.get(i).y
                            &&
                            spaceship_y < planet2s.get(i).y+button_width/2*0.7)
            {
                gameRunning = false;
            }
        }
        for(int i = planetBosses.size()-1; i >= 0; i--){
            if(
                    spaceship_x + spaceship_middle > planetBosses.get(i).x
                            &&
                            spaceship_x + spaceship_middle < planetBosses.get(i).x + button_width*3*0.7
                            &&
                            spaceship_y > planetBosses.get(i).y
                            &&
                            spaceship_y < planetBosses.get(i).y+button_width*3*0.7)
            {
                gameRunning = false;
            }
        }
    }


    class GameThread extends Thread {
        @Override
        public void run() {
            pM = new planetMake();
            bM = new bossMake();
            bM.start();
            pM.start();
            gameRunning = true;
            while(gameRunning){
                try{
                    postInvalidate();
                    moveMissile();
                    movePlanet();
                    movePlanet2();
                    movePlanetBoss();
                    checkCollision();
                    checkDie();
                    count++;
                    sleep(30);
                }catch (Exception e){                }
            }
            addScore(score);
            Activity MA = (Activity)MainActivity.Main_Activity;
            MA.finish();
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
                    if(count > 600) {
                        if (planet2s.size() < 2) {
                            planet2s.add(new Planet2(x, 0, dir));
                        }
                    }
                    sleep(700);
                }
            }catch (Exception e){
            }
        }
    }

    class bossMake extends Thread{
        @Override
        public void run() {try {
            while(gameRunning) {
                Random r1 = new Random();
                int x = r1.nextInt(Width);
                int y = r1.nextInt(Height/3);
                int dir = r1.nextInt(1);
                if(count >= 1000) {
                    if (planetBosses.size() < 1) {
                        planetBosses.add(new PlanetBoss(x, 0, dir));
                    }
                }
                sleep(5000);
            }
        }catch (Exception e){
        }
        }
    }

    public void addScore(int sc){
        scores = getScoresArrayPref();
        scores.add(score);
        Collections.sort(scores,compare);
        setScoresArrayPref(SCORES, scores);
    }

    public void init(){
        for(int i = 0; i < 5; i++){
            scores.add(0);
        }
        setScoresArrayPref(SCORES, scores);
    }

    private void setScoresArrayPref(String key, ArrayList<Integer> values) {
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        String json = gson.toJson(values, listType);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, json);
        editor.apply();
    }

    private ArrayList<Integer> getScoresArrayPref() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        SharedPreferences Prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = Prefs.getString(SCORES, "");
        if (!json.equals("")) {
            Type type = new TypeToken<ArrayList<Integer>>() {
            }.getType();
            arrayList = gson.fromJson(json, type);
        }
        return arrayList;
    }

}
