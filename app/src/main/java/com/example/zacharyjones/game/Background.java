package com.example.zacharyjones.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {

    Bitmap BackBitmap;
    int x, y;
    int ScreenWidth;
    int Count_Background;
    GamePanel root_gamepanel;

    public Background(Bitmap bitmap, int Screen_w, GamePanel Game_panel) {
        this.BackBitmap = bitmap;
        this.x = 0;
        this.y = 0;
        this.ScreenWidth = Screen_w;
        Count_Background = ScreenWidth / BackBitmap.getWidth() + 1;
        root_gamepanel = Game_panel;
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < Count_Background + 1; i++) {
            if (canvas != null)
                canvas.drawBitmap(BackBitmap, BackBitmap.getWidth() * i + x, y, null);
        }
        if (Math.abs(x) > BackBitmap.getWidth()) {
            x = x + BackBitmap.getWidth();
        }

    }

    public void update(float dt) {
        x = (int) (x - root_gamepanel.ShipSpeed * dt);
    }
}