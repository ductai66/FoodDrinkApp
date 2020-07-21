package com.tai06.dothetai.fooddrink.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.tai06.dothetai.fooddrink.Contacts.Address;
import com.tai06.dothetai.fooddrink.Contacts.HomeLink;
import com.tai06.dothetai.fooddrink.Fragment.DonHangFragment;
import com.tai06.dothetai.fooddrink.Object.OOP.Phuong;
import com.tai06.dothetai.fooddrink.Object.OOP.Quan;
import com.tai06.dothetai.fooddrink.Object.OOP.Thongtin;
import com.tai06.dothetai.fooddrink.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;

public class HoaDonActivity extends AppCompatActivity {
    public static final int MSG_QUAN = 1;
    public static final int MSG_PHUONG = 2;

    private TextView tongtien_hd,soluong_hd,ghichu_hd,gia_sphd,name_sphd,text_address;
    private ImageView image_sphd;
    private TextInputEditText name_hd,diachi_hd,sdt_hd;
    private TextInputLayout txt_inputlayout_sdt;
    private Thongtin thongtin;
    private Button thanhtoan;
    private int tongtien=0;
    private Toolbar toolbar_hoadon;
    private Handler handler;
    private List<Quan> quanList;
    private List<Phuong> phuongList;
    private Spinner sp_parent,sp_child;
    private String address,txt_quan,txt_phuong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don);
        init();
        initHandler();
        getAllQuan();
