package com.example.abziz.trivia;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Abziz on 22/05/2017.
 */

public class $ {
   static AsyncHttpClient client = null;
   public static void ajax(String url, final function f){
      if(client == null) {
         client = new AsyncHttpClient();
      }
      client.get(url,new JsonHttpResponseHandler(){
         @Override
         public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               f.success(response);
         }
      });
   }
}


