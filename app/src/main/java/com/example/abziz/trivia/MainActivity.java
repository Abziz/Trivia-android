package com.example.abziz.trivia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapButtonGroup;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {



    @BindView(R.id.btn_group) BootstrapButtonGroup bbg;
    @BindView(R.id.select) Spinner select;
    @BindView(R.id.overlay) RelativeLayout overlay;
    @BindView(R.id.spinner) AVLoadingIndicatorView spinner;
    @BindView(R.id.name) EditText name;
    @BindView(R.id.true_false) BootstrapButton true_false;
    @BindView(R.id.multiple) BootstrapButton multiple;
    @BindView(R.id.start) BootstrapButton start;

    HashMap<String,String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        InitElements();
        setClickListeners();
    }



    private void InitElements(){
        bbg.getChildAt(1).setSelected(true);
        $.ajax("https://opentdb.com/api_category.php", new function() {
            @Override
            public void success(JSONObject data) {
                try {
                    categories = new HashMap<String, String>();

                    JSONArray all_categories = data.getJSONArray("trivia_categories");
                    for (int i = 0; i < all_categories.length(); i++) {
                        JSONObject cat = all_categories.getJSONObject(i);
                        categories.put(cat.getString("name"),"&category="+cat.getString("id"));
                    }
                    ArrayList<String> list = new ArrayList<String>(categories.keySet());
                    categories.put("Any category","");
                    list.add(0,"Any Category");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                            R.layout.support_simple_spinner_dropdown_item,
                            list);
                    select.setAdapter(adapter);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setClickListeners(){
        for (int i = 0 ; i < bbg.getChildCount(); i++) {
            ((BootstrapButton)bbg.getChildAt(i)).setOnCheckedChangedListener(new BootstrapButton.OnCheckedChangedListener() {
                @Override
                public void OnCheckedChanged(BootstrapButton bootstrapButton, boolean isChecked) {
                    boolean isOneChecked = false;
                    for (int j = 0; j < bbg.getChildCount() ; j++) {
                        isOneChecked = isOneChecked || bbg.getChildAt(j).isSelected();
                    }
                    if( !isOneChecked ){
                        bootstrapButton.setSelected(true);
                        bootstrapButton.setChecked(true);
                    }
                }
            });
        }
    }

    @OnClick(R.id.start)
    public void StartGame(){
        Spin();
        GameInfo.instance().category = categories.get(select.getSelectedItem().toString());
        GameInfo.instance().name = name.getText().toString();
        String type = "";
        if( true_false.isSelected() && !multiple.isSelected()){
            type="&type=boolean";
        }else if(!true_false.isSelected() && multiple.isSelected()){
            type="&type=multiple";
        }
        GameInfo.instance().questionType = type;

        $.ajax("https://opentdb.com/api_token.php?command=request", new function() {
            @Override
            public void success(JSONObject data) {
                try {
                    GameInfo.instance().session = "&token=" + data.getString("token");
                }catch(Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }finally {
                    Stop();
                    Intent intent = new Intent(MainActivity.this, Trivia.class);
                    startActivity(intent);
                }
            }
        });

    }
    private void Spin(){
        overlay.setVisibility(View.VISIBLE);
        spinner.smoothToShow();
    }

    private void Stop(){
        overlay.setVisibility(View.INVISIBLE);
        spinner.smoothToHide();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
