package com.mcpubba.game.game;


/**
 * Created by Will on 2016-12-05.
 */

public class Score {
    public float getAcc() {
        return acc;
    }

    public int getMisses() {
        return misses;
    }

    public int getCombo() {
        return combo;
    }

    private float acc;
    private int n300p;
    private int n300;
    private int n200;
    private int n100;
    private int n50;
    private int misses;
    private int combo;
    public void noteHit(int acc){
        switch(acc){
            case 320:
                n300p++;
                break;
            case 300:
                n300++;
                break;
            case 200:
                n200++;
                break;
            case 100:
                n100++;
                break;
            case 50:
                n50++;
                break;
            case 0:
                misses++;
                combo = -1;
                break;
        }
        combo++;
        calculateAcc();
    }
    public void sliderBreak(){
        combo = 0;
    }
    public void sliderHeld(){
        combo++;
    }
    private void calculateAcc(){
        acc = (n300p+n300+n200*2/3.0f+n100/3.0f+n50/6.0f)/(n300p+n300+n200+n100+n50+misses);
    }
}
