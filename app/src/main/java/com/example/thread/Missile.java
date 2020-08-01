package com.example.thread;

public class Missile extends Thread{
    int x,y;
    int missileSpeed = 35;
    boolean missileMove;

    Missile(int x, int y){
        this.x = x;
        this.y = y;
        this.missileMove = true;
    }
    public void move(){
        y-=missileSpeed;
    }

    @Override
    public void run() {
        try {
            y -= missileSpeed;
        }catch (Exception e){

        }
    }
}
