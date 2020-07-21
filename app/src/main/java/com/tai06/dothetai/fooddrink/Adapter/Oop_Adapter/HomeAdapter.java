package com.tai06.dothetai.fooddrink.Adapter.Oop_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tai06.dothetai.fooddrink.Fragment.HomeFragment;
import com.tai06.dothetai.fooddrink.Object.OOP.Home;
import com.tai06.dothetai.fooddrink.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<Home> mList;
    private Fragment mFrag;

    public static final int TYPE1 = 0;
    public static final int TYPE2 = 1;

    public HomeAdapter(List<Home> mList, Fragment mFrag) {
        this.mList = mList;
        this.mFrag = mFrag;
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
//        return new ViewHolder(view);
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = null;
        switch (viewType){
            case TYPE1:
                view = layoutInflater.inflate(R.layout.layout_item, parent, false);
                break;
            case TYPE2:
                view = layoutInflater.inflate(R.layout.layout_item2, parent, false);
                break;
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        Home home = mList.get(position);
        Picasso.get().load(home.getImage()).into(holder.img_product);
        holder.name_product.setText(home.getTen_mh());
        holder.price.setText(home.getGia_mh()+"VNĐ");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position%2 == 0){
            return TYPE2;
        }else{
            return TYPE1;
        }
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
                    ((HomeFragment)mFrag).showFood(home);
                }
            });
        }
    }
}
