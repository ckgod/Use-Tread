package com.example.thread;

public class PlanetBoss {
    int x,y;
    int planetSpeed = 5;
    int dir;

    PlanetBoss(int x, int y, int dir){
        this.x = x;
        this.y = y;
        this.dir = dir;
    }
    public void move(){
        y+=planetSpeed;
    }
}
