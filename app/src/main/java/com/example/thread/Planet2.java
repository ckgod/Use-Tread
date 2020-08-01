package com.example.thread;

public class Planet2 extends Thread {
    int x,y;
    int planetSpeed = 25;
    int dir;

    Planet2(int x, int y, int dir){
        this.x = x;
        this.y = y;
        this.dir = dir;
    }
    public void move(){
        y+=planetSpeed;
    }
}
