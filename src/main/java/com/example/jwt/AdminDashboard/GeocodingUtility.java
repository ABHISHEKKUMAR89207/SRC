package com.example.jwt.AdminDashboard;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.springframework.stereotype.Service;



public class GeocodingUtility {

    private static final String API_KEY = "AIzaSyCshTBR_R1_NGWYMj2GZrw_4MLd39YOqj8AIzaSyCshTBR_R1_NGWYMj2GZrw_4MLd39YOqj8";

//    public static String getAddressFromCoordinates(Double latitude, Double longitude) {
//        try {
//            GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();
//            GeocodingResult[] results = GeocodingApi.newRequest(context)
//                    .latlng(new com.google.maps.model.LatLng(latitude, longitude)).await();
//
//            if (results != null && results.length > 0) {
//                return results[0].formattedAddress;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public String getAddressFromCoordinates(Double latitude, Double longitude) {
        try {
            GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();
            GeocodingResult[] results = GeocodingApi.newRequest(context)
                    .latlng(new com.google.maps.model.LatLng(latitude, longitude)).await();

            if (results != null && results.length > 0) {
                return results[0].formattedAddress;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
