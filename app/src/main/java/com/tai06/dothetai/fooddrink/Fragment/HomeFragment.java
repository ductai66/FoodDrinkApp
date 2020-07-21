package com.tai06.dothetai.fooddrink.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.tai06.dothetai.fooddrink.Activity.InfoProduct.InfoFoodActivity;
import com.tai06.dothetai.fooddrink.Activity.InfoProduct.ThongtinActivity;
import com.tai06.dothetai.fooddrink.Activity.ViewPlusActivity;
import com.tai06.dothetai.fooddrink.Adapter.Oop_Adapter.ComboSearchAdapter;
import com.tai06.dothetai.fooddrink.Adapter.Oop_Adapter.HomeAdapter;
import com.tai06.dothetai.fooddrink.Adapter.Oop_Adapter.HomeAdapter1;
import com.tai06.dothetai.fooddrink.Adapter.Slide_Adapter.SlideHomeAdapter;
import com.tai06.dothetai.fooddrink.Contacts.HomeLink;
import com.tai06.dothetai.fooddrink.Contacts.ImageLink;
import com.tai06.dothetai.fooddrink.Object.OOP.Home;
import com.tai06.dothetai.fooddrink.Object.OOP.Person;
import com.tai06.dothetai.fooddrink.Object.Slide.SlideHome;
import com.tai06.dothetai.fooddrink.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    public static final int MSG_RECYL_Food = 1;
    public static final int MSG_RECYL_Drink = 2;
    public static final int MSG_RECYL_Combo = 3;

    private Home home;
    private ViewPager slide_page;
    private TabLayout tab_slide;
    private HomeAdapter homeAdapter;
    private HomeAdapter1 homeAdapter1;
    private ComboSearchAdapter comboSearchAdapter;
    private List<Home> arrHome;
    private List<SlideHome> arrSlideHome;
    private Handler handler;
    private RecyclerView recycle_food,recycle_drink,recycle_combo;
    private View view;
    private TextView more_burger,more_toco,more_combo;
    private ProgressDialog progressDialog;
    private Person person;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        recycle_food = view.findViewById(R.id.recycle_food);
        recycle_drink = view.findViewById(R.id.recycle_drink);
        recycle_combo = view.findViewById(R.id.recycle_combo);
        slide_page = view.findViewById(R.id.slide_page);
        tab_slide = view.findViewById(R.id.tab_slide);

        more_burger = view.findViewById(R.id.more_burger);
        more_toco = view.findViewById(R.id.more_toco);
        more_combo = view.findViewById(R.id.more_combo);

        Auto_Slide();
        initHandler();
        event_TextView(more_burger,1,"Danh sách Food");
        event_TextView(more_toco,2,"Danh sách Drink");
        event_TextView(more_combo,3,"Danh sách Combo");
        progress_dialog();
        getProductFood(HomeLink.URL_Food,MSG_RECYL_Food);
        getProductFood(HomeLink.URL_Drink,MSG_RECYL_Drink);
        getProductFood(HomeLink.URL_Combo,MSG_RECYL_Combo);
        return view;
    }

    private void Auto_Slide(){
        arrSlideHome = new ArrayList<>();
        arrSlideHome.add(new SlideHome(ImageLink.URL_Slide1));
        arrSlideHome.add(new SlideHome(ImageLink.URL_Slide2));
        arrSlideHome.add(new SlideHome(ImageLink.URL_Slide3));

        SlideHomeAdapter slideHomeAdapter = new SlideHomeAdapter(HomeFragment.this,arrSlideHome);
        slide_page.setAdapter(slideHomeAdapter);
        tab_slide.setupWithViewPager(slide_page,true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new HomeFragment.RunTimeSlide(),3000,6000);
    }

    public class RunTimeSlide extends TimerTask {
        @Override
        public void run() {
            if (getActivity() != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (slide_page.getCurrentItem() < arrSlideHome.size() - 1) {
                            slide_page.setCurrentItem(slide_page.getCurrentItem() + 1);
                        } else {
                            slide_page.setCurrentItem(0);
                        }
                    }
                });
            }
        }
    }

    private void event_TextView(TextView textView, final int id_lh, final String title){
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewPlusActivity.class);
                intent.putExtra("id_lh",id_lh);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });
    }

    private void progress_dialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    private void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MSG_RECYL_Food:
                        arrHome = new ArrayList<>();
                        arrHome.addAll((Collection<? extends Home>) msg.obj);
                        setadapterFood(arrHome);
                        break;
                    case MSG_RECYL_Drink:
                        arrHome = new ArrayList<>();
                        arrHome.addAll((Collection<? extends Home>) msg.obj);
                        setadapterDrink(arrHome);
                        break;
                    case MSG_RECYL_Combo:
                        arrHome = new ArrayList<>();
                        arrHome.addAll((Collection<? extends Home>) msg.obj);
                        setadapterCombo(arrHome);
                        break;
                }
            }
        };
    }

    private void setadapterFood(List<Home> list){
        homeAdapter = new HomeAdapter(list,HomeFragment.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1, GridLayoutManager.HORIZONTAL,false);
        recycle_food.setLayoutManager(gridLayoutManager);
        recycle_food.setAdapter(homeAdapter);
        recycle_food.setHasFixedSize(true);
        homeAdapter.notifyDataSetChanged();
    }

    private void setadapterDrink(List<Home> list){
        homeAdapter1 = new HomeAdapter1(list,HomeFragment.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2, GridLayoutManager.HORIZONTAL,false);
        recycle_drink.setLayoutManager(gridLayoutManager);
        recycle_drink.setAdapter(homeAdapter1);
        recycle_drink.setHasFixedSize(true);
        homeAdapter1.notifyDataSetChanged();
    }

    private void setadapterCombo(List<Home> list){
        comboSearchAdapter = new ComboSearchAdapter(list,HomeFragment.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1, GridLayoutManager.VERTICAL,false);
        recycle_combo.setLayoutManager(gridLayoutManager);
        recycle_combo.setAdapter(comboSearchAdapter);
        recycle_combo.setHasFixedSize(true);
        comboSearchAdapter.notifyDataSetChanged();
    }

    private void getProductFood(final String link, final int a){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, link, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                List<Home> list = new ArrayList<>();
                                for (int i=0;i<response.length();i++){
                                    try {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        list.add(new Home(
                                                jsonObject.getInt("id_mh"),
                                                jsonObject.getInt("id_lh"),
                                                jsonObject.getString("ten_mh"),
                                                jsonObject.getInt("gia_mh"),
                                                jsonObject.getInt("sl_mh"),
                                                jsonObject.getString("image")
                                        ));
                                        Message msg = new Message();
                                        msg.what = a;
                                        msg.obj = list;
                                        handler.sendMessage(msg);
                                        progressDialog.dismiss();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley",error.toString());
                        progressDialog.dismiss();
                    }
                });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread.start();
    }

    public void showFood(Home home){
        Intent intent = new Intent(getActivity(), InfoFoodActivity.class);
        intent.putExtra("food",home);
        startActivity(intent);
    }
    public void showDrink(Home home){
        Intent intent = new Intent(getActivity(), ThongtinActivity.class);
        intent.putExtra("drink",home);
        startActivity(intent);
    }
    public void showCombo(Home home){
        Intent intent = new Intent(getActivity(), InfoFoodActivity.class);
        intent.putExtra("food",home);
        startActivity(intent);
    }
}
