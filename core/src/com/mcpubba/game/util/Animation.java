package com.mcpubba.game.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * Created by wkennedy on 2016-12-08.
 */

public class Animation {
    int framesElapsed;
    Texture[] keyFrames;
    int[] duration;
    public void start(){

    }
    public void render(){

    }
    public boolean isFinished(){
        return framesElapsed>getDuration();
    }
    public int getDuration(){
        int i = 0;
        for(int j : duration){
            i+=j;
        }
        return i;
    }
}
