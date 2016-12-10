package com.mcpubba.game.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mcpubba.game.util.Animation;

import java.util.HashMap;

/**
 * Created by Will on 2016-12-07.
 */

public class HitAnim {
    HashMap<String, Animation> hit;
    int currentHit = -1;
    float scale = 1;
    int time;
    float angle = ((float)Math.random()-0.5f)*20;
    public HitAnim(HashMap<String,Animation> h){
        hit=h;
    }
    public void draw(Batch b, float x, float y, float scale2){
        String s = currentHit<320?currentHit+"":"300g";
        if(currentHit == -1)return;
        if(currentHit == 0)hit.get(s).draw(b,
                x-hit.get(s).getWidth()/2,
                y,
                hit.get(s).getWidth(),
                hit.get(s).getHeight(),
                hit.get(s).getWidth()/2,
                hit.get(s).getHeight()/2,
                scale2,
                angle);
        else hit.get(s).draw(b,
                x-hit.get(s).getWidth()/2,
                y,
                hit.get(s).getWidth(),
                hit.get(s).getHeight(),
                hit.get(s).getWidth()/2,
                hit.get(s).getHeight()/2,
                scale*scale2,
                0);
        if(time<3){
            scale += 0.05;
        }else if(time<20){
            scale -= 0.025;
            if(scale<1)scale=1;
        }else{
            currentHit = -1;
        }
        time++;
    }
    public void setCurrentHit(int i){
        if(currentHit==0)angle = ((float)Math.random()-0.5f)*20;
        currentHit = i;
        scale = 1;
        time = 0;
    }
    public HashMap<String, Animation> getHits(){
        return hit;
    }
}
