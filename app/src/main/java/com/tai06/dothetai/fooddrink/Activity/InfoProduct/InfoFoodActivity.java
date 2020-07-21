package com.tai06.dothetai.fooddrink.Activity.InfoProduct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.tai06.dothetai.fooddrink.Activity.HoaDonActivity;
import com.tai06.dothetai.fooddrink.Activity.MainActivity;
import com.tai06.dothetai.fooddrink.Contacts.HomeLink;
import com.tai06.dothetai.fooddrink.Object.OOP.Home;
import com.tai06.dothetai.fooddrink.Object.OOP.Thongtin;
import com.tai06.dothetai.fooddrink.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InfoFoodActivity extends AppCompatActivity {
    public static final int MSG_INFO_FOOD = 1;

    private Toolbar toolbar;
    private ImageView image_info,ic_remove,ic_add;
    private int slmh = 1, sum_money = 0;
    private int tien1 = 0,tien2 = 0,tien3 =0,tong = 0;
    private TextView name_product,detail_product,soluong,test;
    private Button tongtien;
    private Home home;
    private String text1 ="",text2="",text3="";
    public  String ghichu = "";
    private Thongtin thongtin;
    private Handler handler;
    private CheckBox checkBox1,checkBox2,checkBox3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_food);
        init();
        initHandler();
        getInfoFood(HomeLink.Info);
//        event_checkbox();
        event_money(home.getGia_mh());
        event_thanhtoan();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image_info = findViewById(R.id.image_info);
        name_product = findViewById(R.id.name_product);
        detail_product = findViewById(R.id.detail_product);
        soluong = findViewById(R.id.soluong);
        tongtien = findViewById(R.id.tongtien);
        checkBox1 = findViewById(R.id.checkbox1);
        checkBox2 = findViewById(R.id.checkbox2);
        checkBox3 = findViewById(R.id.checkbox3);
        ic_add = findViewById(R.id.ic_add);
        ic_remove = findViewById(R.id.ic_remove);
        home = (Home) getIntent().getSerializableExtra("food");
    }

    private void event_thanhtoan(){
        tongtien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), HoaDonActivity.class);
                intent.putExtra("ghichu",ghichu);
                intent.putExtra("soluong",soluong.getText().toString().trim());
                intent.putExtra("tongtien",tongtien.getText().toString().trim());
                intent.putExtra("info",thongtin);
                startActivity(intent);
            }
        });
    }

    private void event_money(final int gia_mh) {
        soluong.setText(String.valueOf(slmh));
        ic_remove.setEnabled(false);
        ic_remove.setImageResource(R.drawable.ic_remove_silver);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int sl = Integer.parseInt(soluong.getText().toString().trim());
                if (isChecked){ //mặc định ischecked = true
                    tien1 = 23000;
                    tong = tong + 23000;
                    sum_money = (sl * gia_mh) + tong;
                    tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                    text1 = text1 + "Miếng bò whopper nhỏ +23000VNĐ";
                    ghichu = text1+"\n"+text2+"\n"+text3;
                }else{
                    tien1 = 0;
                    tong = tong - 23000;
                    sum_money = (sl * gia_mh) + tong;
                    tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                    text1 = "";
                    ghichu = text1+text2+"\n"+text3;
                }
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int sl = Integer.parseInt(soluong.getText().toString().trim());
                if (isChecked){ //mặc định ischecked = true
                    tien2 = 15000;
                    tong = tong + 15000;
                    sum_money = (sl * gia_mh) + tong;
                    tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                    text2 = text2 + "Thịt xông khói +15000VNĐ";
                    ghichu = text1+"\n"+text2+"\n"+text3;
                }else{
                    tien2 = 0;
                    tong = tong - 15000;
                    sum_money = (sl * gia_mh) + tong;
                    tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                    text2 ="";
                    ghichu = text1+"\n"+text2+text3;
                }
            }
        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int sl = Integer.parseInt(soluong.getText().toString().trim());
                if (isChecked){ //mặc định ischecked = true
                    tien3 = 12000;
                    tong = tong + 12000;
                    sum_money = (sl * gia_mh) + tong;
                    tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                    text3 = text3 + "Phô mai mỹ +12000VNĐ";
                    ghichu = text1+"\n"+text2+"\n"+text3;
                }else{
                    tien3 = 0;
                    tong = tong - 12000;
                    sum_money = (sl * gia_mh) + tong;
                    tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                    text3 = "";
                    ghichu = text1+"\n"+text2+text3;
                }
            }
        });

        ic_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slmh == 1) {
                    ic_remove.setEnabled(false);
                    ic_remove.setImageResource(R.drawable.ic_remove_silver);
                } else {
                    slmh = slmh - 1;
                    soluong.setText(String.valueOf(slmh));
                    sum_money = (slmh * gia_mh) + tong;
                    tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                }
            }
        });
        ic_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slmh = slmh + 1;
                sum_money = (slmh * gia_mh) + tong;
                ic_remove.setEnabled(true);
                ic_remove.setImageResource(R.drawable.ic_remove);
                soluong.setText(String.valueOf(slmh));
