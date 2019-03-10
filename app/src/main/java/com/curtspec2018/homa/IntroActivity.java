package com.curtspec2018.homa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.curtspec2018.homa.vo.Schedule;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class IntroActivity extends AppCompatActivity {

    ImageView iv;
    Animation dropDown;
    Animation zoomOut;

    Intent loginIntent, mainIntent;
    boolean isAuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        loginIntent = new Intent(this, LoginActivity.class);
        mainIntent = new Intent(this, MainActivity.class);

        iv = findViewById(R.id.iv_logo);

        dropDown = AnimationUtils.loadAnimation(this, R.anim.drop_down);
        zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out);

        zoomOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(isAuto) startActivity(mainIntent);
                else startActivity(loginIntent);
                finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        t.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        iv.startAnimation(dropDown);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            iv.startAnimation(zoomOut);
        }
    };

    Thread t = new Thread(){
        @Override
        public void run() {
            SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
            isAuto = preferences.getBoolean("auto", false);
            if (isAuto){
                String id = preferences.getString("id", null);
                G.setId(id);

                //load Memos....=======================================================================
                String url = G.SERVER_URL + "loadMemos.php?id=" + G.getId();
                Gson gson = new Gson();
                ArrayList<Schedule> memos = new ArrayList<>();
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Schedule memo = null;
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject recode = response.getJSONObject(i);
                                String calendarGson = recode.getString("date");
                                Calendar date = gson.fromJson(calendarGson, Calendar.class);
                                int type = recode.getInt("type");
                                if (type == Schedule.TYPE_SCHEDULE){
                                    memo = Schedule.getInstanceFromMemo(date, recode.getString("title"), recode.getString("subtitle"));
                                }else {
                                    memo = Schedule.getInstanceFromTenant(date, recode.getString("location"), type);
                                }
                                memos.add(memo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        G.setMemos(memos);
                        handler.sendEmptyMessageDelayed(10, 1000);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(IntroActivity.this, "서버연결에 문제가 있습니다.", Toast.LENGTH_SHORT).show();
                        handler.sendEmptyMessageDelayed(10, 1000);
                    }
                });
                Volley.newRequestQueue(IntroActivity.this).add(request);


                //load rooms ... =======================================================================

            }else {
                String url = G.SERVER_URL+"loadMember.php";
                HashMap<String, String> members = new HashMap<>();
                JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String id, pw;
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject json = response.getJSONObject(i);
                                id = json.getString("id");
                                pw = json.getString("pw");
                                members.put(id, pw);
                                if (members.size() > 0) loginIntent.putExtra("members", members);
                                handler.sendEmptyMessageDelayed(10, 1000);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(IntroActivity.this, "서버문제 발생", Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue queue = Volley.newRequestQueue(IntroActivity.this);
                queue.add(request);
            }
        }//run()
    };

}
