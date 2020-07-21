package com.tai06.dothetai.fooddrink.Adapter.Oop_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tai06.dothetai.fooddrink.Activity.ViewPlusActivity;
import com.tai06.dothetai.fooddrink.Object.OOP.Home;
import com.tai06.dothetai.fooddrink.R;

import java.util.List;

public class ViewPlusAdapter extends RecyclerView.Adapter<ViewPlusAdapter.ViewHolder> {

    private List<Home> mList;
    private Context mContext;

    public ViewPlusAdapter(List<Home> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewPlusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_combo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPlusAdapter.ViewHolder holder, int position) {
        Home home = mList.get(position);
        Picasso.get().load(home.getImage()).into(holder.img_product);
        holder.name_product.setText(home.getTen_mh());
        holder.price.setText("Giá sản phẩm: "+home.getGia_mh()+"VNĐ");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product;
        TextView name_product,price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            name_product = itemView.findViewById(R.id.name_product);
            price = itemView.findViewById(R.id.price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Home home = mList.get(getAdapterPosition());
                    ((ViewPlusActivity)mContext).showProduct(home);
                }
            });
        }
    }
}
