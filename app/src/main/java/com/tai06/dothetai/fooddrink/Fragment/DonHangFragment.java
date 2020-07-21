package com.tai06.dothetai.fooddrink.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.tai06.dothetai.fooddrink.Adapter.Oop_Adapter.DonHangAdapter;
import com.tai06.dothetai.fooddrink.Contacts.HomeLink;
import com.tai06.dothetai.fooddrink.Object.OOP.Hoadon;
import com.tai06.dothetai.fooddrink.Object.OOP.Person;
import com.tai06.dothetai.fooddrink.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonHangFragment extends Fragment {

    public static final int MSG_DONHANG = 1;

    private View view;
    private RecyclerView recycle_donhang;
    private Handler handler;
    private List<Hoadon> arrHoadon;
    private DonHangAdapter donHangAdapter;
    private ProgressDialog progressDialog;
    private TextView empty_donhang;

    public DonHangFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_don_hang, container, false);
        init();
        initHandler();
        progress_dialog();
        check_donhang();
        return view;
    }

    private void init(){
        recycle_donhang = view.findViewById(R.id.recycle_donhang);
        empty_donhang = view.findViewById(R.id.empty_donhang);
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
                    case MSG_DONHANG:
                        arrHoadon = new ArrayList<>();
                        arrHoadon.addAll((Collection<? extends Hoadon>) msg.obj);
                        setAdapterDonhang(arrHoadon);
                        break;
                }
            }
        };
    }

    public void setAdapterDonhang(List<Hoadon> list){
        donHangAdapter = new DonHangAdapter(list,DonHangFragment.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1,GridLayoutManager.VERTICAL,false);
        recycle_donhang.setLayoutManager(gridLayoutManager);
        recycle_donhang.setAdapter(donHangAdapter);
        recycle_donhang.setHasFixedSize(true);
        donHangAdapter.notifyDataSetChanged();
    }

    private void postDonhang(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, HomeLink.URL_GETpost_HD, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Hoadon> list = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                list.add(new Hoadon(
                                        jsonObject.getInt("id_hd"),
                                        jsonObject.getInt("id_mh"),
                                        jsonObject.getInt("id_person"),
                                        jsonObject.getString("ten_kh"),
                                        jsonObject.getString("ten_mh"),
                                        jsonObject.getString("diachi"),
                                        jsonObject.getString("sdt"),
                                        jsonObject.getString("image"),
                                        jsonObject.getInt("gia_mh"),
                                        jsonObject.getInt("sl_mh"),
                                        jsonObject.getInt("thanhtien"),
                                        jsonObject.getString("ngaydat"),
                                        jsonObject.getString("ghichu"),
                                        jsonObject.getString("trangthai")
                                ));
                                Message msg = new Message();
                                msg.what = MSG_DONHANG;
                                msg.obj = list;
                                handler.sendMessage(msg);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Xảy ra lỗi !", Toast.LENGTH_SHORT).show();
                        Log.d("AAA","Error" + error.toString());
                        progressDialog.dismiss();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("id_person",HomeLink.id_person);
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    private void check_donhang(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, HomeLink.URL_CHECK_DONHANG, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")){
                            progressDialog.dismiss();
                            empty_donhang.setVisibility(View.VISIBLE);
                            recycle_donhang.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Bạn chưa có đơn hàng nào!", Toast.LENGTH_SHORT).show();
                        }else{
                            empty_donhang.setVisibility(View.GONE);
                            recycle_donhang.setVisibility(View.VISIBLE);
                            postDonhang();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("AAA","Error"+error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("id_person",HomeLink.id_person);
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }
}
