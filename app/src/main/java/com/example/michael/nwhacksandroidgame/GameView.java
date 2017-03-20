package com.example.michael.nwhacksandroidgame;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.Random;

/**
 * Created by Michael on 3/18/2017.
 */

public class GameView extends SurfaceView implements Runnable {

    private Thread thread = null;
    private SurfaceHolder surfaceHolder;
    private volatile boolean playing;
    private boolean paused = true;
    private Canvas canvas;
    private Paint paint;
    private long fps;
    private long startTimeFrame;
    private long timeFrame;
    private int screenXSize;
    private int screenYSize;

    private Insect[] insects = new Insect[10];
    int numInsects = 0;

    private SoundPool soundPool;
    private int squishBugID = -1;

    Bitmap bitmapInsect1;
    Bitmap bitmapInsect2;
    Bitmap bitmapInsect3;
    Bitmap bitmapInsect4;
    Bitmap bitmapSplat;
    Bitmap bitmapBackground = BitmapFactory.decodeResource(this.getResources(), R.drawable.background);


    int score = 0;
    int lives = 9;

    private long animationInterval = 100;

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

            assetFileDescriptor = assetManager.openFd("splat.mp3");
            squishBugID = soundPool.load(assetFileDescriptor, 0);
        } catch(IOException e) {
            Log.e("Error", "failed to load sound files");
        }

        bitmapInsect1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.insect1);
        bitmapInsect2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.insect2);
        bitmapInsect3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.insect3);
        bitmapInsect4 = BitmapFactory.decodeResource(this.getResources(), R.drawable.insect4);
        bitmapSplat = BitmapFactory.decodeResource(this.getResources(), R.drawable.splat);

        prepareLevel();
    }

    private void prepareLevel() {
        for (int i = 0; i < insects.length; i++) {
            insects[i] = new Insect(screenXSize, screenYSize);
            numInsects++;
        }
        lives = 9;
        score = 0;
    }

    @Override
    public void run() {
        while (playing) {
            startTimeFrame = System.currentTimeMillis();
            if (!paused) {
                update();
            }

            draw();

            timeFrame = System.currentTimeMillis() - startTimeFrame;
            if (timeFrame >= 1) {
                fps = 1000 / timeFrame;
            }
        }
    }

    private void update() {
        Random random = new Random();
        for (int i = 0; i < insects.length; i++) {
            if (!insects[i].getStatus()) {
                if (random.nextInt(250) == 0) {
                    insects[i].setX(random.nextInt(screenXSize) - insects[i].width);
                    insects[i].setY(screenYSize - insects[i].height);
                    insects[i].setActive();
                }
            }
        }

        // Update all the invaders bullets if active
        for (int i = 0; i < insects.length; i++) {
            if (insects[i].getStatus()) {
                insects[i].update(fps);
                if (insects[i].getRect().right + 200 < 0 || insects[i].getRect().bottom + 200 < 0 ||
                        insects[i].getRect().left + 200 > screenXSize || insects[i].getRect().top + 200 > screenYSize) {
                    insects[i].setInactive();
                    lives--;
                    if (lives == 0) {
                        paused = true;
                    }
                }
            }
        }

        if (paused) {
            prepareLevel();
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            Rect rect = new Rect(0, 0, screenXSize, screenYSize);
            canvas.drawBitmap(bitmapBackground, null, rect, paint);

            for (int i = 0; i < insects.length; i++) {
                if (insects[i].getStatus()) {
                    if (startTimeFrame - insects[i].lastAnimationTime > animationInterval) {
                        if (insects[i].whichAnimation != 4) {
                            insects[i].whichAnimation++;
                        } else {
                            insects[i].whichAnimation = 1;
                        }
                        drawInsect(insects[i]);
                        insects[i].lastAnimationTime = System.currentTimeMillis();
                    } else {
                        drawInsect(insects[i]);
                    }
                } else {
                    if(System.currentTimeMillis() - insects[i].splatStartTime < 1000) {
                        canvas.drawBitmap(bitmapSplat, insects[i].getRect().left, insects[i].getRect().top, paint);
                    }
                }
            }

            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(60);
            canvas.drawText("Score: " + score, 10, 50, paint);
            canvas.drawText("Lives: " + lives, 10, 100, paint);
            canvas.drawText("FPS:" + fps, 10, 150, paint);

            if(paused) {
                paint.setTextSize(100);
                canvas.drawText("Tap the screen to play!", screenXSize/3, screenYSize/2, paint);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawInsect(Insect insect) {
        switch (insect.whichAnimation) {
            case 1:
                canvas.drawBitmap(bitmapInsect1, insect.getRect().left, insect.getRect().top, paint);
                break;
            case 2:
                canvas.drawBitmap(bitmapInsect2, insect.getRect().left, insect.getRect().top, paint);
                break;
            case 3:
                canvas.drawBitmap(bitmapInsect3, insect.getRect().left, insect.getRect().top, paint);
                break;
            case 4:
                canvas.drawBitmap(bitmapInsect4, insect.getRect().left, insect.getRect().top, paint);
                break;
        }
    }

    public void pause() {
        playing = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    public void resume() {
        playing = true;
        thread = new Thread(this);
        thread.start();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                paused = false;
                float motionEventX = motionEvent.getX();
                float motionEventY = motionEvent.getY();
                for (int i = 0; i < insects.length; i++) {
                    if (insects[i].getX()+3*insects[i].width > motionEventX && insects[i].getX() < motionEventX) {
                        if (insects[i].getY()+2*insects[i].height > motionEventY && insects[i].getY() < motionEventY) {
                            if (insects[i].getStatus()) {
                                score++;
                                insects[i].setInactive();
                                soundPool.play(squishBugID, 1, 1, 0, 0, 1);
                            }
                        }
                    }
                }
                break;
        }
        return true;
    }
}