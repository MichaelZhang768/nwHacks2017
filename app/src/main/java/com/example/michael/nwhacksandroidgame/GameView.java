package com.example.michael.nwhacksandroidgame;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
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
        prepareLevel();
    }

    private void prepareLevel() {

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

        if(lost) {
            prepareLevel();
        }
    }

    private void draw() {
        if(surfaceHolder.getSurface().isValid()) {
            
        }
    }
}
