package com.example.michael.nwhacksandroidgame;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by Michael on 3/18/2017.
 */

public class GameView extends SurfaceView implements Runnable {

    private Context context;
    private Thread thread = null;
    private SurfaceHolder surfaceHolder;
    private volatile boolean playing;
    private boolean paused = true;
    private Canvas canvas;
    private Paint paint;
    private long fps;
    private long timeFrame;
    private int screenXSize;
    private int screenYSize;

    private Insect[] insects = new Insect[20];
    int numInsects = 0;

    private SoundPool soundPool;
    private int squishBugID = -1;

    Bitmap bitmapInsect;

    int score = 0;
    int lives = 9;

    public GameView(Context context, int x, int y) {
        super(context);
        surfaceHolder = getHolder();
        paint = new Paint();

        screenXSize = x;
        screenYSize = y;

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor assetFileDescriptor;

            assetFileDescriptor = assetManager.openFd("squished.ogg");
            squishBugID = soundPool.load(assetFileDescriptor, 0);
        } catch(IOException e) {
            Log.e("Error", "failed to load sound files");
        }

        bitmapInsect = BitmapFactory.decodeResource(this.getResources(), R.drawable.insect);

        prepareLevel();
    }

    private void prepareLevel() {
        for(int i = 0; i < insects.length; i++) {
            insects[i] = new Insect();
        }
    }

    @Override
    public void run() {
        long startTimeFrame = System.currentTimeMillis();

        if(!paused) {
            update();
        }

        draw();

        timeFrame = System.currentTimeMillis() - startTimeFrame;
        if(timeFrame >= 1) {
            fps = 1000/timeFrame;
        }
    }

    private void update() {
        boolean lost = false;

        // Move the player's ship

        // Update the invaders if visible

        // Update all the invaders bullets if active
        for(int i = 0; i < insects.length; i++) {
            if(insects[i].getStatus()) {
                insects[i].update(fps);
            }
        }

        // Did an invader bump into the edge of the screen
        if(lost) {
            prepareLevel();
        }

        // Update the players bullet

        // Has the player's bullet hit the top of the screen

        // Has an invaders bullet hit the bottom of the screen

        // Has the player's bullet hit an invader

        // Has an alien bullet hit a shelter brick

        // Has a player bullet hit a shelter brick

        // Has an invader bullet hit the player ship
    }

    private void draw() {
        if(surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 26, 128, 182));
            paint.setColor(Color.argb(255, 255, 255, 255));

            for(int i = 0; i < insects.length; i++) {
                if(insects[i].getStatus()) {

                }
            }

            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + " Lives: " + lives, 10, 50, paint);
            canvas.drawText("" + fps , 10, 100, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        playing = false;
        try {
            thread.join();
        } catch(InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    public void resume() {
        playing = true;
        thread = new Thread(this);
        thread.start();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                float motionEventX = motionEvent.getX();
                float motionEventY = motionEvent.getY();
                RectF insectRect;
                for(int i = 0; i < insects.length; i++) {
                    insectRect = insects[i].getRect();
                    if(insectRect.right > motionEventX && insectRect.left < motionEventX) {
                        if(insectRect.bottom > motionEventY && insectRect.top < motionEventY) {
                            insects[i].setInactive();
                            soundPool.play(squishBugID, 1, 1, 0, 0, 1);
                        }
                    }
                }
                break;
        }
        return true;
    }
}
