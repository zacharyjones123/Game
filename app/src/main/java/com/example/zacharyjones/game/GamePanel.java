package com.example.zacharyjones.game;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public MainThread thread;
    public boolean Pause_game;
    private Background background;
    private Ship ship;
    private Barriermanager BM;
    private Bonus coin;
    public float ShipSpeed;
    public int ScreenWidth;
    public int Screenheigt;
    public Game game;


    public GamePanel(Context context, Game game,int ScreenWidth,int Screenheigt) {
        super(context);
        getHolder().addCallback(this);
        this.game = game;
        thread = new MainThread(getHolder(),this);
        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.game_fon), ScreenWidth, this);
        BM = new Barriermanager(BitmapFactory.decodeResource(getResources(), R.drawable.barier), this);
        BM.setScreen(ScreenWidth, Screenheigt);
        ship = new Ship(BitmapFactory.decodeResource(getResources(), R.drawable.player), 100, 0, ScreenWidth, Screenheigt);
        coin = new Bonus(BitmapFactory.decodeResource(getResources(), R.drawable.bonus), -200,-200);
        ArrayList<Bitmap> animation = new ArrayList<Bitmap>();
        animation.add(BitmapFactory.decodeResource(getResources(), R.drawable.boom1));
        animation.add(BitmapFactory.decodeResource(getResources(), R.drawable.boom2));
        animation.add(BitmapFactory.decodeResource(getResources(), R.drawable.boom3));
        animation.add(BitmapFactory.decodeResource(getResources(), R.drawable.boom4));
        ship.setBoomAnimation(animation);

        coin.setBarrierManager(BM);
        setFocusable(true);
        ShipSpeed = ScreenWidth/2.f;
        this.ScreenWidth = ScreenWidth;
        this.Screenheigt = Screenheigt;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            ship.up=true;
        }
        if (event.getAction()==MotionEvent.ACTION_UP){
            ship.up=false;
        }

        return true;
    }

    void Draw(Canvas canvas){
        if (!Pause_game)
            if (canvas!=null){
                canvas.drawColor(Color.BLACK);
                background.draw(canvas);
                coin.draw(canvas);
                ship.draw(canvas);
                BM.draw(canvas);
            }
    }

    void Update(float dt){

        ship.update(dt);
        if (!ship.death){
            background.update(dt);
            coin.update(dt);
            BM.update(dt);
            ArrayList<Point> coin_point = new ArrayList<Point>(coin.GetArray());
            if (ship.bump(coin_point.get(0), coin_point.get(1), coin_point.get(2), coin_point.get(3))){
                coin.setX(-200);
                coin.setY(-200);
                Message msg = BM.game_panel.game.handler.obtainMessage();
                msg.what = 0;
                BM.game_panel.game.handler.sendMessage(msg);

            }

            for (int i=0; i<BM.TopWalls.size();i++){
                ArrayList< Point>temp = new ArrayList<Point>(BM.TopWalls.get(i).GetArray());
                ArrayList< Point>temp2 = new ArrayList<Point>(BM.BottomWalls.get(i).GetArray());
                if ((ship.bump(temp.get(0), temp.get(1), temp.get(2), temp.get(3)))||(ship.bump(temp2.get(0), temp2.get(1), temp2.get(2), temp2.get(3))))
                {
                    ship.death=true;
                    MediaPlayer mp = MediaPlayer.create(game, R.raw.boom);
                    mp.start();
                    Message msg = BM.game_panel.game.handler.obtainMessage();
                    msg.what = 1;
                    BM.game_panel.game.handler.sendMessage(msg);
                }
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        boolean retry = true;
        while (retry) {
            try{
                thread.join();
                retry=false;
            } catch (InterruptedException e){

            }

        }

    }

}