package com.curtspec2019.homa;

import android.annotation.SuppressLint;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        isAuto = preferences.getBoolean("auto", false);
        if (isAuto) {
            String id = preferences.getString("id", null);
            G.setId(id);
            G.loadInfor(this, new Thread(){
                @Override
                public void run() {
                    handler.sendEmptyMessageDelayed(10, 1000);
                }
            }, null);
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
                    Log.i("errorTrace", error.getMessage());
                }
            });
            RequestQueue queue = Volley.newRequestQueue(IntroActivity.this);
            queue.add(request);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        iv.startAnimation(dropDown);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            iv.startAnimation(zoomOut);

        }
    };

}
