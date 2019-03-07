package com.curtspec2018.homa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.curtspec2018.homa.databinding.ActivityLoginBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding b;
    Animation dragInLeft, dragOutLeft, dragInRight, dragOutRight;

    HashMap<String, String> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        b = DataBindingUtil.setContentView(this, R.layout.activity_login);
        dragInLeft = AnimationUtils.loadAnimation(this, R.anim.drag_in_left);
        dragOutLeft = AnimationUtils.loadAnimation(this, R.anim.drag_out_left);
        dragOutRight = AnimationUtils.loadAnimation(this, R.anim.drag_out_right);
        dragInRight = AnimationUtils.loadAnimation(this, R.anim.drag_in_right);

        members = (HashMap<String, String>) getIntent().getSerializableExtra("members");
    }

    public void clickLogIn(View view) {
        String id = b.editLoginId.getText().toString();
        String pw = b.editLogInPw.getText().toString();

        if (id.equals("") || pw.equals("")) return;
        else if (id.length() < 6 || pw.length() < 6){
            Toast.makeText(this, "정확한 아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        if (members == null) {
            String url = G.SERVER_URL + "loadMember.php";
            JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    String idRecived, pwRecived;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject json = response.getJSONObject(i);
                            idRecived = json.getString("id");
                            pwRecived = json.getString("pw");
                            if (idRecived.equals(id) && pwRecived.equals(pw)) {
                                //로그인 성공
                                boolean isAuto = b.cbAuto.isChecked();
                                Log.i("ErrorTrace - login", isAuto+"");
                                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                                if (isAuto) {
                                    preferences.edit().putBoolean("auto", true);
                                }
                                preferences.edit().putString("id", id).apply();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "서버문제 발생. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            if (members.containsKey(id)){
                if (pw.equals(members.get(id))){
                    boolean isAuto = b.cbAuto.isChecked();
                    SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    if (isAuto) {
                        editor.putBoolean("auto", true).apply();
                    }
                    editor.putString("id", id).apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }else
                Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickJoin(View view) {
        dragInLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                b.viewJoin.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animation) {            }
            @Override
            public void onAnimationRepeat(Animation animation) {            }
        });
        dragOutLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {            }
            @Override
            public void onAnimationEnd(Animation animation) {
                b.viewLogIn.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {            }
        });
        b.viewLogIn.startAnimation(dragOutLeft);
        b.viewJoin.startAnimation(dragInLeft);
    }

    public void clickOK(View view) {
        boolean isRight = true;
        String id = b.editId.getText().toString();
        String pw = b.editPw.getText().toString();
        String pwCheck = b.editPwCheck.getText().toString();
        if (!pw.equals(pwCheck)) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        char tmp;
        for (int i = 0; i<id.length(); i++){
            tmp = id.charAt(i);
            if (!((tmp >= 'A' && tmp <= 'Z') || (tmp >= 'a' && tmp <= 'z') || (tmp >= '0' && tmp <= '9'))) {
                isRight = false;
                break;
            }
        }
        for (int i = 0; i<pw.length(); i++){
            tmp = pw.charAt(i);
            if (!((tmp >= 'A' && tmp <= 'Z') || (tmp >= 'a' && tmp <= 'z') || (tmp >= '0' && tmp <= '9'))) {
                isRight = false;
                break;
            }
        }

        if (id.length() == 0 || pw.length() == 0 || pwCheck.length() == 0){
            Toast.makeText(this, "정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }else if (id.length() < 6 || pw.length() < 6 || !isRight){
            Toast.makeText(this, "계정 생성규칙에 어긋납니다", Toast.LENGTH_SHORT).show();
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("계정 등록중입니다.");
        progressDialog.show();

        String url = G.SERVER_URL + "registerMember.php";
        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")){
                    Toast.makeText(LoginActivity.this, "화원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    clickCancel(view);
                    members.put(id, pw);
                }else Toast.makeText(LoginActivity.this, "회원가입에 문제가 발생했습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "회원가입에 문제가 발생했습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
        });
        request.addStringParam("id", id);
        request.addStringParam("pw", pw);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    public void clickCancel(View view) {
        dragOutRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {            }
            @Override
            public void onAnimationEnd(Animation animation) {
                b.viewJoin.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {            }
        });
        dragInRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                b.viewLogIn.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animation) {            }
            @Override
            public void onAnimationRepeat(Animation animation) {            }
        });
        b.viewJoin.startAnimation(dragOutRight);
        b.viewLogIn.startAnimation(dragInRight);
        b.editId.setText("");
        b.editPw.setText("");
        b.editPwCheck.setText("");
    }

    public void clickRule(View view) {
        if (b.viewRule.getVisibility() == View.VISIBLE) return;
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        zoomIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                b.viewRule.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                handler.sendEmptyMessageDelayed(10, 1200);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {            }
        });
        zoomOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {            }
            @Override
            public void onAnimationEnd(Animation animation) {
                b.viewRule.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {            }
        });
        b.viewRule.startAnimation(zoomIn);
    }

    Animation zoomOut;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            b.viewRule.startAnimation(zoomOut);
        }
    };
}