//                sum_money = (slmh * gia_mh) + tong;
                tongtien.setText(String.valueOf(sum_money) + "VNĐ");
            }
        });
    }

    private void event_checkbox(){
        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = tongtien.getText().toString().trim();
                money = money.substring(0,money.length()-3);
                int number_money = Integer.parseInt(money);
                if (checkBox1.isChecked()){
                    text1 = "Miếng bò whopper nhỏ +23000VNĐ";
                    ghichu = text1+"\n"+text2+"\n"+text3;
//                    number_money = number_money +23000;
//                    tongtien.setText(String.valueOf(number_money)+"VNĐ");
                }
                else{
                    text1 ="";
//                    number_money = number_money - 23000;
//                    tongtien.setText(String.valueOf(number_money)+"VNĐ");
                    ghichu = text1+text2+"\n"+text3;
                }
            }
        });
        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox2.isChecked()){
                    text2 = text2 + "Thịt xông khói  +15000VNĐ";
                    ghichu = text1+"\n"+text2+"\n"+text3;
                }else{
                    text2 = "";
                    ghichu = text1+"\n"+text2+text3;
                }
            }
        });
        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox3.isChecked()){
                    text3 = text3 + "Phô mai mỹ +12000VNĐ";
                    ghichu = text1+"\n"+text2+"\n"+text3;
                }
                else{
                    text3 = "";
                    ghichu = text1+"\n"+text2+text3;
                }
            }
        });
    }

    private void initHandler() {
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MSG_INFO_FOOD:
                        thongtin = (Thongtin) msg.obj;
                        setAdapter(home,thongtin);
                        break;
                }
            }
        };
    }

    private void setAdapter(Home home,Thongtin thongtin){
        Picasso.get().load(home.getImage()).into(image_info);
        name_product.setText(home.getTen_mh());
        detail_product.setText(thongtin.getMota());
        tongtien.setText(String.valueOf(thongtin.getGia_mh())+"VNĐ");
    }

    private void getInfoFood(final String url){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(InfoFoodActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Thongtin thongtin = new Thongtin();
                            JSONObject jsonObject = new JSONObject(response);
                            thongtin.setId_mh(jsonObject.getInt("id_mh"));
                            thongtin.setId_lh(jsonObject.getInt("id_lh"));
                            thongtin.setTen_mh(jsonObject.getString("ten_mh"));
                            thongtin.setGia_mh(jsonObject.getInt("gia_mh"));
                            thongtin.setSl_mh(jsonObject.getInt("sl_mh"));
                            thongtin.setImage(jsonObject.getString("image"));
                            thongtin.setMota(jsonObject.getString("mota"));
                            Message msg = new Message();
                            msg.what = MSG_INFO_FOOD;
                            msg.obj = thongtin;
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InfoFoodActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "Lỗi!\n " + error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("id_mh",String.valueOf(home.getId_mh()));
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    private void Back_Arrow(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoFoodActivity.this, MainActivity.class);
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
