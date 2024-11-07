package com.fpt.hotelbooking.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fpt.hotelbooking.R;
import com.fpt.hotelbooking.model.Hotel;

import java.util.List;

public class HotelCarouselAdapter extends RecyclerView.Adapter<HotelCarouselAdapter.ViewHolder> {

    private final List<Hotel> hotels;
    private final Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Hotel hotel);
    }

    public HotelCarouselAdapter(Context context, List<Hotel> hotels, OnItemClickListener listener) {
        this.context = context;
        this.hotels = hotels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hotel_carousel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hotel hotel = hotels.get(position);
        holder.hotelName.setText(hotel.getHotelName());
        holder.ratingBar.setRating(hotel.getRatings());

        Log.d("HotelCarouselAdapter", "Displaying hotel: " + hotel.getHotelName());

        Glide.with(context)
                .load(hotel.getImage())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.hotelImage);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(hotel));
    }


    @Override
    public int getItemCount() {
        return hotels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView hotelName;
        private final ImageView hotelImage;
        private final RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelName = itemView.findViewById(R.id.hotelName);
            hotelImage = itemView.findViewById(R.id.hotelImage);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
