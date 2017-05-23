package com.example.abziz.trivia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Trivia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

    }


    private void newGame(){
        GameInfo.instance().score = 0;
        GameInfo.instance().num = 0;
        GameInfo.instance().lives = 5;
    }
}
