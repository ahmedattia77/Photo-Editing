package com.example.photoediting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder>{

    private ArrayList<SliderItem> sliderItemArrayList;
    private ViewPager2 viewPager2;

    public SliderAdapter(ArrayList<SliderItem> sliderItemArrayList, ViewPager2 viewPager2) {
        this.sliderItemArrayList = sliderItemArrayList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_container ,
                parent,
                false));

    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
            holder.setImage(sliderItemArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderItemArrayList.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView imageView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlider);
        }
        void setImage(SliderItem sliderItem){
            imageView.setImageResource(sliderItem.getItem());
        }

    }

}
