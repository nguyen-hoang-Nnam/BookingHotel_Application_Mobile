package com.fpt.hotelbooking.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CountryImageProvider {

    private static final List<String> countryImageUrls = new ArrayList<>();
    private static final Random random = new Random();

    static {
        countryImageUrls.add("https://images.pexels.com/photos/169647/pexels-photo-169647.jpeg?cs=srgb&dl=pexels-peng-liu-45946-169647.jpg&fm=jpg");
        countryImageUrls.add("https://images.pexels.com/photos/4270292/pexels-photo-4270292.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        countryImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/thumb/0/00/Khu_trung_t%C3%A2m_th%C3%A0nh_ph%E1%BB%91_H%E1%BB%93_Ch%C3%AD_Minh%2C_nh%C3%ACn_t%E1%BB%AB_ph%C3%ADa_qu%E1%BA%ADn_2.JPG/2560px-Khu_trung_t%C3%A2m_th%C3%A0nh_ph%E1%BB%91_H%E1%BB%93_Ch%C3%AD_Minh%2C_nh%C3%ACn_t%E1%BB%AB_ph%C3%ADa_qu%E1%BA%ADn_2.JPG");
        countryImageUrls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_eKDoLUqF5JuB9_gx-yYvQhkdaIeDR1urG6Yuf4KqdT8_7qEn6SfS6a5a4EEDuSG6hOY&usqp=CAU");
        countryImageUrls.add("https://danviet.mediacdn.vn/upload/2-2018/images/2018-05-28/1-1527481499-width650height410.jpg");
        countryImageUrls.add("https://png.pngtree.com/thumb_back/fh260/background/20210910/pngtree-k-wuhan-city-night-scene-cyberpunk-atmospheric-aerial-photography-night-scene-image_848626.jpg");
        countryImageUrls.add("https://cdnphoto.dantri.com.vn/YsHcZ_WkF1-lKr-en4mX_9dYKm8=/2021/04/30/dji-0788-hdr-panoa-crop-1619717280597.jpeg");
        countryImageUrls.add("https://cdnphoto.dantri.com.vn/Agu6m7W6UnDYpjIBKafpjvl43j8=/2021/04/28/nga-6-phudong-1619582769213.jpg");
    }

    public static String getRandomImage() {
        return countryImageUrls.get(random.nextInt(countryImageUrls.size()));
    }
}
