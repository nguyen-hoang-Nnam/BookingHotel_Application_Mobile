package com.fpt.hotelbooking.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fpt.hotelbooking.R;
import com.fpt.hotelbooking.activity.BookingActivity;
import com.fpt.hotelbooking.activity.LoginActivity;
import com.fpt.hotelbooking.model.Room;

import java.util.List;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {

    private final Context context;
    private final List<Room> roomList;

    public RoomListAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.roomName.setText(room.getRoomName());
        holder.roomDescription.setText(room.getRoomDes());
        holder.roomPrice.setText(room.getPricePerDay() + " VND");
        holder.guestNumber.setText(room.getGuestNumber() + "");
        holder.roomType.setText("Type: " +room.getRoomTypeName());
        holder.roomSize.setText("Size: "+room.getRoomSize() +" m²");
        if (room.getRoomStatus() == 2) {
            holder.roomStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
            holder.roomStatus.setText(R.string.available);
        } else {
            holder.roomStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
            holder.roomStatus.setText(R.string.unavailable);
        }

        Glide.with(context)
                .load(room.getImage())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.roomImage);

        holder.btnBookRoom.setOnClickListener(v -> {
            if (room.getRoomStatus() == 2) {
                SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
                if (isLoggedIn) {
                    Intent intent = new Intent(context, BookingActivity.class);
                    intent.putExtra("roomId", room.getRoomId());
                    intent.putExtra("roomName", room.getRoomName());
                    intent.putExtra("price", (float) room.getPricePerDay());
                    intent.putExtra("roomImage", room.getImage());
                    context.startActivity(intent);
                } else {
                    Intent loginIntent = new Intent(context, LoginActivity.class);
                    context.startActivity(loginIntent);
                }
            } else {
                new AlertDialog.Builder(context)
                        .setTitle("Unavailable")
                        .setMessage("This room is currently unavailable for booking.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList == null ? 0 : roomList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView roomName, roomDescription, roomPrice, guestNumber, roomStatus, roomSize, roomType, btnBookRoom;
        private final ImageView roomImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.roomName);
            roomDescription = itemView.findViewById(R.id.roomDescription);
            roomPrice = itemView.findViewById(R.id.roomPrice);
            roomImage = itemView.findViewById(R.id.roomImage);
            guestNumber = itemView.findViewById(R.id.roomGuest);
            roomStatus = itemView.findViewById(R.id.roomStatus);
            roomSize = itemView.findViewById(R.id.roomSize);
            roomType = itemView.findViewById(R.id.roomType);
            btnBookRoom = itemView.findViewById(R.id.btnBookRoom);
        }
    }
}
