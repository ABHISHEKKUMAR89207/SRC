package com.example.jwt.entities.dashboardEntity.healthTrends;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        TextNode node = p.getCodec().readTree(p);
        String dateText = node.textValue();

        // Define the expected date format and parser
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");

        // Attempt to parse the date with the expected format
        try {
            return LocalDate.parse(dateText, formatter);
        } catch (Exception e) {
            // Handle parsing errors here (e.g., log the error, throw a custom exception, etc.)
            throw new IllegalArgumentException("Invalid date format: " + dateText);
        }
    }
}
