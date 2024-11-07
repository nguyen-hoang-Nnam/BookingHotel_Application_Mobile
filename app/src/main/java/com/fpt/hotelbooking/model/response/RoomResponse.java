package com.fpt.hotelbooking.model.response;
import com.fpt.hotelbooking.model.Room;

import java.util.List;

public class RoomResponse {
    private int hotelId;
    private String hotelName;
    private String hotelDescription;
    private String image;
    private String address;
    private int ratings;
    private int countryId;
    private String countryName;
    private List<Room> rooms;

    public int getHotelId() { return hotelId; }
    public void setHotelId(int hotelId) { this.hotelId = hotelId; }

    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }

    public String getHotelDescription() { return hotelDescription; }
    public void setHotelDescription(String hotelDescription) { this.hotelDescription = hotelDescription; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getRatings() { return ratings; }
    public void setRatings(int ratings) { this.ratings = ratings; }

    public int getCountryId() { return countryId; }
    public void setCountryId(int countryId) { this.countryId = countryId; }

    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }

    public List<Room> getRooms() { return rooms; }
    public void setRooms(List<Room> rooms) { this.rooms = rooms; }
}
