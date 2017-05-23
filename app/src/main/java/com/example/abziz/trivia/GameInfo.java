package com.example.abziz.trivia;

import android.icu.util.ULocale;

/**
 * Created by Abziz on 23/05/2017.
 */

class GameInfo {
    private static final GameInfo ourInstance = new GameInfo();

    static GameInfo instance() {
        return ourInstance;
    }
    int num;
    public String name;
    public int score;
    public int lives;
    public String category;
    public String questionType;
    public String session;



    private GameInfo() {
    }
}
