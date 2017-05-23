package com.example.abziz.trivia;

import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Abziz on 22/05/2017.
 */

public class Question {
    static final int BOOLEAN = 2,MULTIPLE =4;
    public String category;
    public String type;
    public String difficulty;



    public String question;
    public String correct_answer;
    public String[] incorrect_answers;
    public Question(JSONObject data){
        try {
            JSONObject q = data.getJSONArray("results").getJSONObject(0);
            category = Html.fromHtml(q.getString("category")).toString();
            type = q.getString("type");
            difficulty = q.getString("difficulty");
            question = Html.fromHtml(q.getString("question")).toString();
            correct_answer = Html.fromHtml(q.getString("correct_answer")).toString();
            if( type == "boolean"){
                incorrect_answers = new String[BOOLEAN];
            }else{
                incorrect_answers = new String[MULTIPLE];
            }
            JSONArray incorrect =  q.getJSONArray("incorrect_answers");
            for (int i = 0; i < incorrect.length() ; i++) {
                incorrect_answers[i] = Html.fromHtml(incorrect.getString(i)).toString();
            }
            incorrect_answers[incorrect.length()] =  correct_answer;
        } catch (JSONException e) {

        }
    }

    @Override
    public String toString() {
        return "Question{" +
                "category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", question='" + question + '\'' +
                ", correct_answer='" + correct_answer + '\'' +
                ", incorrect_answers=" + Arrays.toString(incorrect_answers) +
                '}';
    }

}
