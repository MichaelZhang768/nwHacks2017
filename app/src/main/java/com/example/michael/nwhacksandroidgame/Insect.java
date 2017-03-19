package com.example.michael.nwhacksandroidgame;

import android.graphics.RectF;

import java.util.Random;

/**
 * Created by Michael on 3/18/2017.
 */

public class Insect {

    private float x;
    private float y;

    private RectF rect;

    public final int UP = 0;
    public final int DOWN = 1;

    private int heading = -1;
    private float speed = 350;

    private int width = 200;
    private int height = 200;

    private boolean isActive;

    public int whichAnimation;

    private Random random;

    public Insect(int screenX, int screenY) {
        isActive = false;
        rect = new RectF();
        x = random.nextInt(screenX);
        y = screenY;
        whichAnimation = 1;
    }

    public RectF getRect() {
        return rect;
    }

    public boolean getStatus() {
        return isActive;
    }

    public void setInactive() {
        isActive = false;
    }

//    public void move(float startX, float startY, int direction) {
//        if(!isActive) {
//            x = startX;
//            y = startY;
//            heading = direction;
//            isActive = true;
//        }
//    }

    public void update(long fps) {
        if(heading == UP) {
            y = y - speed/fps;
        } else {
            y = y + speed/fps;
        }

        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;
    }
}
