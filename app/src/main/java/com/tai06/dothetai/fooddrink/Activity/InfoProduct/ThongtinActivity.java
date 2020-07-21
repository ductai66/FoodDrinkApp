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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class ThongtinActivity extends AppCompatActivity {
    public static final int MSG_INFO = 1;

    private Toolbar toolbar;
    private Button tongtien;
    private Handler handler;
    private Home home;
    private int slmh = 1, sum_money = 0;
    private Thongtin thongtin;
    private TextView info_name, detailproduct, soluong;
    private ImageView ic_add, ic_remove, temp_row, size_row, sugar_row, cold_row, image_info;
    private RadioGroup temp_group, size_group, sugar_group, cold_group;
    private RadioButton radioButtontemp,radioButtonsize,radioButtonsugar,radioButtoncold,sizeL,sizeM;

    private String str_temp,str_size,str_sugar,str_cold;
    private int tien1=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtin);
        init();
        event_expandable_row(temp_row, temp_group);
        event_expandable_row(size_row, size_group);
        event_expandable_row(sugar_row, sugar_group);
        event_expandable_row(cold_row, cold_group);
        initHandler();
        getInfo(HomeLink.Info);
        event_money(home.getGia_mh());
        event_radio();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        temp_group = findViewById(R.id.temp_group);
        size_group = findViewById(R.id.size_group);
        sugar_group = findViewById(R.id.sugar_group);
        cold_group = findViewById(R.id.cold_group);

        sizeM = findViewById(R.id.sizeM);
        sizeL = findViewById(R.id.sizeL);

        tongtien = findViewById(R.id.tongtien);
        info_name = findViewById(R.id.info_name);
        detailproduct = findViewById(R.id.detailproduct);
        ic_add = findViewById(R.id.ic_add);
        ic_remove = findViewById(R.id.ic_remove);

        temp_row = findViewById(R.id.temp_row);
        size_row = findViewById(R.id.size_row);
        sugar_row = findViewById(R.id.sugar_row);
        cold_row = findViewById(R.id.cold_row);

        image_info = findViewById(R.id.image_info);
        soluong = findViewById(R.id.soluong);
        home = (Home) getIntent().getSerializableExtra("drink");
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MSG_INFO:
                        thongtin = (Thongtin) msg.obj;
                        setAdapter(home, thongtin);
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    private void setAdapter(Home home, Thongtin thongtin) {
        Picasso.get().load(home.getImage()).into(image_info);
        info_name.setText(home.getTen_mh());
        detailproduct.setText(thongtin.getMota());
        tongtien.setText(String.valueOf(home.getGia_mh()) + "VNĐ");
    }

    private void event_money(final int gia_mh) {
        soluong.setText(String.valueOf(slmh));
        ic_remove.setEnabled(false);
        ic_remove.setImageResource(R.drawable.ic_remove_silver);
        size_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int sl = Integer.parseInt(soluong.getText().toString().trim());
                switch (checkedId){
                    case R.id.sizeM:
                        tien1 = 0;
                        sum_money = sl * gia_mh;
                        tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                        break;
                    case R.id.sizeL:
                        tien1 = 6000;
                        sum_money = sl * (gia_mh + tien1);
                        tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                        break;
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
                    sum_money = slmh * (gia_mh + tien1);
                    tongtien.setText(String.valueOf(sum_money) + "VNĐ");
                }
            }
        });
        ic_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slmh = slmh + 1;
                sum_money = slmh * (gia_mh + tien1);
                ic_remove.setEnabled(true);
                ic_remove.setImageResource(R.drawable.ic_remove);
                soluong.setText(String.valueOf(slmh));
//                sum_money = slmh * (gia_mh + tien1);
                tongtien.setText(String.valueOf(sum_money) + "VNĐ");
            }
        });
    }

    private void event_expandable_row(final ImageView imageView, final RadioGroup radioGroup) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_expand_less).getConstantState())) {
                    radioGroup.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.ic_expand_more);
                } else {
                    radioGroup.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.ic_expand_less);
                }
            }
        });
    }

    private void getInfo(final String url) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(ThongtinActivity.this);
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
                            msg.what = MSG_INFO;
                            msg.obj = thongtin;
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThongtinActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "Lỗi!\n " + error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("id_mh", String.valueOf(home.getId_mh()));
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    private void event_radio() {
        tongtien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtontemp = (RadioButton) findViewById(temp_group.getCheckedRadioButtonId());
                String a = radioButtontemp.getText().toString();
                radioButtonsize = (RadioButton) findViewById(size_group.getCheckedRadioButtonId());
                String b = radioButtonsize.getText().toString();
                radioButtonsugar = (RadioButton) findViewById(sugar_group.getCheckedRadioButtonId());
                String c = radioButtonsugar.getText().toString();
                radioButtoncold = (RadioButton) findViewById(cold_group.getCheckedRadioButtonId());
                String d = radioButtoncold.getText().toString();
                String ghichu = "Nhiệt độ: "+a+"\nKích thước: "+b+"\nLượng đường: "+c+"\nLượng đá: "+d;

                Intent intent = new Intent(getApplication(), HoaDonActivity.class);
                intent.putExtra("ghichu", ghichu);
                intent.putExtra("soluong", soluong.getText().toString().trim());
                intent.putExtra("tongtien",tongtien.getText().toString().trim());
                intent.putExtra("info", thongtin);
                startActivity(intent);
            }
        });
    }

    private void Back_Arrow(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThongtinActivity.this, MainActivity.class);
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
