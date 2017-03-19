package com.example.michael.nwhacksandroidgame;

import android.graphics.RectF;
import java.util.Random;

/**
 * Created by Michael on 3/18/2017.
 */

public class Insect {

    private float x;
    private float y;
    private int screenX;
    private int screenY;

    private RectF rect;

    public final int UP = 0;
    public final int DOWN = 1;
    public final int LEFT = 2;
    public final int RIGHT = 3;

    private Random random = new Random();

    private int headingY = 0;
    private int headingX = 2;
    private float speedY = random.nextInt(500);
    private float speedX = random.nextInt(500);

    public int width = 200;
    public int height = 200;

    private boolean isActive;

    public int whichAnimation;


    public Insect(int screenX, int screenY) {
        isActive = false;
        rect = new RectF();
        x = random.nextInt(screenX) + width;
        y = screenY + height;
        whichAnimation = 1;
        this.screenX = screenX;
        this.screenY = screenY;
        Random random = new Random();
        int upOrDown = random.nextInt(2);
        int leftOrRight = random.nextInt(2);
        if(leftOrRight == 0) {
            headingX = LEFT;
        } else {
            headingX = RIGHT;
        }
//        if(upOrDown == 0) {
//            headingY = UP;
//        } else {
//            headingY = DOWN;
//            y = 0;
//        }
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

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setActive() {
        isActive = true;
    }

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

        rect.left = x- width/2;
        rect.right = x + width/2;
        rect.top = y - height/2;
        rect.bottom = y+height/2 ;
    }

}
