package source55.challengeSpeed;

import android.content.Context;
import android.content.res.Resources;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class ChallengeSpeed extends View {
    Paint paint = new Paint();
    SharedPreferences sharedPreferences;
    Bitmap btnPrevious, btnNext, logo, btncw, btnccw, star, opaqueStar;
    Bitmap[] car = new Bitmap[36], tile = new Bitmap[21], numbers = new Bitmap[6], miniTiles = new Bitmap[21];
    float screenWidth, screenHeight, carPositionX, carPositionY, fractionScreenSize, miniTilesSize;
    boolean cwPressed, ccwPressed, walkOut;
    int angle3, time = -5, stage = 0, maxStageUnlocked = 0, qttStages = 24, alreadyWalkLenght;
    int[] minimumStars = {1,1,2,1,1,6,3,3,2,3,2,3,2,3,3,4,6,3,6,3,3,4,3,4};
    int[] starsEarned = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    int[] maxSpeed = {8,9,10,10,11,11,11,12,12,12,13,13,13,14,14,14,14,15,15,15,15,16,16,16};
    int[] startAngle = {0,56,28,82,0,56,56,28,56,56,28,28,28,28,0,56,28,56,82,56,82,56,0,0};
    int[] stageTimeLimit = {740,540,800,710,980,980,760,1030,1020,1300,1080,1700,1080,620,1570,1180,1410,1900,1460,840,2000,1580,2160,1520};
    int[] tilesRequired = {15,12,21,18,28,28,21,32,32,45,37,61,37,21,60,44,52,75,55,34,79,66,92,67};
    int[][] alreadyWalk = new int[100][2];
    int[][] stageStarts = {{6,2},{0,2},{4,2},{0,4},{2,0},{4,0},{0,4},{2,0},{0,4},{0,6},{0,0},{6,8},{0,0},{4,0},{4,10},{2,0},{12,0},{0,8},{4,4},{4,4},{0,10},{0,0},{4,14},{12,0}};
    int[][] stageEnds = {{6,0},{0,0},{2,2},{3,2},{4,0},{2,0},{0,6},{0,0},{0,6},{0,4},{2,0},{6,6},{2,0},{2,0},{6,10},{0,0},{10,0},{0,10},{6,4},{4,6},{2,10},{0,2},{6,14},{4,4}};
    int[][][] course = {
            {{12,4,6},{2,0,1},{2,0,1},{2,0,1},{2,0,1},{2,0,1},{19,0,16}},
            {{14,0,13},{1,0,2},{1,0,2},{1,0,2},{1,0,2},{9,4,7}},
            {{11,3,3,3,5},{1,0,0,0,2},{1,0,20,4,7},{1,0,0,0,0},{1,0,15,3,5},{1,0,0,0,2},{9,4,4,4,7}},
            {{12,4,4,4,17},{2,0,0,0,0},{2,0,0,0,0},{2,0,20,4,6},{2,0,0,0,1},{2,0,0,0,1},{10,3,3,3,8}},
            {{11,3,5,0,11,3,5},{1,0,2,0,1,0,2},{16,0,10,3,8,0,2},{0,0,0,0,0,0,2},{14,0,12,4,6,0,2},{1,0,2,0,1,0,2},{9,4,7,0,9,4,7}},
            {{12,4,6,0,12,4,6},{2,0,1,0,2,0,1},{19,0,9,4,7,0,1},{0,0,0,0,0,0,1},{13,0,11,3,5,0,1},{2,0,1,0,2,0,1},{10,3,8,0,10,3,8}},
            {{0,0,0,0,13,0,14},{0,0,0,0,2,0,1},{0,0,12,4,7,0,1},{0,0,2,0,0,0,1},{12,4,7,0,11,3,8},{2,0,0,0,1,0,0},{10,3,3,3,8,0,0}},
            {{20,4,4,4,4,4,4,6},{0,0,0,0,0,0,0,1},{15,3,3,3,3,5,0,1},{0,0,0,0,0,2,0,1},{12,4,4,4,4,7,0,1},{2,0,0,0,0,0,0,1},{10,3,3,3,3,3,3,8}},
            {{12,4,6,0,13,0,14,0,12,4,6},{2,0,1,0,2,0,1,0,2,0,1},{2,0,9,4,7,0,9,4,7,0,1},{2,0,0,0,0,0,0,0,0,0,1},{10,3,3,3,3,3,3,3,3,3,8}},
            {{11,3,5,0,14,0,13,0,11,3,5},{1,0,2,0,1,0,2,0,1,0,2},{1,0,10,3,8,0,10,3,8,0,2},{1,0,0,0,0,0,0,0,0,0,2},{1,0,12,4,6,0,12,4,6,0,2},{1,0,2,0,1,0,2,0,1,0,2},{9,4,7,0,9,4,7,0,9,4,7}},
            {{15,3,5,0,11,3,3,3,5},{0,0,2,0,1,0,0,0,2},{14,0,2,0,9,4,6,0,2},{1,0,2,0,0,0,1,0,2},{1,0,10,3,3,3,8,0,2},{1,0,0,0,0,0,0,0,2},{9,4,4,4,4,4,4,4,7}},
            {{12,4,4,4,6,0,12,4,6,0,12,4,4,4,6},{2,0,0,0,1,0,2,0,1,0,2,0,0,0,1},{2,0,11,3,8,0,2,0,1,0,10,3,5,0,1},{2,0,1,0,0,0,2,0,1,0,0,0,2,0,1},{2,0,9,4,4,4,7,0,9,4,4,4,7,0,1},{2,0,0,0,0,0,0,0,0,0,0,0,0,0,1},{10,3,3,3,3,3,18,0,15,3,3,3,3,3,8}},
            {{15,3,5,0,11,3,5},{0,0,2,0,1,0,2},{14,0,2,0,1,0,2},{1,0,2,0,1,0,2},{1,0,2,0,1,0,2},{1,0,2,0,1,0,2},{1,0,10,3,8,0,2},{1,0,0,0,0,0,2},{9,4,4,4,4,4,7}},
            {{0,0,12,4,6,0,0},{0,0,2,0,1,0,0},{20,4,7,0,9,4,6},{0,0,0,0,0,0,1},{15,3,5,0,11,3,8},{0,0,2,0,1,0,0},{0,0,10,3,8,0,0}},
            {{0,0,12,4,4,4,4,4,6,0,0},{0,0,2,0,0,0,0,0,1,0,0},{12,4,7,0,11,3,5,0,9,4,6},{2,0,0,0,1,0,2,0,0,0,1},{10,3,3,3,8,0,10,3,5,0,16},{0,0,0,0,0,0,0,0,2,0,0},{12,4,4,4,6,0,12,4,7,0,14},{2,0,0,0,1,0,2,0,0,0,1},{10,3,5,0,9,4,7,0,11,3,8},{0,0,2,0,0,0,0,0,1,0,0},{0,0,10,3,3,3,3,3,8,0,0}},
            {{20,4,4,4,4,4,4,4,4,4,4,4,4,4,6},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},{13,0,11,3,5,0,11,3,5,0,11,3,5,0,1},{2,0,1,0,2,0,1,0,2,0,1,0,2,0,1},{10,3,8,0,10,3,8,0,10,3,8,0,10,3,8}},
            {{12,4,4,4,4,4,6},{2,0,0,0,0,0,1},{2,0,11,3,5,0,1},{2,0,1,0,2,0,1},{2,0,1,0,10,3,8},{2,0,1,0,0,0,0},{2,0,9,4,4,4,6},{2,0,0,0,0,0,1},{10,3,3,3,5,0,1},{0,0,0,0,2,0,1},{20,4,4,4,7,0,1},{0,0,0,0,0,0,1},{15,3,3,3,3,3,8}},
            {{12,4,4,4,4,4,6,0,13,0,14},{2,0,0,0,0,0,1,0,2,0,1},{2,0,11,3,5,0,1,0,2,0,1},{2,0,1,0,2,0,1,0,2,0,1},{2,0,1,0,2,0,1,0,2,0,1},{2,0,1,0,2,0,1,0,2,0,1},{2,0,1,0,2,0,1,0,2,0,1},{2,0,1,0,2,0,1,0,2,0,1},{2,0,1,0,2,0,1,0,2,0,1},{2,0,1,0,2,0,9,4,7,0,1},{2,0,1,0,2,0,0,0,0,0,1},{10,3,8,0,10,3,3,3,3,3,8}},
            {{12,4,4,4,4,4,4,4,6},{2,0,0,0,0,0,0,0,1},{2,0,11,3,3,3,5,0,1},{2,0,1,0,0,0,2,0,1},{2,0,9,4,17,0,2,0,1},{2,0,0,0,0,0,2,0,1},{10,3,3,3,18,0,2,0,1},{0,0,0,0,0,0,2,0,1},{12,4,4,4,4,4,7,0,1},{2,0,0,0,0,0,0,0,1},{10,3,3,3,3,3,3,3,8}},
            {{0,0,0,0,11,3,5,0,0,0,0},{0,0,0,0,1,0,2,0,0,0,0},{0,0,11,3,8,0,10,3,5,0,0},{0,0,1,0,0,0,0,0,2,0,0},{11,3,8,0,13,0,14,0,10,3,5},{1,0,0,0,2,0,1,0,0,0,2},{9,4,4,4,7,0,9,4,4,4,7}},
            {{12,4,4,4,4,4,4,4,4,4,17},{2,0,0,0,0,0,0,0,0,0,0},{2,0,11,3,3,3,3,3,3,3,18},{2,0,1,0,0,0,0,0,0,0,0},{2,0,1,0,12,4,4,4,4,4,6},{2,0,1,0,2,0,0,0,0,0,1},{2,0,1,0,2,0,11,3,5,0,1},{2,0,1,0,2,0,1,0,2,0,1},{2,0,1,0,10,3,8,0,2,0,1},{2,0,1,0,0,0,0,0,2,0,1},{2,0,9,4,4,4,4,4,7,0,1},{2,0,0,0,0,0,0,0,0,0,1},{10,3,3,3,3,3,3,3,3,3,8}},
            {{13,0,14,0,0,0,0,0,0,0,0,0,0,0},{2,0,1,0,0,0,0,0,0,0,0,0,0,0},{2,0,1,0,0,12,4,4,4,4,4,4,4,6},{2,0,1,0,0,2,0,0,0,0,0,0,0,1},{2,0,9,4,4,7,0,0,11,3,3,3,3,8},{2,0,0,0,0,0,0,0,1,0,0,0,0,0},{10,3,3,3,5,0,0,0,9,4,6,0,0,0},{0,0,0,0,2,0,0,0,0,0,1,0,0,0},{12,4,4,4,7,0,11,3,5,0,1,0,0,0},{2,0,0,0,0,0,1,0,2,0,1,0,0,0},{10,3,3,3,3,3,8,0,2,0,1,0,0,0},{0,0,0,0,0,0,0,0,2,0,1,0,0,0},{0,0,0,0,0,0,0,0,10,3,8,0,0,0}},
            {{12,4,4,4,6,0,12,4,6,0,12,4,4,4,6},{2,0,0,0,1,0,2,0,1,0,2,0,0,0,1},{2,0,11,3,8,0,2,0,1,0,10,3,5,0,1},{2,0,1,0,0,0,2,0,1,0,0,0,2,0,1},{2,0,9,4,4,4,7,0,9,4,4,4,7,0,16},{2,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{2,0,11,3,3,3,5,0,11,3,3,3,5,0,14},{2,0,1,0,0,0,2,0,1,0,0,0,2,0,1},{2,0,9,4,6,0,2,0,1,0,12,4,7,0,1},{2,0,0,0,1,0,2,0,1,0,2,0,0,0,1},{10,3,3,3,8,0,10,3,8,0,10,3,3,3,8}},
            {{12,4,6,0,12,4,4,4,6},{2,0,1,0,2,0,0,0,1},{2,0,9,4,7,0,11,3,8},{2,0,0,0,0,0,1,0,0},{10,3,5,0,14,0,9,4,6},{0,0,2,0,1,0,0,0,1},{12,4,7,0,1,0,11,3,8},{2,0,0,0,1,0,1,0,0},{10,3,3,3,8,0,9,4,6},{0,0,0,0,0,0,0,0,1},{11,3,5,0,11,3,5,0,1},{1,0,2,0,1,0,2,0,1},{16,0,10,3,8,0,10,3,8}}
    };

    public ChallengeSpeed(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        fractionScreenSize = screenHeight / 600;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        for (int e = 0; e < qttStages; e++)
            if (sharedPreferences.contains("stage" + e))
                starsEarned[e] = sharedPreferences.getInt("stage" + e, 0);
        maxStageUnlocked = 0;
        int starsCount = 0;
        for (int e = 0; e < qttStages; e++) starsCount += starsEarned[e];
        for (int e = 0; starsCount >= 0 && e < qttStages; e++) {
            maxStageUnlocked = e;
            starsCount -= minimumStars[e];
        }
        stage = -1;
        for (int f = 0; stage == -1 && f < 3; f++)
            for (int e = 0; stage == -1 && e < qttStages; e++) if (starsEarned[e] == f) stage = e;
        if (stage == -1) stage = 0;
        Typeface typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD);
        paint.setTypeface(typeface);
        paint.setTextAlign(Paint.Align.CENTER);
        Resources r = getResources();
        logo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.logo), (int) Math.ceil(645 * fractionScreenSize), (int) Math.ceil(450 * fractionScreenSize), true);
        btnccw = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.ccw), (int) Math.ceil(120 * fractionScreenSize), (int) Math.ceil(120 * fractionScreenSize), true);
        btncw = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.cw), (int) Math.ceil(120 * fractionScreenSize), (int) Math.ceil(120 * fractionScreenSize), true);
        btnPrevious = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.prev), (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), true);
        btnNext = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.prox), (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), true);
        car[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.carro), (int) Math.ceil(72 * fractionScreenSize), (int) Math.ceil(72 * fractionScreenSize), true);
        numbers[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.n1), (int) Math.ceil(240 * fractionScreenSize), (int) Math.ceil(240 * fractionScreenSize), true);
        numbers[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.n2), (int) Math.ceil(240 * fractionScreenSize), (int) Math.ceil(240 * fractionScreenSize), true);
        numbers[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.n3), (int) Math.ceil(240 * fractionScreenSize), (int) Math.ceil(240 * fractionScreenSize), true);
        numbers[4] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.n4), (int) Math.ceil(240 * fractionScreenSize), (int) Math.ceil(240 * fractionScreenSize), true);
        numbers[5] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.n5), (int) Math.ceil(240 * fractionScreenSize), (int) Math.ceil(240 * fractionScreenSize), true);
        Matrix matrix;
        for (int x = 1; x < 36; x++) {
            matrix = new Matrix();
            matrix.postRotate(x * 10);
            car[x] = Bitmap.createBitmap(car[0], 0, 0, (int) Math.ceil(72 * fractionScreenSize), (int) Math.ceil(72 * fractionScreenSize), matrix, true);
        }
        tile[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.linha), (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), true);
        matrix = new Matrix();
        matrix.postRotate(180);
        tile[2] = Bitmap.createBitmap(tile[1], 0, 0, (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), matrix, true);
        matrix = new Matrix();
        matrix.postRotate(90);
        tile[3] = Bitmap.createBitmap(tile[1], 0, 0, (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), matrix, true);
        matrix = new Matrix();
        matrix.postRotate(270);
        tile[4] = Bitmap.createBitmap(tile[1], 0, 0, (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), matrix, true);
        tile[5] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.curvahorario), (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), true);
        tile[6] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.curvaantihorario), (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), true);
        matrix = new Matrix();
        matrix.postRotate(90);
        tile[7] = Bitmap.createBitmap(tile[5], 0, 0, (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), matrix, true);
        matrix = new Matrix();
        matrix.postRotate(90);
        tile[8] = Bitmap.createBitmap(tile[6], 0, 0, (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), matrix, true);
        matrix = new Matrix();
        matrix.postRotate(180);
        tile[9] = Bitmap.createBitmap(tile[5], 0, 0, (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), matrix, true);
        matrix = new Matrix();
        matrix.postRotate(180);
        tile[10] = Bitmap.createBitmap(tile[6], 0, 0, (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), matrix, true);
        matrix = new Matrix();
        matrix.postRotate(270);
        tile[11] = Bitmap.createBitmap(tile[5], 0, 0, (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), matrix, true);
        matrix = new Matrix();
        matrix.postRotate(270);
        tile[12] = Bitmap.createBitmap(tile[6], 0, 0, (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), matrix, true);
        tile[13] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.inicio), (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), true);
        tile[14] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.chegada), (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), true);
        matrix = new Matrix();
        matrix.postRotate(270);
        tile[15] = Bitmap.createBitmap(tile[13], 0, 0, (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), matrix, true);
        matrix = new Matrix();
        matrix.postRotate(180);
        tile[16] = Bitmap.createBitmap(tile[13], 0, 0, (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), matrix, true);
        matrix = new Matrix();
        matrix.postRotate(90);
        tile[17] = Bitmap.createBitmap(tile[13], 0, 0, (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), matrix, true);
        matrix = new Matrix();
        matrix.postRotate(90);
        tile[18] = Bitmap.createBitmap(tile[14], 0, 0, (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), matrix, true);
        matrix = new Matrix();
        matrix.postRotate(180);
        tile[19] = Bitmap.createBitmap(tile[14], 0, 0, (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), matrix, true);
        matrix = new Matrix();
        matrix.postRotate(270);
        tile[20] = Bitmap.createBitmap(tile[14], 0, 0, (int) Math.ceil(200 * fractionScreenSize), (int) Math.ceil(200 * fractionScreenSize), matrix, true);
        star = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.star), (int) (50 * fractionScreenSize), (int) (50 * fractionScreenSize), true);
        opaqueStar = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.notstar), (int) (50 * fractionScreenSize), (int) (50 * fractionScreenSize), true);
        miniTilesSize = screenHeight / 17;
        for (int x = 1; x < 21; x++)
            miniTiles[x] = Bitmap.createScaledBitmap(tile[x], (int) miniTilesSize, (int) miniTilesSize, true);
        new Timer().schedule(new TimerTask() {
            final int[] deltaDistanceX = {0,-1,-2,-3,-4,-5,-6,-7,-8,-9,-8,-7,-6,-5,-4,-3,-2,-1,0,1,2,3,4,5,6,7,8,9,8,7,6,5,4,3,2,1};
            final int[] deltaDistanceY = {9,8,7,6,5,4,3,2,1,0,-1,-2,-3,-4,-5,-6,-7,-8,-9,-8,-7,-6,-5,-4,-3,-2,-1,0,1,2,3,4,5,6,7,8};

            public void run() {
                if (time >= 0) {
                    time++;
                    if (ccwPressed) {
                        angle3--;
                        if (angle3 == -3) angle3 = 105;
                    }
                    if (cwPressed) {
                        angle3++;
                        if (angle3 == 108) angle3 = 0;
                    }
                    int[] upTile = {-1, -1};
                    if (-carPositionY / 200 / fractionScreenSize > 0 && -carPositionX / 200 / fractionScreenSize > 0 && -carPositionY / 200 / fractionScreenSize < course[stage].length && -carPositionX / 200 / fractionScreenSize < course[stage][0].length && course[stage][(int) (-carPositionY / 200 / fractionScreenSize)][(int) (-carPositionX / 200 / fractionScreenSize)] != 0) {
                        upTile[0] = (int) (-carPositionY / 200 / fractionScreenSize);
                        upTile[1] = (int) (-carPositionX / 200 / fractionScreenSize);
                    }
                    boolean alreadyWalked = false;
                    for (int x = 0; x <= alreadyWalkLenght; x++)
                        if (alreadyWalk[x][0] == upTile[0] && alreadyWalk[x][1] == upTile[1])
                            alreadyWalked = true;
                    if (upTile[0] != -1 && !alreadyWalked) {
                        alreadyWalkLenght++;
                        alreadyWalk[alreadyWalkLenght][0] = upTile[0];
                        alreadyWalk[alreadyWalkLenght][1] = upTile[1];
                    }
                    if (upTile[0] == stageEnds[stage][0] && upTile[1] == stageEnds[stage][1]) {
                        boolean displayFinalScreen = false;
                        if (alreadyWalkLenght >= tilesRequired[stage]) {
                            boolean changeStage = false;
                            int starsDeserved = 1;
                            if (!walkOut || time < stageTimeLimit[stage]) starsDeserved = 2;
                            if (!walkOut && time < stageTimeLimit[stage]) starsDeserved = 3;
                            if (starsEarned[stage] <= starsDeserved) {
                                changeStage = starsEarned[stage] < starsDeserved;
                                Editor editor = sharedPreferences.edit();
                                starsEarned[stage] = starsDeserved;
                                for (int e = 0; e < qttStages; e++)
                                    editor.putInt("stage" + e, starsEarned[e]);
                                editor.commit();
                                int starsCount = 0;
                                for (int e = 0; e < qttStages; e++) starsCount += starsEarned[e];
                                for (int e = 0; starsCount >= 0; e++) {
                                    if (e == qttStages) {
                                        displayFinalScreen = true;
                                        break;
                                    }
                                    maxStageUnlocked = e;
                                    starsCount -= minimumStars[e];
                                }
                            }
                            if (displayFinalScreen) {
                                time = -3;
                                stage = 0;
                            } else if (changeStage) time = -4;
                            else time = -1;
                        } else time = -2;
                    } else {
                        float deltaPositionX = deltaDistanceX[angle3 / 3];
                        float deltaPositionY = deltaDistanceY[angle3 / 3];
                        if (upTile[0] == -1) {
                            walkOut = true;
                            deltaPositionX /= 3;
                            deltaPositionY /= 3;
                        }
                        if (time > 100)
                            carPositionX += fractionScreenSize * deltaPositionX * maxSpeed[stage] * 0.0642f;
                        if (time > 100)
                            carPositionY += fractionScreenSize * deltaPositionY * maxSpeed[stage] * 0.0642f;
                        if (upTile[0] == -1 && time > stageTimeLimit[stage]) time = -2;
                    }
                }
                invalidate();
            }
        }, 0, 10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        float x = me.getX(), y = me.getY();
        if (time > 0) {
            cwPressed = false;
            ccwPressed = false;
            if (x < 330 * fractionScreenSize && y > screenHeight - (380 * fractionScreenSize))
                ccwPressed = true;
            if (x > screenWidth - (330 * fractionScreenSize) && y > screenHeight - (380 * fractionScreenSize))
                cwPressed = true;
            if (me.getAction() == MotionEvent.ACTION_UP) {
                cwPressed = false;
                ccwPressed = false;
            }
        }
        if (time == -1 && me.getAction() == MotionEvent.ACTION_DOWN) {
            if (x < 250 * fractionScreenSize) stage = (stage + qttStages - 1) % qttStages;
            else if (x > screenWidth - (250 * fractionScreenSize)) stage = (stage + 1) % qttStages;
            else if (stage > maxStageUnlocked)
                Toast.makeText(getContext(), R.string.faltaestrela, Toast.LENGTH_SHORT).show();
            else {
                time = 0;
                angle3 = startAngle[stage];
                carPositionX = ((-stageStarts[stage][1] * 200) - 100) * fractionScreenSize;
                carPositionY = ((-stageStarts[stage][0] * 200) - 100) * fractionScreenSize;
                alreadyWalkLenght = 0;
                alreadyWalk[alreadyWalkLenght][1] = stageStarts[stage][0];
                alreadyWalk[alreadyWalkLenght][0] = stageStarts[stage][1];
                cwPressed = false;
                ccwPressed = false;
                walkOut = false;
            }
        }
        if ((time == -4 || time == -3) && me.getAction() == MotionEvent.ACTION_DOWN) {
            stage = -1;
            for (int f = 0; stage == -1 && f < 3; f++)
                for (int e = 0; stage == -1 && e < qttStages; e++)
                    if (starsEarned[e] == f) stage = e;
            if (stage == -1) stage = 0;
        }
        if (time < -1 && me.getAction() == MotionEvent.ACTION_DOWN) time = -1;
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (time == -5) {
            paint.setColor(Color.rgb(0, 65, 120));
            canvas.drawPaint(paint);
            canvas.drawBitmap(logo, (screenWidth - logo.getWidth()) / 2, (screenHeight - logo.getHeight()) / 2, paint);
        } else if (time == -4) {
            canvas.drawBitmap(starsEarned[stage] > 0 ? star : opaqueStar, (float) (screenHeight * 0.04), (float) (screenHeight * 0.04), paint);
            canvas.drawBitmap(starsEarned[stage] > 1 ? star : opaqueStar, (float) (screenHeight * 0.04) + (50 * fractionScreenSize), (float) (screenHeight * 0.04), paint);
            canvas.drawBitmap(starsEarned[stage] > 2 ? star : opaqueStar, (float) (screenHeight * 0.04) + (100 * fractionScreenSize), (float) (screenHeight * 0.04), paint);
            for (int x = 0; x < 8; x++)
                canvas.drawBitmap(tile[3], (x * 200 * fractionScreenSize) - (100 * fractionScreenSize), (float) (screenHeight * 0.68), paint);
            paint.setTextSize((float) (screenHeight * 0.160));
            paint.setColor(Color.rgb(0, 180, 65));
            canvas.drawText(getContext().getString(R.string.passoudefase), screenWidth / 2, screenHeight / 2, paint);
        } else if (time == -3) {
            paint.setColor(Color.rgb(15, 120, 215));
            paint.setTextSize((float) (screenHeight * 0.18));
            canvas.drawText(getContext().getString(R.string.fimdojogo1), screenWidth / 2, (float) (screenHeight * 0.4), paint);
            paint.setColor(Color.rgb(230, 230, 230));
            paint.setTextSize((float) (screenHeight * 0.12));
            canvas.drawText(getContext().getString(R.string.fimdojogo2), screenWidth / 2, (float) (screenHeight * 0.64), paint);
        } else if (time == -2) {
            paint.setColor(Color.rgb(0, 180, 65));
            paint.setTextSize((float) (screenHeight * 0.1));
            canvas.drawText(getContext().getString(R.string.tentedenovo1), screenWidth / 2, (float) (screenHeight * 0.2), paint);
            paint.setColor(Color.rgb(215, 215, 215));
            paint.setTextSize((float) (screenHeight * 0.047));
            canvas.drawText(getContext().getString(R.string.tentedenovo2), screenWidth / 2, (float) (screenHeight * 0.36), paint);
            canvas.drawText(getContext().getString(R.string.tentedenovo3), screenWidth / 2, (float) (screenHeight * 0.44), paint);
            for (int x = 0; x < 8; x++)
                canvas.drawBitmap(tile[3], (x * 200 * fractionScreenSize) - (100 * fractionScreenSize), (float) (screenHeight * 0.54), paint);
        } else if (time == -1) {
            for (int y = 0; y < course[stage].length; y++)
                for (int x = 0; x < course[stage][y].length; x++)
                    if (course[stage][y][x] != 0)
                        canvas.drawBitmap(miniTiles[course[stage][y][x]], ((screenWidth / 2) - (course[stage][y].length * miniTilesSize / 2)) + (x * miniTilesSize), ((screenHeight / 2) - (course[stage].length * miniTilesSize / 2)) + (y * miniTilesSize), paint);
            canvas.drawBitmap(starsEarned[stage] > 0 ? star : opaqueStar, (screenWidth / 2) - (75 * fractionScreenSize), (float) (screenHeight * 0.02), paint);
            canvas.drawBitmap(starsEarned[stage] > 1 ? star : opaqueStar, (screenWidth / 2) - (25 * fractionScreenSize), (float) (screenHeight * 0.02), paint);
            canvas.drawBitmap(starsEarned[stage] > 2 ? star : opaqueStar, (screenWidth / 2) + (25 * fractionScreenSize), (float) (screenHeight * 0.02), paint);
            canvas.drawBitmap(btnPrevious, 40 * fractionScreenSize, ((screenHeight - 300) / 2 * fractionScreenSize), paint);
            canvas.drawBitmap(btnNext, screenWidth - (240 * fractionScreenSize), ((screenHeight - 300) / 2 * fractionScreenSize), paint);
            paint.setColor(Color.rgb(215, 215, 215));
            paint.setTextSize((float) ((screenWidth - (200 * fractionScreenSize)) * 0.06));
            canvas.drawText(maxSpeed[stage] * 5 + " Km/h", screenWidth / 2, (float) (screenHeight * 0.972), paint);
        } else if (time > 0) {
            paint.setColor(Color.rgb(10, 45, 90));
            canvas.drawPaint(paint);
            for (int y = 0; y < course[stage].length; y++)
                for (int x = 0; x < course[stage][y].length; x++)
                    if (course[stage][y][x] != 0)
                        canvas.drawBitmap(tile[course[stage][y][x]], carPositionX + (x * 200 * fractionScreenSize) + (screenWidth / 2), carPositionY + (y * 200 * fractionScreenSize) + (screenHeight / 2), paint);
            canvas.drawBitmap(car[angle3 / 3], (screenWidth - car[angle3 / 3].getWidth()) / 2, (screenHeight - car[angle3 / 3].getHeight()) / 2, paint);
            if (time > 100 && stageTimeLimit[stage] - time > 90 && stageTimeLimit[stage] - time < 510 && (stageTimeLimit[stage] - time) % 100 < 10)
                canvas.drawBitmap(numbers[(stageTimeLimit[stage] - time) / 100], (screenWidth - numbers[(stageTimeLimit[stage] - time) / 100].getHeight()) / 2, (screenHeight - numbers[(stageTimeLimit[stage] - time) / 100].getHeight()) / 2, paint);
            canvas.drawBitmap(star, (float) (screenHeight * 0.04), (float) (screenHeight * 0.04), paint);
            canvas.drawBitmap(!walkOut || stageTimeLimit[stage] > time ? star : opaqueStar, (float) (screenHeight * 0.04) + (50 * fractionScreenSize), (float) (screenHeight * 0.04), paint);
            canvas.drawBitmap(!walkOut && stageTimeLimit[stage] > time ? star : opaqueStar, (float) (screenHeight * 0.04) + (100 * fractionScreenSize), (float) (screenHeight * 0.04), paint);
            paint.setColor(Color.rgb(110, 170, 130));
            if (ccwPressed)
                canvas.drawCircle((80 * fractionScreenSize), screenHeight - (80 * fractionScreenSize), (58 * fractionScreenSize), paint);
            canvas.drawBitmap(btnccw, (20 * fractionScreenSize), screenHeight - (140 * fractionScreenSize), paint);
            if (cwPressed)
                canvas.drawCircle(screenWidth - (80 * fractionScreenSize), screenHeight - (80 * fractionScreenSize), (58 * fractionScreenSize), paint);
            canvas.drawBitmap(btncw, screenWidth - (140 * fractionScreenSize), screenHeight - (140 * fractionScreenSize), paint);
        }
    }
}