package com.curtspec2019.homa;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.curtspec2019.homa.account.CurrentFragment;
import com.curtspec2019.homa.vo.Building;
import com.curtspec2019.homa.vo.MonthAccount;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.Nullable;

public class MonthChangeService extends Service {

    MonthAccount currentMonth;
    Building currentBuilding;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Gson gson = new Gson();
        SharedPreferences preferences = getSharedPreferences(CurrentFragment.PREFERENCE_NAME, MODE_PRIVATE);
        String data = preferences.getString("data", null);
        if (data != null) currentMonth = gson.fromJson(data, MonthAccount.class);
        else return START_NOT_STICKY;

        Boolean isRunning = preferences.getBoolean("isRunning", false);
        if (isRunning) return START_NOT_STICKY;

        String id = getSharedPreferences("login", MODE_PRIVATE).getString("id", null);
        if (id == null) return START_NOT_STICKY;

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = G.SERVER_URL + "loadInfo.php?id=" + G.getId();
        JsonArrayRequest infoRequest = new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray buildings = response.getJSONArray(0);

                    String name, profileUrl, address, tag, accountsJson;
                    int numOfFloor, type;
                    boolean isElevator = false, isUnderGround = false, isParking = false;

                    JSONObject recode;
                    int size = buildings.length();
                    for (int i = 0; i < size; i++){
                        recode = buildings.getJSONObject(i);
                        name = recode.getString("name");
                        profileUrl = recode.getString("profile");
                        address = recode.getString("address");
                        numOfFloor = recode.getInt("floor");
                        tag = recode.getString("tag");
                        isElevator = recode.getInt("elevator") == 1;
                        isParking = recode.getInt("parking") == 1;
                        isUnderGround = recode.getInt("underground") == 1;
                        accountsJson = recode.getString("accounts");
                        type = recode.getInt("type");

                        ArrayList<MonthAccount> accounts = gson.fromJson(accountsJson, new TypeToken<ArrayList<MonthAccount>>(){}.getType());

                        Building b = new Building(profileUrl, name, address, numOfFloor, isElevator, isParking, isUnderGround);
                        b.setTag(tag);
                        b.setAccounts(accounts);

                        if (type == 1) currentBuilding = b;
                    }

                    String thisMonth = new SimpleDateFormat("yyyy.MM").format(new Date());
                    if (!currentMonth.getWhen().equals(thisMonth)){
                          currentBuilding.getAccounts().add(currentMonth);
                          currentMonth = new MonthAccount(thisMonth, currentBuilding.getTotalRent(), new ArrayList<>(), new ArrayList<>());
                          preferences.edit().putString("data", gson.toJson(currentMonth)).apply();

                          String updateUrl = G.SERVER_URL + "updateBuilding.php";
                          SimpleMultiPartRequest updateRequest = new SimpleMultiPartRequest(Request.Method.POST, updateUrl, new Response.Listener<String>() {
                              @Override
                              public void onResponse(String response) {                              }
                          }, new Response.ErrorListener() {
                              @Override
                              public void onErrorResponse(VolleyError error) {                              }
                          });
                          updateRequest.addStringParam("id", id);
                          updateRequest.addStringParam("building", gson.toJson(currentBuilding));
                          queue.add(updateRequest);
                    }

                } catch (JSONException e) { e.printStackTrace();  }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {   }
        });
        queue.add(infoRequest);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
