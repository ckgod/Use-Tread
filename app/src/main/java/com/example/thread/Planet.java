package com.example.thread;

public class Planet extends Thread{
    int x,y;
    int planetSpeed = 10;
    int dir;

    Planet(int x, int y, int dir,int speed){
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.planetSpeed = speed;
    }
    public void move(){
        y+=planetSpeed;
    }
}
