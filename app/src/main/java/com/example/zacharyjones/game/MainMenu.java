package com.example.zacharyjones.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainMenu extends Activity {
    /** Called when the activity is first created. */
    MediaPlayer MainMenuMusic;
    RelativeLayout Btn;
    ImageView ImageButton;
    TextView txt;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final TextView more =(TextView)findViewById(R.id.wwwtext);
        if (more!=null){
            more.setOnTouchListener(new OnTouchListener() {

                public boolean onTouch(View arg0, MotionEvent Motionevent) {
                    if (Motionevent.getAction()==MotionEvent.ACTION_OUTSIDE)
                    {
                        more.setTextColor(Color.rgb(99, 183, 255));
                    }
                    if (Motionevent.getAction()==MotionEvent.ACTION_DOWN)
                    {
                        more.setTextColor(Color.WHITE);
                    }
                    if (Motionevent.getAction()==MotionEvent.ACTION_UP)
                    {
                        more.setTextColor(Color.rgb(99, 183, 255));
                    }

                    return false;
                }
            });
            more.setOnClickListener(new OnClickListener() {

                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("http://game-era.com"));
                    startActivity(intent);

                }
            });
        }

        Btn = (RelativeLayout) findViewById(R.id.btn_start);
        ImageButton = (ImageView) findViewById(R.id.ing_btn);
        txt = (TextView) findViewById(R.id.text_start);

        Typeface Custom = Typeface.createFromAsset(getAssets(), "font.ttf");
        txt.setTypeface(Custom);

        Btn.setOnTouchListener(new TochButton(ImageButton));

        Btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this, Game.class);
                startActivity(myIntent);
            }
        });

    }

    @Override
    protected void onStart() {
        MainMenuMusic = MediaPlayer.create(MainMenu.this, R.raw.main);
        MainMenuMusic.setVolume(0.3f, 0.3f);
        MainMenuMusic.start();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (MainMenuMusic.isPlaying())
            MainMenuMusic.stop();
        super.onStop();
    }

}