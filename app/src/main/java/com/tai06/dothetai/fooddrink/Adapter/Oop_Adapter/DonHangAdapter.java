package com.tai06.dothetai.fooddrink.Adapter.Oop_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.tai06.dothetai.fooddrink.Contacts.HomeLink;
import com.tai06.dothetai.fooddrink.Fragment.DonHangFragment;
import com.tai06.dothetai.fooddrink.Object.OOP.Hoadon;
import com.tai06.dothetai.fooddrink.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.ViewHolder> {

    private List<Hoadon> mList;
    private Fragment mFrag;

    public DonHangAdapter(List<Hoadon> mList, Fragment mFrag) {
        this.mList = mList;
        this.mFrag = mFrag;
    }

    @NonNull
    @Override
    public DonHangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_donhang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DonHangAdapter.ViewHolder holder, final int position) {
        final Hoadon hoadon = mList.get(position);
        Picasso.get().load(hoadon.getImage()).into(holder.img_product);
        holder.name_product.setText(hoadon.getTen_mh());
        holder.soluong.setText(String.valueOf(hoadon.getSl_mh()));
        holder.price.setText(String.valueOf(hoadon.getThanhtien())+"VNĐ");
        holder.status.setText(hoadon.getTrangthai());
        if (hoadon.getTrangthai().equals("Đã hủy")){
            holder.cancel_donhang.setText("Xoá");
        }
        holder.cancel_donhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textBtn = holder.cancel_donhang.getText().toString().trim();
                if (textBtn.trim().equals("Hủy đơn hàng")){
                    RequestQueue requestQueue = Volley.newRequestQueue(mFrag.getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, HomeLink.URL_CANCEL_HD, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("success")){
                                Toast.makeText(mFrag.getActivity(), "Hệ thống đã xử lí", Toast.LENGTH_SHORT).show();
                                holder.status.setText("Đã hủy");
                            }else{
                                Toast.makeText(mFrag.getActivity(),"Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(mFrag.getActivity(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                            Log.d("AAA", "Lỗi!\n " + error.toString());
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> param = new HashMap<>();
                            param.put("id_hd",String.valueOf(hoadon.getId_hd()));
                            return param;
                        }
                    };
                    requestQueue.add(stringRequest);
                    holder.cancel_donhang.setText("Xóa");
                }else{
//                    mList.remove(position);
//                    ((DonHangFragment) mFrag).setAdapterDonhang(mList);
                    process_delete(hoadon,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product;
        TextView name_product,price,status,soluong;
        Button cancel_donhang;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            name_product = itemView.findViewById(R.id.name_product);
            price = itemView.findViewById(R.id.price);
            soluong = itemView.findViewById(R.id.soluong);
            status = itemView.findViewById(R.id.status);
            cancel_donhang = itemView.findViewById(R.id.cancel_donhang);
        }
    }

    private void process_delete(final Hoadon hoadon, final int index){
        RequestQueue requestQueue = Volley.newRequestQueue(mFrag.getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HomeLink.URL_DELTE_HD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(mFrag.getActivity(), "Đã xóa", Toast.LENGTH_SHORT).show();
//                    ((DonHangFragment)mFrag).setAdapterDonhang(mList);
//                    ((DonHangFragment)mFrag).getDonHang(HomeLink.URL_GET_HD);
                    mList.remove(index);
                    ((DonHangFragment) mFrag).setAdapterDonhang(mList);
                }else{
                    Toast.makeText(mFrag.getActivity(),"Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mFrag.getActivity(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                Log.d("AAA", "Lỗi!\n " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("id_hd",String.valueOf(hoadon.getId_hd()));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
}
