package com.mcpubba.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.HashMap;

/**
 * Created by Will on 2016-12-07.
 */

public class HitAnim {
    float stretch;
    HashMap<Integer, Texture> hit;
    int currentHit = -1;
    float scale = 1;
    int time;
    public HitAnim(HashMap<Integer, Texture> h){
        hit=h;
    }
    public void draw(Batch b, int x, int y, float scale2){
        if(currentHit == -1)return;
        b.draw(hit.get(currentHit), (2*x-hit.get(currentHit).getWidth()*scale*scale2)/2, y,
                hit.get(currentHit).getWidth()*scale*scale2, hit.get(currentHit).getHeight()*scale*scale2);
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
