package com.fpt.hotelbooking.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.fpt.hotelbooking.R;
import com.fpt.hotelbooking.activity.HotelDetailActivity;
import com.fpt.hotelbooking.model.Hotel;
import java.util.List;

public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.ViewHolder> {

    private final Context context;
    private final List<Hotel> hotelList;

    public HotelListAdapter(Context context, List<Hotel> hotelList) {
        this.context = context;
        this.hotelList = hotelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hotel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        holder.hotelName.setText(hotel.getHotelName());
        holder.hotelAddress.setText(hotel.getAddress());
        holder.ratingBar.setRating(hotel.getRatings());

        Glide.with(context)
                .load(hotel.getImage())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.hotelImage);

        holder.btnViewDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, HotelDetailActivity.class);
            intent.putExtra("hotelId", hotel.getHotelId());
            intent.putExtra("hotelName", hotel.getHotelName());
            intent.putExtra("hotelDescription", hotel.getHotelDescription());
            intent.putExtra("hotelImage", hotel.getImage());
            intent.putExtra("hotelAddress", hotel.getAddress());
            intent.putExtra("hotelRatings", (float) hotel.getRatings());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView hotelName;
        private final TextView hotelAddress;
        private final ImageView hotelImage;
        private final RatingBar ratingBar;
        private final Button btnViewDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelName = itemView.findViewById(R.id.hotelName);
            hotelAddress = itemView.findViewById(R.id.hotelAddress);
            hotelImage = itemView.findViewById(R.id.hotelImage);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }
    }
}
