package com.curtspec2018.homa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.curtspec2018.homa.vo.Account;
import com.curtspec2018.homa.vo.Building;
import com.curtspec2018.homa.vo.Floor;
import com.curtspec2018.homa.vo.MonthAccount;
import com.curtspec2018.homa.vo.Room;
import com.curtspec2018.homa.vo.Schedule;
import com.curtspec2018.homa.vo.Tenant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.Nullable;

public class G {

    private static String id;
    public static final String SERVER_URL = "http://curtspec2019.dothome.co.kr/homa/";
    private static Building currentBuilding;
    private static ArrayList<Building> buildings = new ArrayList<>();
    private static ArrayList<Schedule> memos = new ArrayList<>();


    public static ArrayList<Schedule> getMemos(){
        return memos;
    }

    public static ArrayList<Schedule> getAddableSchedules(){
        ArrayList<Schedule> addable = new ArrayList<>();
        addable.addAll(memos);
        if (currentBuilding != null) addable.addAll(currentBuilding.getSchedulesFromTenant());
        for (Schedule s : addable){
            Log.i("ErrorTrace", new SimpleDateFormat("yyyy.MM.dd").format(new Date(s.getDate().getTimeInMillis())) + "\n");
        }
        return addable;
    }

    public static ArrayList<Building> getBuildings(){
        return buildings;
    }

    public static void setCurrentBuilding(Building building){
        currentBuilding = building;
    }

    public static Building getCurrentBuilding(){
        return currentBuilding;
    }

    public static void setBuildings(ArrayList<Building> buildings) {
        G.buildings = buildings;
    }

    public static void setMemos(ArrayList<Schedule> memos) {
        G.memos = memos;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        G.id = id;
    }

    //==================================== method ==============================================
    public static String divisionThousand(long value){
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(value);
    }

    public static float dpToPx(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static void loadInfor(Context context, @Nullable Thread taskAfterSuccess, @Nullable Thread taskAfterError){

        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("회원정보 로딩중...");
        dialog.show();

        //============================================  Load Memos  ===================================================

        String url = G.SERVER_URL + "loadMemos.php?id=" + G.getId();
        Gson gson = new Gson();
        ArrayList<Schedule> memos = new ArrayList<>();
        JsonArrayRequest memoRequest = new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {
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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "회원정보를 가져오는데 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });

        // =========================================== load Info ======================================

        url = G.SERVER_URL + "loadInfo.php?id=" + G.getId();
        JsonArrayRequest infoRequest = new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray buildings = response.getJSONArray(0);
                    JSONArray rooms = response.getJSONArray(1);

                    String name, profileUrl, address, tag, accountsJson;
                    int numOfFloor, type;
                    boolean isElevator = false, isUnderGround = false, isParking = false;

                    //======================================== buildings ========================================

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

                        if (type == 1) G.setCurrentBuilding(b);
                        else G.getBuildings().add(b);
                    }

                    //======================================== rooms ========================================

                    String nickname, belong, tenantJson = null;
                    int floor;
                    boolean isOccupied;

                    HashMap<String, ArrayList<Room>> divider = new HashMap<>();
                    ArrayList<String> keys = new ArrayList<>();

                    size = rooms.length();
                    for (int i = 0; i < size; i++){
                        recode = rooms.getJSONObject(i);
                        name = recode.getString("name");
                        nickname = recode.getString("nickname");
                        floor = recode.getInt("floor");
                        isUnderGround = recode.getInt("underground") == 1;
                        isOccupied = recode.getInt("occupied") == 1;
                        if (isOccupied){
                            tenantJson = recode.getString("tenant");
                        }
                        tag = recode.getString("tag");
                        belong = recode.getString("belong");

                        Room r = new Room(name, nickname, floor, isOccupied, isUnderGround);
                        if (isOccupied && tenantJson != null){
                            Tenant t = gson.fromJson(tenantJson, Tenant.class);
                            String target = t.getTenantName();
                            if (target != null) {
                                String[] unicodeStr = target.split("u");
                                String goal = "";
                                for (int k = 1; k < unicodeStr.length; k++) {
                                    int hexCode = Integer.parseInt(unicodeStr[k], 16);
                                    goal += (char)hexCode;
                                }
                                t.setTenantName(goal);
                            }
                            r.setTenants(t);
                        }
                        if (tag != null) r.setTag(tag);

                        if (divider.containsKey(belong)){
                            divider.get(belong).add(r);
                        }else {
                            ArrayList<Room> rList = new ArrayList<>();
                            rList.add(r);
                            divider.put(belong, rList);
                            keys.add(belong);
                        }
                    }//room 객체 생성

                    for (String key : keys){
                        ArrayList<Floor> floors = new ArrayList<>();
                        for (Room r : divider.get(key)){
                            int index = -1;
                            for (int i = 0; i < floors.size(); i++){
                                if (floors.get(i).getFloor() == r.getFloor()){
                                    index = i;
                                    break;
                                }
                            }
                            if (index >= 0){
                                floors.get(index).getRooms().add(r);
                            }else {
                                ArrayList<Room> rList = new ArrayList<>();
                                rList.add(r);
                                floors.add(new Floor(r.getFloor(), rList));
                            }
                        }

                        if (G.getCurrentBuilding().getTag().equals(key)) {
                            G.getCurrentBuilding().setFloors(floors);
                            continue;
                        }
                        for (Building tmp : G.getBuildings()){
                            if (tmp.getTag().equals(key)) {
                                tmp.setFloors(floors);
                                break;
                            }
                        }
                    }// room 객체 분류작업

                    dialog.dismiss();
                    if (taskAfterSuccess != null) taskAfterSuccess.start();

                } catch (JSONException e) { e.printStackTrace();  }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "회원정보를 가져오는데 실패했습니다", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                if (taskAfterError != null) taskAfterError.start();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(memoRequest);
        queue.add(infoRequest);
    }
}
