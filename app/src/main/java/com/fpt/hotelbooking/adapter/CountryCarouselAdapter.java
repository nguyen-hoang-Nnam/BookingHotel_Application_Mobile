package com.fpt.hotelbooking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fpt.hotelbooking.R;

import java.util.List;

public class CountryCarouselAdapter extends RecyclerView.Adapter<CountryCarouselAdapter.ViewHolder> {

    private final List<String> countryNames;
    private final List<String> countryImages;
    private final Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position); // Make sure it accepts `int position`
    }

    public CountryCarouselAdapter(Context context, List<String> countryNames, List<String> countryImages, OnItemClickListener listener) {
        this.context = context;
        this.countryNames = countryNames;
        this.countryImages = countryImages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_country_carouse, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.countryName.setText(countryNames.get(position));

        // Load image from URL using Glide
        Glide.with(context)
                .load(countryImages.get(position))
                .placeholder(R.drawable.placeholder_image)
                .into(holder.countryImage);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(position)); // Pass position to listener
    }

    @Override
    public int getItemCount() {
        return countryNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView countryName;
        private final ImageView countryImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.countryName);
            countryImage = itemView.findViewById(R.id.countryImage);
        }
    }
}
