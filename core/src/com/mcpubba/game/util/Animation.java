package com.mcpubba.game.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;

/**
 * Created by wkennedy on 2016-12-08.
 */

public class Animation {
    int framesElapsed;
    Texture[] keyFrames;
    boolean loop;
    boolean running;
    float drawsPerFrame;
    public Animation(Texture[] k, int framerate, boolean l){
        keyFrames = k;
        loop = l;
        drawsPerFrame = 60f/framerate;
    }
    public Animation(Texture t){
        this(new Texture[]{t}, 60, true);
    }
    public Animation(ArrayList<Texture> t, int framerate, boolean l){
        this(t.toArray(new Texture[t.size()]), framerate, l);
    }
    public Animation start(){
        framesElapsed = 0;
        running = true;
        return this;
    }
    public void stop(){
        running = false;
    }
    public void draw(Batch b, float x, float y){
        if(!loop&&framesElapsed/drawsPerFrame>=keyFrames.length){
            stop();
            return;
        }
        b.draw(keyFrames[(int)(framesElapsed/drawsPerFrame)%keyFrames.length], x, y,
                keyFrames[(int)(framesElapsed/drawsPerFrame)%keyFrames.length].getWidth(),
                keyFrames[(int)(framesElapsed/drawsPerFrame)%keyFrames.length].getHeight());
        framesElapsed++;
    }
    public void draw(Batch b, float x, float y, float w, float h){
        if(!loop&&framesElapsed/drawsPerFrame>=keyFrames.length){
            stop();
            return;
        }
        b.draw(keyFrames[(int)(framesElapsed/drawsPerFrame)%keyFrames.length], x, y, w, h);
        framesElapsed++;
    }
    public void draw(Batch b, float x, float y, float w, float h,
                     boolean flipX, boolean flipY){

        if(!loop&&framesElapsed/drawsPerFrame>=keyFrames.length){
            stop();
            return;
        }
        b.draw(keyFrames[(int)(framesElapsed/drawsPerFrame)%keyFrames.length], x, y,
                    w, h,
                    0,0,
                    keyFrames[(int)(framesElapsed/drawsPerFrame)%keyFrames.length].getWidth(),
                    keyFrames[(int)(framesElapsed/drawsPerFrame)%keyFrames.length].getHeight(),
                    flipX, flipY);
        framesElapsed++;
    }
    public void draw(Batch b, float x, float y, float w, float h,
                     float originX, float originY,
                     float scale, float rot){

        if(!loop&&framesElapsed/drawsPerFrame>=keyFrames.length){
            stop();
            return;
        }
        b.draw(keyFrames[(int)((framesElapsed/drawsPerFrame)%keyFrames.length)],
                    x, y, originX, originY,
                    w, h,
                    scale, scale,
                    rot,0,0,
                    keyFrames[(int)((framesElapsed/drawsPerFrame)%keyFrames.length)].getWidth(),
                    keyFrames[(int)((framesElapsed/drawsPerFrame)%keyFrames.length)].getHeight(),
                    false, false);
        framesElapsed++;
    }
    public boolean isFinished(){
        return !running || !loop&&framesElapsed>keyFrames.length*drawsPerFrame;
    }
    public int getWidth(){
        return keyFrames[0].getWidth();
    }
    public int getHeight(){
        return keyFrames[0].getHeight();
    }
    public boolean isRunning() {
        return running;
    }
}
