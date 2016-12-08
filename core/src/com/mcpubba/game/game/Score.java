package com.mcpubba.game.game;


import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

/**
 * Created by Will on 2016-12-05.
 */

public class Score {
    private float acc = 1;
    private int n300p;
    private int n300;
    private int n200;
    private int n100;
    private int n50;
    private int misses;
    private int combo;

    public int getMaxCombo() {
        return maxCombo;
    }

    private int maxCombo;
    private int bonus;
    private int score;
    private int total;
    private float mult = 1;
    private float div = 1;
    private ArrayList<Integer> rawAcc;

    public Score(int totalNotes){
        rawAcc = new ArrayList<Integer>();
        score = 0;
        bonus = 100;
        total = totalNotes;
    }
    public float getAcc() {
        return acc;
    }

    public int getMisses() {
        return misses;
    }

    public int getCombo() {
        return combo;
    }

    public void noteHit(int acc, int raw){
        if(raw != Integer.MIN_VALUE){
            rawAcc.add(raw);
        }
        switch(acc){
            case 320:
                bonus += 2;
                if(bonus>100)bonus = 100;
                score += (1000000* mult *0.5f/total)*(32*Math.sqrt(bonus)/320);
                n300p++;
                break;
            case 300:
                bonus += 1;
                if(bonus>100)bonus = 100;
                score += (1000000* mult *0.5f/total)*(32*Math.sqrt(bonus)/320);
                n300++;
                break;
            case 200:
                bonus -= 8/div;
                if(bonus<0)bonus = 0;
                score += (1000000* mult *0.5f/total)*(16*Math.sqrt(bonus)/320);
                n200++;
                break;
            case 100:
                bonus -= 24/div;
                if(bonus<0)bonus = 0;
                score += (1000000* mult *0.5f/total)*(8*Math.sqrt(bonus)/320);
                n100++;
                break;
            case 50:
                bonus -= 44/div;
                if(bonus<0)bonus = 0;
                score += (1000000* mult *0.5f/total)*(4*Math.sqrt(bonus)/320);
                n50++;
                break;
            case 0:
                bonus = 0;
                misses++;
                if(combo>maxCombo)maxCombo=combo;
                combo = -1;
                break;
        }
        score += (1000000* mult *0.5f/total)*(acc/320.0f);
        combo++;
        calculateAcc();
    }
    public void sliderBreak(){
        if(combo>maxCombo)maxCombo=combo;
        combo = 0;
    }
    public void sliderHeld(){
        combo++;
    }
    public float getMean(){
        float mean = 0;
        for(int i : rawAcc){
            mean += i;
        }
        return mean / rawAcc.size();
    }
    public void addTime(int time){
        rawAcc.add(time);
    }
    public double getUR(){
        float mean = getMean();
        float var = 0;
        for(int i : rawAcc){
            var += (i-mean)*(i-mean);
        }
        return Math.sqrt(var/(rawAcc.size()-1))*10;
    }
    public int getScore(){
        return score;
    }
    private void calculateAcc(){
        acc = (n300p+n300+n200*2/3.0f+n100/3.0f+n50/6.0f)/(n300p+n300+n200+n100+n50+misses);
    }
}
