package com.example.zacharyjones.game;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Game extends Activity {


    final static int UPDATE_SCORE = 0;
    final static int DEATH = 1;
    final static int LOSE = 2;

    View pausaButton;
    View PauseMenu;
    View WinDialog;
    View LoseDialog;

    MediaPlayer MainMusic;


    RelativeLayout Rel_main_game;
    GamePanel game_panel;
    TextView txt;
    int get_coins = 0;
    int score = 0;

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_SCORE) {

                i_get_coin();
            }
            if (msg.what == DEATH) {
                postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Message msg = handler.obtainMessage();
                        msg.what = LOSE;
                        handler.sendMessage(msg);

                    }
                }, 3000);
            }
            if (msg.what == LOSE) {
                i_lose();
            }

            super.handleMessage(msg);
        }
    };

    OnClickListener Continue_list = new OnClickListener() {

        @Override
        public void onClick(View v) {
            PauseMenu.setVisibility(View.GONE);
            pausaButton.setVisibility(View.VISIBLE);
            game_panel.Pause_game = false;
        }
    };

    OnClickListener To_Main_Menu_list = new OnClickListener() {

        @Override
        public void onClick(View v) {
            game_panel.thread.setRunning(false);
            Game.this.finish();

        }
    };


    OnClickListener Pausa_click = new OnClickListener() {

        @Override
        public void onClick(View v) {
            pausaButton.setVisibility(View.GONE);
            PauseMenu.setVisibility(View.VISIBLE);
            game_panel.Pause_game = true;


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        Rel_main_game = (RelativeLayout) findViewById(R.id.main_game_rl);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);

        final int heightS = dm.heightPixels;
        final int widthS = dm.widthPixels;
        game_panel = new GamePanel(getApplicationContext(), this, widthS, heightS);
        Rel_main_game.addView(game_panel);


        RelativeLayout RR = new RelativeLayout(this);
        RR.setBackgroundResource(R.drawable.btn);
        RR.setGravity(Gravity.CENTER);
        Rel_main_game.addView(RR, 400, 150);
        RR.setX(0);
        txt = new TextView(this);
        Typeface Custom = Typeface.createFromAsset(getAssets(), "font.ttf");
        txt.setTypeface(Custom);
        txt.setTextColor(Color.YELLOW);
        txt.setText("Score: " + score);
        RR.addView(txt);

        LayoutInflater myInflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        pausaButton = myInflater.inflate(R.layout.pause, null, false);
        pausaButton.setX(widthS - 250);
        pausaButton.setY(0);
        Rel_main_game.addView(pausaButton);

        ImageView pauseImage = (ImageView) pausaButton.findViewById(R.id.PauseImage);
        pausaButton.setOnTouchListener(new TochButton(pauseImage));
        pausaButton.setOnClickListener(Pausa_click);
        pausaButton.getLayoutParams().height = 250;
        pausaButton.getLayoutParams().width = 250;

        PauseMenu = myInflater.inflate(R.layout.pause_menu, null, false);
        Rel_main_game.addView(PauseMenu);
        PauseMenu.setVisibility(View.GONE);

        ImageView Cont = (ImageView) PauseMenu.findViewById(R.id.imCont);
        ImageView MainMenuTo = (ImageView) PauseMenu.findViewById(R.id.toMain);
        Cont.setOnTouchListener(new TochButton(Cont));
        Cont.setOnClickListener(Continue_list);
        MainMenuTo.setOnTouchListener(new TochButton(MainMenuTo));
        MainMenuTo.setOnClickListener(To_Main_Menu_list);

        WinDialog = myInflater.inflate(R.layout.win, null, false);
        Rel_main_game.addView(WinDialog);
        ImageView Win_to_main = (ImageView) WinDialog.findViewById(R.id.toMain);
        Win_to_main.setOnTouchListener(new TochButton(Win_to_main));
        Win_to_main.setOnClickListener(To_Main_Menu_list);
        WinDialog.setVisibility(View.GONE);

        LoseDialog = myInflater.inflate(R.layout.lose, null, false);
        Rel_main_game.addView(LoseDialog);
        ImageView Lose_to_main = (ImageView) LoseDialog.findViewById(R.id.toMain);
        Lose_to_main.setOnTouchListener(new TochButton(Lose_to_main));
        Lose_to_main.setOnClickListener(To_Main_Menu_list);
        LoseDialog.setVisibility(View.GONE);

        MainMusic = MediaPlayer.create(Game.this, R.raw.music);
        MainMusic.setVolume(0.3f, 0.3f);
        MainMusic.start();

    }

    @Override
    public void onBackPressed() {
        pausaButton.setVisibility(View.GONE);
        PauseMenu.setVisibility(View.VISIBLE);
        game_panel.Pause_game = true;
    }

    @Override
    protected void onStop() {
        if (MainMusic.isPlaying())
            MainMusic.stop();
        super.onStop();
    }

    protected void i_lose() {
        // TODO Auto-generated method stub

        if (MainMusic.isPlaying())
            MainMusic.stop();
        MainMusic = MediaPlayer.create(Game.this, R.raw.lose);
        MainMusic.start();
        game_panel.Pause_game = true;
        LoseDialog.setVisibility(View.VISIBLE);
    }

    protected void i_get_coin() {
        get_coins++;
        score += 200;
        txt.setText("Score: " + score);
        MediaPlayer mp = MediaPlayer.create(Game.this, R.raw.coin);
        mp.start();
        if (get_coins == 20) {
            i_win();
        }

    }

    private void i_win() {
        // TODO Auto-generated method stub
        if (MainMusic.isPlaying())
            MainMusic.stop();
        MainMusic = MediaPlayer.create(Game.this, R.raw.win);
        MainMusic.start();


        game_panel.Pause_game = true;
        WinDialog.setVisibility(View.VISIBLE);

    }
}