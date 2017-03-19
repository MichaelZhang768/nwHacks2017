package com.example.michael.nwhacksandroidgame;

import android.graphics.RectF;
import java.util.Random;

/**
 * Created by Michael on 3/18/2017.
 */

public class Insect {

    private float x;
    private float y;
    private float screenX;
    private float screenY;

    private RectF rect;

    public final int UP = 0;
    public final int DOWN = 1;
    public final int LEFT = 2;
    public final int RIGHT = 3;

    private Random random;

    private int headingY = 0;
    private int headingX = 2;
    private float speedY = random.nextInt(700);
    private float speedX = random.nextInt(700);

    private int width = 200;
    private int height = 200;

    private boolean isActive;

    public int whichAnimation;


    public Insect(int screenX, int screenY) {
        isActive = false;
        rect = new RectF();
        x = random.nextInt(screenX);
        y = screenY;
        whichAnimation = 1;
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
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

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setActive() {
        isActive = true;
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
        if(headingY == UP && headingX == LEFT) {
            y = y - speedY/fps;
            x = x - speedX/fps;
        } else if (headingY == UP && headingX == RIGHT){
            y = y - speedY/fps;
            x = x + speedX/fps;
        } else if (headingY == DOWN && headingX == LEFT) {
            y = y + speedY/fps;
            x = x - speedX/fps;
        } else {
            y = y + speedY/fps;
            x = x + speedX/fps;
        }

        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;
    }

}
