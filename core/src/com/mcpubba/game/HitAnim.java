package com.mcpubba.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.HashMap;

/**
 * Created by Will on 2016-12-07.
 */

public class HitAnim {
    HashMap<Integer, Texture> hit;
    int currentHit = -1;
    float scale = 1;
    int time;
    public HitAnim(HashMap<Integer, Texture> h){
        hit=h;
    }
    public void draw(Batch b, int x, int y, float scale2){
        if(currentHit == -1)return;
        if(currentHit == 0)b.draw(hit.get(currentHit),
                x-hit.get(currentHit).getWidth()/2,
                y,
                hit.get(currentHit).getWidth()/2,
                hit.get(currentHit).getHeight()/2,
                hit.get(currentHit).getWidth(),
                hit.get(currentHit).getHeight(),
                scale2,
                scale2,
                ((float)Math.random()-0.5f),
                0,0,
                hit.get(currentHit).getWidth(),
                hit.get(currentHit).getHeight(),
                false,false);
        else b.draw(hit.get(currentHit),
                x-hit.get(currentHit).getWidth()/2,
                y,
                hit.get(currentHit).getWidth()/2,
                hit.get(currentHit).getHeight()/2,
                hit.get(currentHit).getWidth(),
                hit.get(currentHit).getHeight(),
                scale*scale2,
                scale*scale2,
                0,
                0,0,
                hit.get(currentHit).getWidth(),
                hit.get(currentHit).getHeight(),
                false,false);
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
        currentHit = i;
        scale = 1;
        time = 0;
    }
    public HashMap<Integer, Texture> getHits(){
        return hit;
    }
}
