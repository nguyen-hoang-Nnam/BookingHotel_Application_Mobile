package com.fpt.hotelbooking.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.fpt.hotelbooking.R;
import com.fpt.hotelbooking.model.Booking;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookings;

    public BookingAdapter(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_list_item, parent, false);
        return new BookingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);

        // Set room name
        holder.tvRoomName.setText(booking.getRoomName());

        // Set booking details
        holder.tvBookingDate.setText("Booking Date: " + formatDate(booking.getBookingDate()));
        holder.tvCheckIn.setText("Check-in: " + formatDate(booking.getCheckIn()));
        holder.tvCheckOut.setText("Check-out: " + formatDate(booking.getCheckOut()));
        holder.tvTotalPrice.setText("Total Price: " + booking.getTotalPrice() + " VND");

        String imageUrl = String.valueOf("https://media.istockphoto.com/id/1390233984/photo/modern-luxury-bedroom.jpg?s=612x612&w=0&k=20&c=po91poqYoQTbHUpO1LD1HcxCFZVpRG-loAMWZT7YRe4="); // Replace with actual image URL
        Glide.with(holder.imgRoom.getContext())
                .load(imageUrl)
                .into(holder.imgRoom);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvBookingDate, tvCheckIn, tvCheckOut, tvTotalPrice;
        ImageView imgRoom;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvCheckIn = itemView.findViewById(R.id.tvCheckIn);
            tvCheckOut = itemView.findViewById(R.id.tvCheckOut);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            imgRoom = itemView.findViewById(R.id.imgRoom);
        }
    }

    private String formatDate(String dateStr) {
        // Assuming dateStr is in ISO format
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        try {
            return displayFormat.format(isoFormat.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }
    }
}
