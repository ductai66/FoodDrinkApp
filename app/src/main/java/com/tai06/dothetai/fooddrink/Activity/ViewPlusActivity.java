package com.tai06.dothetai.fooddrink.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tai06.dothetai.fooddrink.Activity.InfoProduct.InfoFoodActivity;
import com.tai06.dothetai.fooddrink.Activity.InfoProduct.ThongtinActivity;
import com.tai06.dothetai.fooddrink.Adapter.Oop_Adapter.ViewPlusAdapter;
import com.tai06.dothetai.fooddrink.Contacts.HomeLink;
import com.tai06.dothetai.fooddrink.Object.OOP.Home;
import com.tai06.dothetai.fooddrink.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPlusActivity extends AppCompatActivity {
    public static final int MSG_ViewPlus = 1;

    private Toolbar toolbar_viewplus;
    private RecyclerView recycle_viewplus;
    private Handler handler;
    private ViewPlusAdapter viewPlusAdapter;
    private List<Home> arrHome;
    private TextView title_viewplus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plus);
        init();
        initHandler();
        get_ViewPlus();
    }

    private void init(){
        toolbar_viewplus = findViewById(R.id.toolbar_viewplus);
        setSupportActionBar(toolbar_viewplus);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycle_viewplus = findViewById(R.id.recycle_viewplus);
        title_viewplus = findViewById(R.id.title_viewplus);
    }

    private void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MSG_ViewPlus:
                        arrHome = new ArrayList<>();
                        arrHome.addAll((Collection<? extends Home>) msg.obj);
                        setAdapterViewplus(arrHome);
                        break;
                }
            }
        };
    }

    private void setAdapterViewplus(List<Home> list){
        viewPlusAdapter = new ViewPlusAdapter(list,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false );
        recycle_viewplus.setLayoutManager(gridLayoutManager);
        recycle_viewplus.setAdapter(viewPlusAdapter);
        recycle_viewplus.setHasFixedSize(true);
        viewPlusAdapter.notifyDataSetChanged();
    }

    private void get_ViewPlus(){
        Intent intent = getIntent();
        final int id_lh = intent.getIntExtra("id_lh",0);
        String title = intent.getStringExtra("title");
        title_viewplus.setText(title);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(ViewPlusActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, HomeLink.URL_GET_VIEWPLUS, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Home> list = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                list.add(new Home(
                                        jsonObject.getInt("id_mh"),
                                        jsonObject.getInt("id_lh"),
                                        jsonObject.getString("ten_mh"),
                                        jsonObject.getInt("gia_mh"),
                                        jsonObject.getInt("sl_mh"),
                                        jsonObject.getString("image")
                                ));
                                Message msg = new Message();
                                msg.what = MSG_ViewPlus;
                                msg.obj = list;
                                handler.sendMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewPlusActivity.this, "Xảy ra lỗi!", Toast.LENGTH_SHORT).show();
                        Log.d("AAA","Error"+error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("id_lh",String.valueOf(id_lh));
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    public void showProduct(Home home){
        if (home.getId_lh() == 1){
            Intent intent = new Intent(ViewPlusActivity.this, InfoFoodActivity.class);
            intent.putExtra("food",home);
            startActivity(intent);
        }else if (home.getId_lh() == 2){
            Intent intent = new Intent(ViewPlusActivity.this, ThongtinActivity.class);
            intent.putExtra("drink",home);
            startActivity(intent);
        }else if (home.getId_lh() == 3){
            Intent intent = new Intent(ViewPlusActivity.this, InfoFoodActivity.class);
            intent.putExtra("food",home);
            startActivity(intent);
        }
    }

    private void Back_Arrow(){
        toolbar_viewplus.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPlusActivity.this,MainActivity.class);
                intent.putExtra("email",HomeLink.Email);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
