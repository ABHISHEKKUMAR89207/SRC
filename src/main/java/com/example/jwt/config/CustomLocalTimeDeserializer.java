//package com.example.jwt.config;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.exc.InvalidFormatException;
//
//import java.io.IOException;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//
//public class CustomLocalTimeDeserializer extends JsonDeserializer<LocalTime> {
//
//    private static final DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh:mm a");
//    private static final DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");
//
//    @Override
//    public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
//        String timeString = jsonParser.getText().trim();
//        try {
//            // Attempt to parse with 12-hour format first
//            return LocalTime.parse(timeString, formatter12Hour);
//        } catch (Exception e) {
//            try {
//                // If 12-hour format fails, try parsing with 24-hour format
//                return LocalTime.parse(timeString, formatter24Hour);
//            } catch (Exception ex) {
//                throw new InvalidFormatException(jsonParser, "Invalid time format: " + timeString, timeString, LocalTime.class);
//            }
//        }
//    }
//}
