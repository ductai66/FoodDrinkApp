package com.tai06.dothetai.fooddrink.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.tai06.dothetai.fooddrink.Activity.InfoProduct.InfoFoodActivity;
import com.tai06.dothetai.fooddrink.Activity.InfoProduct.ThongtinActivity;
import com.tai06.dothetai.fooddrink.Adapter.Oop_Adapter.SearchAdapter;
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

public class SearchViewActivity extends AppCompatActivity {
    public static final int MSG_SEARCH = 1;

    private Home home;
    private Toolbar toolbar;
    private SearchAdapter searchAdapter;
    private List<Home> arrHome;
    private RecyclerView recycle_search;
    private Handler handler;
    private TextInputEditText key_search;
    private ImageButton close;
    private TextView empty_search;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        init();
        initHandler();
        ProcessEvent();
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Back_Arrow();

        key_search = findViewById(R.id.key_search);
        close = findViewById(R.id.close);
        recycle_search = findViewById(R.id.recycle_search);
        empty_search = findViewById(R.id.empty_search);
    }

    private void progress_dialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void ProcessEvent(){
        close.setVisibility(View.GONE);
        key_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    close.setVisibility(View.VISIBLE);
                }else {
                    close.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key_search.getText().clear();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.search_bar:
                String values = key_search.getText().toString().trim();
                if (values.equals("")){
                    Toast.makeText(this, "Bạn chưa nhập từ khóa", Toast.LENGTH_SHORT).show();
                }else{
                    progress_dialog();
                    check_searchview();
                    closeKeyboard();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view!=null){
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MSG_SEARCH:
                        arrHome = new ArrayList<>();
                        arrHome.addAll((Collection<? extends Home>) msg.obj);
                        setSearchAdapter(arrHome);
                        break;
                }
            }
        };
    }


    private void setSearchAdapter(List<Home> list){
        searchAdapter = new SearchAdapter(list,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1, GridLayoutManager.VERTICAL,false);
        recycle_search.setLayoutManager(gridLayoutManager);
        recycle_search.setAdapter(searchAdapter);
        recycle_search.setHasFixedSize(true);
        searchAdapter.notifyDataSetChanged();
    }

    private void PostSearch(final String link){
        final String ten_mh = key_search.getText().toString().trim();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(SearchViewActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST,link,
                        new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Home> list = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++){
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
                                msg.what = MSG_SEARCH;
                                msg.obj = list;
                                handler.sendMessage(msg);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchViewActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("AAA","Error"+error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> param = new HashMap<>();
                        param.put("ten_mh",ten_mh);
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    private void check_searchview(){
        final String ten_mh = key_search.getText().toString().trim();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(SearchViewActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, HomeLink.URL_CHECK_SEARCHVIEW, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")){
                            progressDialog.dismiss();
                            empty_search.setVisibility(View.VISIBLE);
                            recycle_search.setVisibility(View.GONE);
                            Toast.makeText(SearchViewActivity.this, "Không tìm thấy kết quả!", Toast.LENGTH_SHORT).show();
                        }else{
                            empty_search.setVisibility(View.GONE);
                            recycle_search.setVisibility(View.VISIBLE);
                            PostSearch(HomeLink.URL_Search);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchViewActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("AAA","Error"+error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("ten_mh",ten_mh);
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
            Intent intent = new Intent(SearchViewActivity.this, InfoFoodActivity.class);
            intent.putExtra("food",home);
            startActivity(intent);
        }else if (home.getId_lh() == 2){
            Intent intent = new Intent(SearchViewActivity.this, ThongtinActivity.class);
            intent.putExtra("drink",home);
            startActivity(intent);
        }else if (home.getId_lh() == 3){
            Intent intent = new Intent(SearchViewActivity.this, ThongtinActivity.class);
            intent.putExtra("combo",home);
            startActivity(intent);
        }
    }

    private void Back_Arrow(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchViewActivity.this,MainActivity.class);
                intent.putExtra("email",HomeLink.Email);
                startActivity(intent);
            }
        });
    }
}
