package com.mcpubba.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mcpubba.game.ManiaAndroid;
import com.mcpubba.game.screens.GameScreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Will on 2016-12-09.
 */

public class SkinIniParser {
    HashMap<String, String> general;
    HashMap<String, String> colors;
    HashMap<String, String> fonts;
    HashMap<String, HashMap<String, String>> mania;
    final Pattern p = Pattern.compile("(.*): (.*)");
    FileHandle dir;
    SkinIniParser defaultSkinIni;
    public SkinIniParser(FileHandle f){
        dir = f.parent();
        general = new HashMap<String, String>();
        colors = new HashMap<String, String>();
        fonts = new HashMap<String, String>();
        mania = new HashMap<String, HashMap<String, String>>();
        Scanner s = new Scanner(f.read());
        String str = s.nextLine();
        while (s.hasNextLine() && !str.equals("[General]"))str = s.nextLine();

        while (s.hasNextLine() && !str.equals("[Colours]")) {
            Matcher m = p.matcher(str);
            if (m.matches()) general.put(m.group(1), m.group(2));
            str = s.nextLine();
        }

        while (s.hasNextLine() && !str.equals("[Fonts]")){
            Matcher m = p.matcher(str);
            if (m.matches()){
                colors.put(m.group(1), m.group(2));
            }
            str = s.nextLine();
        }
        while(s.hasNextLine()&&!str.equals("[Mania]")){
            Matcher m = p.matcher(str);
            if (m.matches()){
                fonts.put(m.group(1), m.group(2));
            }
            str = s.nextLine();
        }

        while(s.hasNextLine()) {
            str = s.nextLine();
            String k = str.substring(6);
            if(str.startsWith("Keys: "))mania.put(k, new HashMap<String, String>());
            while(s.hasNextLine()&&!str.equals("[Mania]")){
                Matcher m = p.matcher(str);
                if (m.matches()) mania.get(k).put(m.group(1), m.group(2));
                str = s.nextLine();
            }
        }
        if(!dir.equals(Gdx.files.internal("mania")))defaultSkinIni =
                new SkinIniParser(fileIgnoreCase(Gdx.files.internal("mania"), "skin.ini"));
    }
    public static FileHandle fileIgnoreCase(FileHandle parent, final String str){
        return parent.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.equalsIgnoreCase(str);
            }
        })[0];
    }

    public String getGeneral(String s) {
        if(general.containsKey(s))
            return general.get(s);
        else if(!dir.equals(Gdx.files.internal("mania")))
            return defaultSkinIni.getGeneral(s);
        Gdx.app.log("infinite loop o no", s);
        return null;
    }

    public String getColors(String s) {
        if(colors.containsKey(s))
            return colors.get(s);
        else if(!dir.equals(Gdx.files.internal("mania")))
            return defaultSkinIni.getColors(s);
        Gdx.app.log("infinite loop o no", s);
        return null;
    }

    public String getFonts(String s) {
        if(fonts.containsKey(s))
            return fonts.get(s);
        else if(!dir.equals(Gdx.files.internal("mania")))
            return defaultSkinIni.getFonts(s);
        Gdx.app.log("infinite loop o no", s);
        return null;
    }
    public String getMania(int keys, String s){
        if(mania.containsKey(keys+"")&&mania.get(keys+"").containsKey(s))
            return mania.get(keys+"").get(s);
        else if(!dir.equals(Gdx.files.internal("mania")))
            return defaultSkinIni.getMania(keys, s);
        Gdx.app.log("infinite loop o no", s);
        return null;
    }
}
