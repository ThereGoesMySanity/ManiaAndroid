package com.mcpubba.game.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mcpubba.game.screens.GameScreen;

/**
 * Created by Will on 2016-12-10.
 */

public class ScoreAnim {
    Texture[] score;
    int displayedScore;
    Skin skin;
    public ScoreAnim(Texture[] sc, Skin s){
        score = sc;
        skin = s;
    }
    public void draw(Batch b, float x, float y, boolean rightJust, GameScreen g){
        if(displayedScore<g.map.getScore().getScore())
            displayedScore+=1+(g.map.getScore().getScore()-displayedScore)/5;
        String ss = displayedScore+"";
        int gap = -Integer.parseInt(g.main.skin.ini.getFonts("ScoreOverlap"));
        int xOff = 0;
        int yOff = score[0].getHeight()*g.main.skin.textScale;
        for(int i = 0; i < ss.length(); i++){
            xOff += (gap+score[ss.charAt(i)-48].getWidth()*g.main.skin.textScale);
        }
        for(int i = 0; i < ss.length(); i++){
            b.draw(score[ss.charAt(i)-48], rightJust?
                    x-xOff:x+xOff,
                    y-yOff,
                    score[ss.charAt(i)-48].getWidth()*g.main.skin.textScale,
                    score[ss.charAt(i)-48].getHeight()*g.main.skin.textScale);
            xOff -= (gap+score[ss.charAt(i)-48].getWidth()*g.main.skin.textScale);
        }
    }
}