//        getAllPhuong(Address.id_hoankiem,MSG_PHUONG_TEST);
        get_intent();
        event_button();
    }

    private void init() {
        toolbar_hoadon = findViewById(R.id.toolbar_hoadon);
        setSupportActionBar(toolbar_hoadon);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        soluong_hd = findViewById(R.id.soluong_hd);
        tongtien_hd = findViewById(R.id.tongtien_hd);
        ghichu_hd = findViewById(R.id.ghichu_hd);
        name_hd = findViewById(R.id.name_hd);
        diachi_hd = findViewById(R.id.diachi_hd);
        sdt_hd = findViewById(R.id.sdt_hd);
        txt_inputlayout_sdt = findViewById(R.id.txt_inputlayout_sdt);

        name_hd.setText(HomeLink.Name);
        sdt_hd.setText(HomeLink.Sdt);

        image_sphd = findViewById(R.id.image_sphd);
        gia_sphd = findViewById(R.id.gia_sphd);
        name_sphd = findViewById(R.id.name_sphd);
        thanhtoan = findViewById(R.id.thanhtoan);

        sp_parent = findViewById(R.id.sp_parent);
        sp_child = findViewById(R.id.sp_child);
        text_address = findViewById(R.id.text_address);

        thongtin = (Thongtin) getIntent().getSerializableExtra("info");
    }

    private boolean check_phone(){
        String phone = sdt_hd.getText().toString().trim();
        Matcher matcher = Address.PATTERN_PHONE.matcher(phone);
        if (!matcher.matches()){
            txt_inputlayout_sdt.setErrorEnabled(true);
            txt_inputlayout_sdt.setError("Kiểm tra lại số điện thoại");
            return false;
        }else{
            txt_inputlayout_sdt.setErrorEnabled(false);
            return true;
        }
    }

    private void initHandler(){
        handler = new Handler(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MSG_QUAN:
                        quanList = new ArrayList<>();
                        quanList.addAll((Collection<? extends Quan>) msg.obj);
                        final List<String> listTen_Quan = new ArrayList<>();
                        quanList.stream().forEach(new Consumer<Quan>() {
                            @Override
                            public void accept(Quan quan) {
                                listTen_Quan.add(quan.getTen_quan());
                            }
                        });
                        AddressSpinner(listTen_Quan);
                        break;
                    case MSG_PHUONG:
                        phuongList = new ArrayList<>();
                        phuongList.addAll((Collection<? extends Phuong>) msg.obj);
                        final List<String> listTen_Phuong = new ArrayList<>();
//                        phuongList.stream().forEach(new Consumer<Phuong>() {
//                            @Override
//                            public void accept(Phuong phuong) {
//                                listTen_Phuong.add(phuong.getTen_phuong());
//                            }
//                        });
                        phuongList.stream().forEach( phuong -> {
                            listTen_Phuong.add(phuong.getTen_phuong());
                        });
                        PhuongSpinner(listTen_Phuong);
                        break;
                }
            }
        };
    }

    private void AddressSpinner(final List<String> list){
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_parent.setAdapter(arrayAdapter);
        sp_parent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txt_quan = sp_parent.getSelectedItem().toString();
                switch (position){
                    case 0: {
                        getAllPhuong(Address.id_hoankiem,MSG_PHUONG);
                        break;
                    }
                    case 1: {
                        getAllPhuong(Address.id_dongda,MSG_PHUONG);
                        break;
                    }
                    case 2: {
                        getAllPhuong(Address.id_badinh,MSG_PHUONG);
                        break;
                    }
                    case 3: {
                        getAllPhuong(Address.id_haibatrung,MSG_PHUONG);
                        break;
                    }
                    case 4: {
                        getAllPhuong(Address.id_hoangmai,MSG_PHUONG);
                        break;
                    }
                    case 5: {
                        getAllPhuong(Address.id_thanhxuan,MSG_PHUONG);
                        break;
                    }
                    case 6: {
                        getAllPhuong(Address.id_longbien,MSG_PHUONG);
                        break;
                    }
                    case 7: {
                        getAllPhuong(Address.id_namtuliem,MSG_PHUONG);
                        break;
                    }
                    case 8: {
                        getAllPhuong(Address.id_bactuliem,MSG_PHUONG);
                        break;
                    }
                    case 9: {
                        getAllPhuong(Address.id_tayho,MSG_PHUONG);
                        break;
                    }
                    case 10: {
                        getAllPhuong(Address.id_caugiay,MSG_PHUONG);
                        break;
                    }
                    case 11:{
                        getAllPhuong(Address.id_hadong,MSG_PHUONG);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void PhuongSpinner(List<String> list){
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_child.setAdapter(arrayAdapter);
        sp_child.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txt_phuong = sp_child.getSelectedItem().toString();
                text_address.setText("Địa chỉ: "+txt_phuong+", "+txt_quan);
                address = txt_phuong+", "+txt_quan;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getAllQuan(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(HoaDonActivity.this);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Address.url_getNameQuan, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Quan> list = new ArrayList<>();
                        for (int i=0;i<response.length();i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                list.add(new Quan(
                                   jsonObject.getInt("id_quan"),
                                   jsonObject.getString("ten_quan")
                                ));
                                Message msg = new Message();
                                msg.what = MSG_QUAN;
                                msg.obj  = list;
                                handler.sendMessage(msg);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread.start();
    }

    private void getAllPhuong(final int id_quan, final int msg_phuong){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(HoaDonActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Address.url_getTenPhuong, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Phuong> list = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                list.add(new Phuong(
                                   jsonObject.getInt("id_phuong"),
                                   jsonObject.getInt("id_quan"),
                                   jsonObject.getString("ten_phuong")
                                ));
                                Message msg = new Message();
                                msg.what = msg_phuong;
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

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> param = new HashMap<>();
                        param.put("id_quan",String.valueOf(id_quan));
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    @SuppressLint("ResourceAsColor")
    private void get_intent() {
        Intent intent = getIntent();
        name_sphd.setText(thongtin.getTen_mh());
        Picasso.get().load(thongtin.getImage()).into(image_sphd);
        gia_sphd.setText(String.valueOf(thongtin.getGia_mh())+"VNĐ");
        String note = intent.getStringExtra("ghichu");
        if (!note.trim().isEmpty()){
            ghichu_hd.setText(intent.getStringExtra("ghichu"));
        }else{
            ghichu_hd.setTextColor(getResources().getColor(R.color.bac));
            ghichu_hd.setText("(trống)");
        }

        soluong_hd.setText(intent.getStringExtra("soluong"));
        tongtien_hd.setText(intent.getStringExtra("tongtien"));
    }

    private void event_button(){
        thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_thanhtoan = thanhtoan.getText().toString().trim();
                if (text_thanhtoan.trim().equals("Thanh toán")){
                    button();
                }else{
                    Intent intent = new Intent(HoaDonActivity.this,MainActivity.class);
                    intent.putExtra("email",HomeLink.Email);
                    startActivity(intent);
                    thanhtoan.setEnabled(false);
                    thanhtoan.setText("Thanh toán");
                }
            }
        });
    }

    private void button(){
        String name = name_hd.getText().toString().trim();
        String sdt = sdt_hd.getText().toString().trim();
        String diachi = diachi_hd.getText().toString().trim();
        if (name.equals("") || sdt.equals("") || diachi.equals("")){
            Toast.makeText(HoaDonActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
        }else{
            if (check_phone()){
                postInsertHD();
            }else{
                Toast.makeText(this, "Kiểm tra lại thông tin", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void postInsertHD(){
        Intent intent = getIntent();
        String money = intent.getStringExtra("tongtien");
        money = money.substring(0,money.length()-3);
        tongtien = Integer.parseInt(money);

        long mTime = System.currentTimeMillis();
        Date curDateTime = new Date(mTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        final String curDate = formatter.format(curDateTime);

        String edit_diachihd = diachi_hd.getText().toString().trim();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(HoaDonActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, HomeLink.URL_POST_HD, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("success")){
                            Toast.makeText(HoaDonActivity.this, "Đặt thành công", Toast.LENGTH_SHORT).show();
                            thanhtoan.setText("Quay lại trang chủ");
                        }else{
                            Toast.makeText(HoaDonActivity.this, "Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HoaDonActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "Lỗi!\n " + error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("id_mh",String.valueOf(thongtin.getId_mh()));
                        param.put("id_person",HomeLink.id_person);
                        param.put("ten_kh",name_hd.getText().toString().trim());
                        param.put("ten_mh",name_sphd.getText().toString().trim());
//                        param.put("diachi",diachi_hd.getText().toString().trim());
                        param.put("diachi",edit_diachihd +", "+address);
                        param.put("sdt",sdt_hd.getText().toString().trim());
                        param.put("image",thongtin.getImage());
                        param.put("gia_mh",String.valueOf(thongtin.getGia_mh()));
                        param.put("sl_mh",soluong_hd.getText().toString().trim());
                        param.put("thanhtien",String.valueOf(tongtien));
                        param.put("ngaydat",curDate);
                        param.put("ghichu",ghichu_hd.getText().toString().trim());
                        param.put("trangthai","Đang đóng gói");
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
