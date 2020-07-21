package com.tai06.dothetai.fooddrink.Adapter.Slide_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.tai06.dothetai.fooddrink.Object.Slide.SlideHome;
import com.tai06.dothetai.fooddrink.R;

import java.util.List;

public class SlideHomeAdapter extends PagerAdapter {

    private Fragment mFrag;
    private List<SlideHome> mList;

    public SlideHomeAdapter(Fragment mFrag, List<SlideHome> mList) {
        this.mFrag = mFrag;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mFrag.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_item, container, false);
        ImageView imageView = view.findViewById(R.id.img_item_slide);
        Picasso.get().load(mList.get(position).getImage()).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
